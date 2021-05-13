package dk.au.mad21spring.AppProject.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import dk.au.mad21spring.AppProject.API.QuizAPI;
import dk.au.mad21spring.AppProject.API.QuizModel;
import dk.au.mad21spring.AppProject.R;
import org.apache.commons.text.StringEscapeUtils;

import dk.au.mad21spring.AppProject.model.Quiz;
import dk.au.mad21spring.AppProject.model.Score;
import dk.au.mad21spring.AppProject.viewmodel.QuizViewModel;

public class QuizActivity extends AppCompatActivity {
    //ViewModels
    QuizViewModel quizViewModel;

    // Widgets
    RadioButton quiz01, quiz02, quiz03, quiz04;
    RadioGroup radioGroup;
    TextView Question, scoreText;
    QuizAPI quizAPI;
    Button submitBtn;
    EditText nameInput;

    // Fields
    String[] questionsArr;
    private int questionCounter, questionCountTotal;
    private QuizModel quiz;
    private int score = 0;
    private String difficultly, category, quizId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        quizViewModel = new ViewModelProvider(this).get(QuizViewModel.class);

        setupWidgets();
        setupListener();
        hideUI();

        if(savedInstanceState != null)
        {
            Gson gson = new Gson();
            quiz = gson.fromJson( savedInstanceState.getString("QuizModel"),QuizModel.class);
            Toast.makeText(this, quiz.getResults().get(1).getQuestion(), Toast.LENGTH_SHORT).show();
            questionCounter = savedInstanceState.getInt("questionCounter");
            quizId = savedInstanceState.getString("QuizId");
            score = savedInstanceState.getInt("score");
            setQuestion(questionCounter);
            showQuizUI();
        }

