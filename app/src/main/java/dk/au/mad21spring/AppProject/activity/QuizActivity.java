package dk.au.mad21spring.AppProject.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import dk.au.mad21spring.AppProject.API.QuizAPI;
import dk.au.mad21spring.AppProject.API.QuizModel;
import dk.au.mad21spring.AppProject.R;

public class QuizActivity extends AppCompatActivity {

    RadioButton quiz01, quiz02, quiz03, quiz04;
    TextView Question;
    QuizAPI quizAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        quizAPI = new QuizAPI(getApplication());
        setupWidgets();

        getQuiz();

    }

    private void setupWidgets() {
        quiz01 = findViewById(R.id.quizButtonAns01);
        quiz02 = findViewById(R.id.quizButtonAns02);
        quiz03 = findViewById(R.id.quizButtonAns03);
        quiz04 = findViewById(R.id.quizButtonAns04);

        Question = findViewById(R.id.questionText);
    }



    private void getQuiz()
    {
        RequestQueue queue = Volley.newRequestQueue(getApplication().getApplicationContext());

        // Make request URL using parameters
        String url ="https://opentdb.com/api.php?amount=10&difficulty=medium&type=multiple";
        // Making GET request for weather model request
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    // Deserializing result
                    Gson gson = new Gson();
                    QuizModel quiz = gson.fromJson(response, QuizModel.class);

                    Question.setText(quiz.getResults().get(0).getQuestion());
                    quiz01.setText(quiz.getResults().get(0).getCorrectAnswer());
                    quiz02.setText(quiz.getResults().get(0).getIncorrectAnswers().get(0));
                    quiz03.setText(quiz.getResults().get(0).getIncorrectAnswers().get(1));
                    quiz04.setText(quiz.getResults().get(0).getIncorrectAnswers().get(2));

                    Toast.makeText(getApplication().getApplicationContext(), "quiz fetched" + quiz.getResults().get(0).getCategory(),  Toast.LENGTH_SHORT).show();
                },
                error -> Toast.makeText(getApplication().getApplicationContext(), "Error while fetching quiz" + error.getMessage(),  Toast.LENGTH_SHORT).show());
        queue.add(stringRequest);
    }

}