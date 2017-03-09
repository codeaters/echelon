package com.app.innovationweek;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.app.innovationweek.model.Option;
import com.app.innovationweek.model.Question;
import com.app.innovationweek.util.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QuestionActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "QuestionActivity";

    @BindView(R.id.radio_group_options)
    RadioGroup optionRadioGroup;
    @BindView(R.id.submit)
    Button submit;
    @BindView(R.id.statement)
    TextView textViewQuestionStatement;
    @BindView(R.id.timer)
    TextView timer;
    @BindView(R.id.edit_text_fib)
    EditText editTextFIB;
    @BindView(R.id.image)
    ImageView questionImageView;

    @BindView(R.id.progress)
    View progress;
    @BindView(R.id.progress_msg)
    TextView progressMsg;

    @BindView(R.id.content)
    View content;
    @BindView(R.id.error)
    View error;

    @BindView(R.id.retry)
    Button retry;
    @BindView(R.id.error_msg)
    TextView errorMsg;

    private CountDownTimer countDownTimer;
    private DatabaseReference dbRef, questionRef, resonseRef;
    private String quizId, questionId, Uid;
    private ValueEventListener questionListener, responseListener;
    private long startTime;

    /**
     * The question this activity is showing
     * TODO: DO NOT USE THIS instance variable yet
     */
    private Question question;

    {
        questionListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Log.d(TAG, "datasnap after fetching: " + dataSnapshot.toString());

                question = dataSnapshot.getValue(Question.class);

                resonseRef.addListenerForSingleValueEvent(responseListener);
                //

                //todo: hide progress

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "Question Listener", databaseError.toException());
                //todo: hide progress
            }
        };
        responseListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "response datasnbap:" + dataSnapshot);
                if (dataSnapshot.getValue() == null) {
                    //TODO: show progress
                    resonseRef.child("startTime").setValue(ServerValue.TIMESTAMP)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        resonseRef.child("startTime")
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        if (dataSnapshot == null)
                                                            return;
                                                        startTime = dataSnapshot.getValue(Long.class);
                                                        resonseRef.child("endTime").setValue
                                                                (startTime + question.getMaxTime()
                                                                        * 60000);
                                                        calculateTime(startTime);
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });
                                    }
                                }
                            });
                } else {
                    if (dataSnapshot.hasChild("startTime")) {
                        startTime = dataSnapshot.child("startTime").getValue(Long.class);
                        calculateTime(startTime);
                    } else {
                        Log.d(TAG, "no startTime");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "response failed:" + databaseError);
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        ButterKnife.bind(this);
        retry.setOnClickListener(this);
        submit.setOnClickListener(this);
        showProgress(null);
        //initialize the question listener
        dbRef = FirebaseDatabase.getInstance().getReference();
        Uid = Utils.getUid(getApplicationContext());
        if (savedInstanceState == null) {
            Intent launchIntent = getIntent();
            if (launchIntent != null) {
                Bundle bundle = launchIntent.getExtras();
                quizId = bundle.getString("quiz_id");
                questionId = bundle.getString("question_id");
                Log.d(TAG, "Question ID is:" + questionId + " " + quizId);
            }//TOOD: show messages aleady answered or yet to appear
        } else {
            quizId = savedInstanceState.getString("quiz_id");
            questionId = savedInstanceState.getString("question_id");
        }
        questionRef = dbRef.child(quizId).child(questionId);
        resonseRef = dbRef.child("response").child(quizId).child(Uid).child(questionId);
        questionRef.addListenerForSingleValueEvent(questionListener);
    }

    private void showProgress(String message) {
        progressMsg.setText(message == null || message.isEmpty() ? getString(R.string.loading)
                : message);
        progress.setVisibility(View.VISIBLE);
        content.setVisibility(View.GONE);
        error.setVisibility(View.GONE);


    }

    private void hideProgress(boolean showError, String errorMessage) {
        progress.setVisibility(View.GONE);
        if (showError) {
            content.setVisibility(View.GONE);
            error.setVisibility(View.VISIBLE);
        } else {
            content.setVisibility(View.VISIBLE);
            error.setVisibility(View.GONE);
        }
    }


    public void onRadioButtonOptionClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // TODO: 08-03-2017 Some things
    }

    /**
     * Sets the question to the layout
     * Question id is set as tag in the question Statement text view
     * Option ids are set as tag in the option radio button
     * The optionId of the correct Option is stored as tag in the radioGroup
     */
    private void setQuestion() {

        textViewQuestionStatement.setTag(question.getQuestionId());
        textViewQuestionStatement.setText(question.getStatement());
        // set the options if options are present
        if (question.getOptions() == null && question.getOptions().size() == 0) {
            // this is an fib question, so hide the options
            optionRadioGroup.setVisibility(View.GONE);
            // and prepare the view for fib
            editTextFIB.setVisibility(View.VISIBLE);
            // and hide the radio group
            optionRadioGroup.setVisibility(View.GONE);
        } else {
            // set the options
            int i = 0;
            for (Map.Entry<String, Option> oentry : question.getOptions().entrySet()) {

                RadioButton radioButton = ((RadioButton) optionRadioGroup.getChildAt(i++));
                radioButton.setText(oentry.getValue().getValue());
                radioButton.setTag(oentry.getKey());
                if (oentry.getValue().getCorrect())
                    optionRadioGroup.setTag(oentry.getKey());
            }
            // show the options
            optionRadioGroup.setVisibility(View.VISIBLE);
            //hide the fib
            editTextFIB.setVisibility(View.GONE);
        }
        // check if the optional image uri is present
        if (question.getImgUri() != null) {
            //show the image view
            questionImageView.setVisibility(View.VISIBLE);
            //load the image using Picasso
            Picasso.with(this)
                    .load(question.getImgUri())
                    .placeholder(R.drawable.loading_placeholder)
                    .error(R.drawable.loading_placeholder)
                    .into(questionImageView, new Callback() {
                        @Override
                        public void onSuccess() {
//                            startTimer();
                        }

                        @Override
                        public void onError() {

                        }
                    });

        } else {
            questionImageView.setVisibility(View.GONE);
        }
    }


    // 10 minutes = 1000 * 60 * 10 milliseconds
    // check if the timer had already started by Fetching remaining seconds from firebase RTD for that user
    // if yes, start the countdown timer with those milli seconds
    //if no start the timer with initial  value 10 minutes and System.currentTimeMillis. persist the states in RDB
    //if yes, start the timer with the remaining seconds and persist the data base
    private void calculateTime(long startTime) {
        //max time is 15 minutes hard coded
        hideProgress(false, null);
        final long elapsedSeconds = (System.currentTimeMillis() - startTime) / 1000;
        countDownTimer = new CountDownTimer(question.getMaxTime() * 60000, 1000) {
            long minLeft, secLeft;

            @Override
            public void onTick(long millisecondsLeft) {
                secLeft = millisecondsLeft / 1000;
                if (secLeft > 60) {
                    minLeft = secLeft / 60;
                    secLeft = secLeft % 60;
                    timer.setText(getString(R.string.timer_msg, minLeft, secLeft));
                } else {
                    timer.setText(getString(R.string.timer_msg_sec, secLeft));
                }
            }

            @Override
            public void onFinish() {
                timer.setText(getString(R.string.timer_expire));
                submit.setEnabled(false);
            }
        };
        countDownTimer.start();
        setQuestion();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.retry:

                break;
            case R.id.submit:
                showProgress(getString(R.string.saving_response));
                View markedOption = findViewById(optionRadioGroup.getCheckedRadioButtonId());
                if (markedOption != null)
                    saveResponse((String) markedOption.getTag());
                else
                    saveResponse(null);
                break;
        }
    }

    private void saveResponse(final String checkedOptionKey) {
        //TODO save Response for now stimulating network request
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    for (int i = 0; i < 5; i++) {
                        Thread.sleep(500);
                    }
                } catch (InterruptedException ex) {
                    Log.d(TAG, "thread sleep interrupted");
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                String msg;
                if (checkedOptionKey != null && !checkedOptionKey.isEmpty()) {
                    if (((String) optionRadioGroup.getTag()).equals(checkedOptionKey)) {
                        msg = "You marked the RIGHT answer";
                    } else {
                        msg = "You marked the WRONG answer";
                    }
                } else {
                    msg = "no options marked";
                }
                Toast.makeText(getApplicationContext(), msg, Toast
                        .LENGTH_LONG).show();
                finish();
            }
        }.execute();
    }
}
