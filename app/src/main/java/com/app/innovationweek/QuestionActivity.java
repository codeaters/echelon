package com.app.innovationweek;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

    @BindView(R.id.edit_text_fib)
    EditText editTextFIB;

    @BindView(R.id.image_view_question_image)
    ImageView questionImageView;

    private DatabaseReference mDatabase;

    private ValueEventListener questionListener;

    /**
     * The question this activity is showing
     * TODO: DO NOT USE THIS instance variable yet
     */
    private Question question;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setContentView(R.layout.activity_question2);

        //initialize the question listener
        questionListener= new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Question question = dataSnapshot.getValue(Question.class);
                setQuestion(question);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "Load cancelled.", databaseError.toException());
            }
        };

        Intent launchIntent = getIntent();
        if(launchIntent!= null){
            String qid = launchIntent.getStringExtra("qid");
            Log.d(TAG, "Question ID is:" +qid);
            mDatabase = FirebaseDatabase.getInstance().getReference("questions/"+qid);
            mDatabase.addListenerForSingleValueEvent(questionListener);
        }

        mDatabase = FirebaseDatabase.getInstance().getReference("questions/qid");




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
    private void setQuestion(Question question){


        textViewQuestionStatement.setTag(question.getQuestionId());
        textViewQuestionStatement.setText(question.getQuestionStatement());
        // set the options if options are present
        if(question.getOptions()==null && question.getOptions().size() == 0){
            // this is an fib question, so hide the options
            optionRadioGroup.setVisibility(View.GONE);
            // and prepare the view for fib
            editTextFIB.setVisibility(View.VISIBLE);
            // and hide the radio group
            optionRadioGroup.setVisibility(View.GONE);
        }else
        {
            // set the options
            for(int i=0;i < question.getOptions().size(); i++){
                Option option = question.getOptions().get(i);
                RadioButton radioButton = ((RadioButton)optionRadioGroup.getChildAt(i));
                radioButton.setText(option.getOptionValue());
                radioButton.setTag(option.getOptionId());
                if(option.isCorrect())
                    optionRadioGroup.setTag(option.getOptionId());
            }
            // show the options
            optionRadioGroup.setVisibility(View.VISIBLE);
            //hide the fib
            editTextFIB.setVisibility(View.GONE);
        }
        // check if the optional image uri is present
        if(question.getImgUri() != null){
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

        }
        else{
            questionImageView.setVisibility(View.GONE);
        }
    }

    private void startTimer(){
        // 10 minutes = 1000 * 60 * 10 milliseconds
        // check if the timer had already started by Fetching remaining seconds from firebase RTD for that user
        // if yes, start the countdown timer with those milli seconds
        //if no start the timer with initial  value 10 minutes and System.currentTimeMillis. persist the states in RDB
        //if yes, start the timer with the remaining seconds and persist the data base
    }
}
