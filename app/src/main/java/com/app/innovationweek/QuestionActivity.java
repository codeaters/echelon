package com.app.innovationweek;

import android.content.Intent;
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
import com.app.innovationweek.model.Response;
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
    private DatabaseReference dbRef, questionRef, responseRef;
    private String quizId, questionId, Uid;
    private ValueEventListener questionListener, responseListener;
    private long startTime;
    /**
     * The question this activity is showing
     *
     */
    private Question question;

    {
        questionListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Log.d(TAG, "datasnap after fetching: " + dataSnapshot.toString());

                question = dataSnapshot.getValue(Question.class);

                responseRef.addListenerForSingleValueEvent(responseListener);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "Question Listener", databaseError.toException());
            }
        };
        responseListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "response datasnbap:" + dataSnapshot);
                if (dataSnapshot.getValue() == null) {
                    // Record start time
                    responseRef.child("startTime").setValue(ServerValue.TIMESTAMP);
                    Toast.makeText(getApplicationContext(), "Start Time Recorded", Toast.LENGTH_LONG).show();
                } else {
                    if (dataSnapshot.hasChild("startTime")) {
                    } else {
                        Log.d(TAG, "no startTime");
                    }
                }
                calculateTime();
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
            }//TODO: show messages aleady answered or yet to appear
        } else {
            quizId = savedInstanceState.getString("quiz_id");
            questionId = savedInstanceState.getString("question_id");
        }
        questionRef = dbRef.child(quizId).child(questionId);
        responseRef = dbRef.child("response").child(quizId).child(Uid).child(questionId);
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
     * The correct fib response is stored as tag in fib edit text
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
            // and save the correct response as tag
            editTextFIB.setTag(question.getFibAnswer());
            // and hide the radio group
            optionRadioGroup.setVisibility(View.GONE);
        } else {
            // set the options
            int i = 0;
            for (Map.Entry<String, Option> entry : question.getOptions().entrySet()) {

                RadioButton radioButton = ((RadioButton) optionRadioGroup.getChildAt(i++));
                radioButton.setText(entry.getValue().getValue());
                radioButton.setTag(entry.getKey());
                if (entry.getValue().getCorrect())
                    optionRadioGroup.setTag(entry.getKey());
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
//                            //startTimer();
                        }

                        @Override
                        public void onError() {

                        }
                    });

        } else {
            questionImageView.setVisibility(View.GONE);
        }
        hideProgress(false, null);
    }

    // 10 minutes = 1000 * 60 * 10 milliseconds
    // check if the timer had already started by Fetching remaining seconds from firebase RTD for that user
    // if yes, start the countdown timer with those milli seconds
    //if no start the timer with initial  value 10 minutes and System.currentTimeMillis. persist the states in RDB
    //if yes, start the timer with the remaining seconds and persist the data base
    private void calculateTime() {
        //max time is 15 minutes hard coded

        final long elapsedSeconds = (System.currentTimeMillis() - question.getStartTime()) / 1000;
        System.out.println(TAG + ": Elapsed seconds are:" + elapsedSeconds);
        countDownTimer = new CountDownTimer((question.getMaxTime() * 60 - elapsedSeconds) * 1000, 1000) {
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
                //this was a mcq
                if (question.getOptions() != null && question.getOptions().size() != 0) {
                    String answer = (String) findViewById(optionRadioGroup.getCheckedRadioButtonId()).getTag();
                    int score = optionRadioGroup.getTag().toString().equals(answer) ? 1 : 0;
                    saveResponse(answer, score);
                } else /*This was an fib*/ {
                    String answer = editTextFIB.getText().toString();
                    int score = editTextFIB.getTag().toString().equals(answer) ? 1 : 0;
                    saveResponse(answer, score);
                }
                break;
        }
    }

    private void saveResponse(final String answer, final int score) {
        responseRef.child("endTime").setValue(ServerValue.TIMESTAMP).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    //start and end time recorded. Now get values, compare time difference and save score
                    responseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Response response = dataSnapshot.getValue(Response.class);
                            int difference = (int) (response.getEndTime() - response.getStartTime());

                            System.out.println(TAG + " Difference is: " + difference);


                            if (question.getStartTime() > response.getEndTime() || question.getEndTime() < response.getEndTime()) {
                                Toast.makeText(getApplicationContext(), "You exceeded the time limit. Your response is invalid.", Toast.LENGTH_LONG).show();
                                response.setScore(0);
                                response.setResponse(answer);
                                response.setDuration(difference);
                                response.setLimitExceeded(true);
                            } else {
                                //update score and time duration for that response, although duration is redundant
                                response.setScore(score);
                                response.setResponse(answer);
                                response.setDuration(difference);
                                response.setLimitExceeded(false);
                            }
                            responseRef.setValue(response).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(getApplicationContext(), "Your response has been saved.", Toast.LENGTH_LONG).show();
                                    QuestionActivity.this.finish();
                                }
                            });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            }
        });
    }

    private enum QuestionType {FIB, MCQ}
}
