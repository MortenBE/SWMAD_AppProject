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

    private List<Score> highscores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        //Init ViewModel
        scoreViewModel = new ViewModelProvider(this).get(ScoreViewModel.class);

        if(getIntent().hasExtra("quizId"))
        {
            //Get quizId
            String quizId = getIntent().getStringExtra("quizId");

            //Setup observer for quiz highscores
            scoreViewModel.getScores(quizId).observe(this, scores -> {
                highscores = scores;

                //Do something with the scores
                updateUI();
            });
        }
    }

    private void initWidgets(){

    }

    private void updateUI() {
        for (int j = 0; j < highscores.size(); j ++)
        {
            Toast.makeText(this, (j + 1) + ": Player: " + highscores.get(j).quizzersName + ", Score: " + highscores.get(j).getScore(), Toast.LENGTH_SHORT).show();
        }
    }
}