        else
        {
            getQuizSettings();
        }
    }

    private void showQuizUI() {
        quiz01.setVisibility(View.VISIBLE);
        quiz02.setVisibility(View.VISIBLE);
        quiz03.setVisibility(View.VISIBLE);
        quiz04.setVisibility(View.VISIBLE);
        Question.setVisibility(View.VISIBLE);
    }

    private void getQuizSettings() {

        if(getIntent().hasExtra("quizId"))
        {
            quizViewModel.GetQuiz(getIntent().getStringExtra("quizId")).observe(this, myQuiz -> {
                quizId = myQuiz.getDocumentId();
                difficultly = myQuiz.getDifficulity();
                category = myQuiz .getCategory();

                //TODO: all this should be moved back. Only moved it here because i have problems with firebase async
                quizAPI = new QuizAPI(getApplication());
                getQuiz();
        });

            Toast.makeText(this, "" + quizId, Toast.LENGTH_SHORT).show();
        }

    }


    private void hideUI() {

        scoreText.setVisibility(View.GONE);
        submitBtn.setVisibility(View.GONE);
        nameInput.setVisibility(View.GONE);
        quiz01.setVisibility(View.GONE);
        quiz02.setVisibility(View.GONE);
        quiz03.setVisibility(View.GONE);
        quiz04.setVisibility(View.GONE);
        Question.setVisibility(View.GONE);
    }

    private void setQuestion(int questionCounter) {

        questionsArr = new String[4];

        questionsArr[0] = StringEscapeUtils.unescapeHtml4(quiz.getResults().get(questionCounter).getCorrectAnswer());
        questionsArr[1] = StringEscapeUtils.unescapeHtml4(quiz.getResults().get(questionCounter).getIncorrectAnswers().get(0));
        questionsArr[2] = StringEscapeUtils.unescapeHtml4(quiz.getResults().get(questionCounter).getIncorrectAnswers().get(1));
        questionsArr[3] = StringEscapeUtils.unescapeHtml4(quiz.getResults().get(questionCounter).getIncorrectAnswers().get(2));

        List<String> tempList = Arrays.asList(questionsArr);
        Collections.shuffle(tempList);
        tempList.toArray(questionsArr);

        setUI();
    }

    private void setUI() {
        Question.setText(StringEscapeUtils.unescapeHtml4(quiz.getResults().get(questionCounter).getQuestion()));
        quiz01.setText(questionsArr[0]);
        quiz02.setText(questionsArr[1]);
        quiz03.setText(questionsArr[2]);
        quiz04.setText(questionsArr[3]);
    }

    private void setupListener() {
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            checkResult(checkedId);
        });
        submitBtn.setOnClickListener(v -> {
            if (!nameInput.getText().toString().isEmpty())
            {
            postScore();
            goToMapActivity();
            }
            else
            {
                Toast.makeText(this, getResources().getString(R.string.request_name_and_try_again), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void postScore() {
        Toast.makeText(this, getResources().getString(R.string.submittet_score) + score +  getResources().getString(R.string.with_name) +nameInput.getText(), Toast.LENGTH_SHORT).show();
        Score newScore = new Score(nameInput.getText().toString(), score, quizId);
        quizViewModel.addNewScore(newScore);


    }


    private void goToMapActivity() {
        Intent intent = new Intent(QuizActivity.this, MainActivity.class);

        try {
            QuizActivity.this.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, getResources().getString(R.string.catch_error) + e, Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("SetTextI18n")
    private void nextQuestion() {
        if (questionCounter < questionCountTotal)
        {
            questionCounter ++;
            setQuestion(questionCounter);
        }
        else{
            scoreText.setText(getResources().getString(R.string.quiz_score1) + score + getResources().getString(R.string.quiz_score2));
            scoreText.setVisibility(View.VISIBLE);
            submitBtn.setVisibility(View.VISIBLE);
            nameInput.setVisibility(View.VISIBLE);
            quiz01.setVisibility(View.GONE);
            quiz02.setVisibility(View.GONE);
            quiz03.setVisibility(View.GONE);
            quiz04.setVisibility(View.GONE);
            Question.setVisibility(View.GONE);
        }
    }

    private void checkResult(int Id) {
        RadioButton checkedButton = findViewById(Id);

        if (checkedButton.getText().equals(quiz.getResults().get(questionCounter).getCorrectAnswer()))
        {
            Toast.makeText(this, getResources().getString(R.string.correct_answer), Toast.LENGTH_SHORT).show();
            score ++;
        }
        else {
            Toast.makeText(this, getResources().getString(R.string.wrong_answer) + quiz.getResults().get(questionCounter).getCorrectAnswer()+"a", Toast.LENGTH_LONG).show();
        }
        checkedButton.setChecked(false);
        nextQuestion();
    }

    private void setupWidgets() {
        scoreText = findViewById(R.id.scoreText);
        quiz01 = findViewById(R.id.quizButtonAns01);
        quiz02 = findViewById(R.id.quizButtonAns02);
        quiz03 = findViewById(R.id.quizButtonAns03);
        quiz04 = findViewById(R.id.quizButtonAns04);
        radioGroup = findViewById(R.id.RadioGroupQuiz);
        Question = findViewById(R.id.questionText);
        submitBtn = findViewById(R.id.submitScoreBtn);
        nameInput = findViewById(R.id.NameScoreQuiz);


        questionCounter = 0;
        questionCountTotal = 3;
    }



    private void getQuiz()
    {

        RequestQueue queue = Volley.newRequestQueue(getApplication().getApplicationContext());

        // Make request URL using parameters
        String amountOfQuestions = "amount=4";


        String url ="https://opentdb.com/api.php?"+ amountOfQuestions + "&category=" + category + "&difficulty=" + difficultly + "&type=multiple";
        // Making GET request for weather model request
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    // Deserializing result
                    Gson gson = new Gson();
                    quiz = gson.fromJson(response, QuizModel.class);
                    setQuestion(questionCounter);
                    showQuizUI();

                    // Toast.makeText(getApplication().getApplicationContext(), "quiz fetched" + quiz.getResults().get(0).getCategory(),  Toast.LENGTH_SHORT).show();
                },
                error -> Toast.makeText(getApplication().getApplicationContext(), getResources().getString(R.string.error_while_fetch_api) + error.getMessage(),  Toast.LENGTH_SHORT).show());
        queue.add(stringRequest);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Gson gson = new Gson();

        outState.putString("QuizModel",gson.toJson(quiz));
        outState.putInt("questionCounter", questionCounter);
        outState.putInt("score", score);
        outState.putString("QuizId", quizId);
    }
}