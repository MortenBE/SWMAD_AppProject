package dk.au.mad21spring.AppProject.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

public class QuizActivity extends AppCompatActivity {

    RadioButton quiz01, quiz02, quiz03, quiz04;
    RadioGroup radioGroup;
    TextView Question;
    QuizAPI quizAPI;
    String correctAns;
    private int questionCounter, questionCountTotal;
    private QuizModel quiz;
    private int score;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        quizAPI = new QuizAPI(getApplication());
        setupWidgets();
        setupListener();

        getQuiz();
        score = 0;


    }

    private void setQuestion(int questionCounter) {

        String[] questionsArr = new String[4];

        questionsArr[0] = quiz.getResults().get(questionCounter).getCorrectAnswer();
        questionsArr[1] = quiz.getResults().get(questionCounter).getIncorrectAnswers().get(0);
        questionsArr[2] = quiz.getResults().get(questionCounter).getIncorrectAnswers().get(1);
        questionsArr[3] = quiz.getResults().get(questionCounter).getIncorrectAnswers().get(2);

        List<String> tempList = Arrays.asList(questionsArr);
        Collections.shuffle(tempList);
        tempList.toArray(questionsArr);


        Question.setText(quiz.getResults().get(questionCounter).getQuestion().replace("&amp;", "&").replace("&quot;", "\""));
        quiz01.setText(questionsArr[0]);
        quiz02.setText(questionsArr[1]);
        quiz03.setText(questionsArr[2]);
        quiz04.setText(questionsArr[3]);
    }

    private void setupListener() {
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            checkResult(checkedId);

            nextQuestion();
        });
    }

    private void nextQuestion() {
        if (questionCounter < questionCountTotal)
        {

            setQuestion(questionCounter);
            questionCounter ++;
        }
        else{
            Toast.makeText(this, "Quiz done, you got: " + score + " out of 4 questions correct", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkResult(int Id) {
        RadioButton checkedButton = findViewById(Id);

        if ( checkedButton.getText() == quiz.getResults().get(questionCounter).getCorrectAnswer())
        {
            Toast.makeText(this, "Correct Answer", Toast.LENGTH_SHORT).show();
            score ++;
        }

    }


    private void setupWidgets() {
        quiz01 = findViewById(R.id.quizButtonAns01);
        quiz02 = findViewById(R.id.quizButtonAns02);
        quiz03 = findViewById(R.id.quizButtonAns03);
        quiz04 = findViewById(R.id.quizButtonAns04);
        radioGroup = findViewById(R.id.RadioGroupQuiz);
        Question = findViewById(R.id.questionText);

        questionCounter = 0;
        questionCountTotal = 3;
    }



    private void getQuiz()
    {

        RequestQueue queue = Volley.newRequestQueue(getApplication().getApplicationContext());

        // Make request URL using parameters
        String url ="https://opentdb.com/api.php?amount=4&difficulty=medium&type=multiple";
        // Making GET request for weather model request
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    // Deserializing result
                    Gson gson = new Gson();
                    quiz = gson.fromJson(response, QuizModel.class);
                    setQuestion(questionCounter);
                   // Toast.makeText(getApplication().getApplicationContext(), "quiz fetched" + quiz.getResults().get(0).getCategory(),  Toast.LENGTH_SHORT).show();
                },
                error -> Toast.makeText(getApplication().getApplicationContext(), "Error while fetching quiz" + error.getMessage(),  Toast.LENGTH_SHORT).show());
        queue.add(stringRequest);
    }

}