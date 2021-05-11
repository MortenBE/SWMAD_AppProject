package dk.au.mad21spring.AppProject.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import dk.au.mad21spring.AppProject.R;
import dk.au.mad21spring.AppProject.model.Score;
import dk.au.mad21spring.AppProject.viewmodel.ScoreViewModel;

public class ScoreActivity extends AppCompatActivity {
    private static final String TAG = "ScoreActivity";

    ScoreViewModel scoreViewModel;

    private List<Score> highscores = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        /*
        scoreViewModel = new ViewModelProvider(this).get(ScoreViewModel.class);
        scoreViewModel.getAllScores().observe(this, scores -> {
            highscores = scores;
            updateUI();
        });
         */

        getQuiz();
    }

    private void getQuiz() {
        if(getIntent().hasExtra("quizId"))
        {
            String quizId = getIntent().getStringExtra("quizId");
            Toast.makeText(this, "" + quizId, Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUI() {
        for(int i = 0; i < highscores.size(); i++)
        {
            Toast.makeText(this, "" + highscores.get(i).quizzersName, Toast.LENGTH_SHORT).show();
        }
    }
}