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

import butterknife.BindView;
import butterknife.ButterKnife;

public class QuestionActivity extends AppCompatActivity {

    private static final String TAG = "QuestionActivity";

    @BindView(R.id.radio_group_options)
    RadioGroup optionRadioGroup;

    @BindView(R.id.button_response)
    Button responseButton;

    @BindView(R.id.text_view_question_statement)
    TextView textViewQuestionStatement;

    @BindView(R.id.text_view_timer)
    TextView timer;

    @BindView(R.id.edit_text_fib)
    EditText editTextFIB;

    @BindView(R.id.image_view_question_image)
    ImageView questionImageView;
    CountDownTimer countDownTimer;
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
                final Question question = dataSnapshot.getValue(Question.class);
                setQuestion(question);
                //
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
                //todo: hide progress

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "Load cancelled.", databaseError.toException());
                //todo: hide progress
            }
        };
        responseListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot == null) {
                    //TODO: show progress
                    questionRef.addListenerForSingleValueEvent(questionListener);

                } else {
                    if (dataSnapshot.hasChild("startTime")) {
                        startTime = dataSnapshot.child("startTime").getValue(Long.class);
                        calculateTime(startTime);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        ButterKnife.bind(this);
        //initialize the question listener

        Intent launchIntent = getIntent();
        if (launchIntent != null) {
            quizId = launchIntent.getStringExtra("quiz_id");
            questionId = launchIntent.getStringExtra("question_id");
            Log.d(TAG, "Question ID is:" + questionId);
            resonseRef = dbRef.child(quizId).child(Uid).child(questionId);
            resonseRef.addValueEventListener(responseListener);
//            questionRef.addListenerForSingleValueEvent(questionListener);
        }//TOOD: show messages aleady answered or yet to appear
        Uid = Utils.getUid(getApplicationContext());
        dbRef = FirebaseDatabase.getInstance().getReference();
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
    private void setQuestion(Question question) {


        textViewQuestionStatement.setTag(question.getQuestionId());
        textViewQuestionStatement.setText(question.getQuestionStatement());
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
            for (int i = 0; i < question.getOptions().size(); i++) {
                Option option = question.getOptions().get(i);
                RadioButton radioButton = ((RadioButton) optionRadioGroup.getChildAt(i));
                radioButton.setText(option.getOptionValue());
                radioButton.setTag(option.getOptionId());
                if (option.isCorrect())
                    optionRadioGroup.setTag(option.getOptionId());
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
                            startTimer();
                        }

                        @Override
                        public void onError() {

                        }
                    });

        } else {
            questionImageView.setVisibility(View.GONE);
        }
    }

    private void startTimer() {
        // 10 minutes = 1000 * 60 * 10 milliseconds
        // check if the timer had already started by Fetching remaining seconds from firebase RTD for that user
        // if yes, start the countdown timer with those milli seconds
        //if no start the timer with initial  value 10 minutes and System.currentTimeMillis. persist the states in RDB
        //if yes, start the timer with the remaining seconds and persist the data base
    }

    private void calculateTime(long startTime) {
        //max time is 15 minutes hard coded
        final long elapsedSeconds = (System.currentTimeMillis() - startTime) / 1000;
        countDownTimer = new CountDownTimer(startTime + question.getMaxTime() * 60000, 1000) {

            @Override
            public void onTick(long millisecondsLeft) {
                if (millisecondsLeft > 60000) {
                    timer.setText(getString(R.string.timer_msg, millisecondsLeft / 60000, "minutes"));
                } else if (question.getMaxTime() * 60 - elapsedSeconds > 60) {
                    timer.setText(getString(R.string.timer_msg, millisecondsLeft / 1000, "seconds"));
                }
            }

            @Override
            public void onFinish() {
                timer.setText(getString(R.string.timer_expire));
                responseButton.setEnabled(false);
            }
        };
        countDownTimer.start();
    }
}
