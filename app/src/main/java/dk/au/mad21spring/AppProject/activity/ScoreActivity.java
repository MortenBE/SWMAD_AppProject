package dk.au.mad21spring.AppProject.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import dk.au.mad21spring.AppProject.R;
import dk.au.mad21spring.AppProject.adapters.ScoreAdapter;
import dk.au.mad21spring.AppProject.model.Score;
import dk.au.mad21spring.AppProject.viewmodel.ScoreViewModel;

public class ScoreActivity extends AppCompatActivity {
    private static final String TAG = "ScoreActivity";

    ScoreViewModel scoreViewModel;
    private String quizId;

    ScoreAdapter adapter;
    RecyclerView recyclerView;
    //Button viewScoreBtn;

    private List<Score> highscores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        //Get quizId
        if(getIntent().hasExtra("quizId")) {
            quizId = getIntent().getStringExtra("quizId");
        } else if(savedInstanceState != null) {
            quizId = savedInstanceState.getString("quizId");
        }

        //Init adapter - Mich//
        adapter = new ScoreAdapter(this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        //===============================//

        //Init ViewModel
        scoreViewModel = new ViewModelProvider(this).get(ScoreViewModel.class);
        //Setup observer for quiz highscores
        scoreViewModel.getScores(quizId).observe(this, scores -> {
            adapter.setScores(scores);
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("quizId", quizId);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }


}