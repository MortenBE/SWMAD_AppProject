package dk.au.mad21spring.AppProject.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import dk.au.mad21spring.AppProject.API.QuizAPI;
import dk.au.mad21spring.AppProject.API.QuizModel;
import dk.au.mad21spring.AppProject.R;
import org.apache.commons.text.StringEscapeUtils;
import dk.au.mad21spring.AppProject.model.Score;
import dk.au.mad21spring.AppProject.viewmodel.QuizViewModel;

public class QuizActivity extends AppCompatActivity {
    //ViewModels
    QuizViewModel quizViewModel;

    RadioButton quiz01, quiz02, quiz03, quiz04;
    RadioGroup radioGroup;
    TextView Question, scoreText;
    QuizAPI quizAPI;
    Button submitBtn;
    EditText nameInput;

    private int questionCounter, questionCountTotal;
    private QuizModel quiz;
    private int score = 0;
    private String name;
    private String difficultly;
    private String category;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        quizViewModel = new ViewModelProvider(this).get(QuizViewModel.class);
        getQuizSettings();
        quizAPI = new QuizAPI(getApplication());
        setupWidgets();
        setupListener();
        hideUI();
        getQuiz();
    }

    private void getQuizSettings() {

        Intent intent = getIntent();
        difficultly = intent.getStringExtra("difficultly");
        category = intent.getStringExtra("category");
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

        String[] questionsArr = new String[4];

        quiz01.setTextColor(Color.BLACK);
        quiz02.setTextColor(Color.BLACK);
        quiz03.setTextColor(Color.BLACK);
        quiz04.setTextColor(Color.BLACK);


        questionsArr[0] = StringEscapeUtils.unescapeHtml4(quiz.getResults().get(questionCounter).getCorrectAnswer());
        questionsArr[1] = StringEscapeUtils.unescapeHtml4(quiz.getResults().get(questionCounter).getIncorrectAnswers().get(0));
        questionsArr[2] = StringEscapeUtils.unescapeHtml4(quiz.getResults().get(questionCounter).getIncorrectAnswers().get(1));
        questionsArr[3] = StringEscapeUtils.unescapeHtml4(quiz.getResults().get(questionCounter).getIncorrectAnswers().get(2));



        List<String> tempList = Arrays.asList(questionsArr);
        Collections.shuffle(tempList);
        tempList.toArray(questionsArr);


        Question.setText(StringEscapeUtils.unescapeHtml4(quiz.getResults().get(questionCounter).getQuestion()));
        quiz01.setText(questionsArr[0]);
        quiz02.setText(questionsArr[1]);
        quiz03.setText(questionsArr[2]);
        quiz04.setText(questionsArr[3]);
    }//.replace("&amp;", "&").replace("&quot;", "\"").replace("&#039;","'").replace("&ldquo;","\"").replace("&rdquo;","\""));

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

                Toast.makeText(this, "Please write a name, and try agian", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void postScore() {
        Toast.makeText(this, "Submitted score: " + score +  " With Name: " +nameInput.getText(), Toast.LENGTH_SHORT).show();
        Score newScore = new Score(nameInput.getText().toString(), score);
        quizViewModel.addNewScore(newScore);
    }


    private void goToMapActivity() {
        Intent intent = new Intent(QuizActivity.this, MainActivity.class);

        try {
            QuizActivity.this.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Error" + e, Toast.LENGTH_SHORT).show();
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
            scoreText.setText("Quiz done, you got: " + score + " out of 4 questions correct, input name and submit score below");
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
            Toast.makeText(this, "Correct Answer", Toast.LENGTH_SHORT).show();
            score ++;
        }
        else {
            Toast.makeText(this, "Wrong Answer, correct answer was: " + quiz.getResults().get(questionCounter).getCorrectAnswer()+"a", Toast.LENGTH_LONG).show();
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
                    quiz01.setVisibility(View.VISIBLE);
                    quiz02.setVisibility(View.VISIBLE);
                    quiz03.setVisibility(View.VISIBLE);
                    quiz04.setVisibility(View.VISIBLE);
                    Question.setVisibility(View.VISIBLE);

                    // Toast.makeText(getApplication().getApplicationContext(), "quiz fetched" + quiz.getResults().get(0).getCategory(),  Toast.LENGTH_SHORT).show();
                },
                error -> Toast.makeText(getApplication().getApplicationContext(), "Error while fetching quiz" + error.getMessage(),  Toast.LENGTH_SHORT).show());
        queue.add(stringRequest);
    }
}