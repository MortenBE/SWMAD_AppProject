package dk.au.mad21spring.AppProject.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import dk.au.mad21spring.AppProject.R;
import dk.au.mad21spring.AppProject.database.Repository;
import dk.au.mad21spring.AppProject.model.Score;
import dk.au.mad21spring.AppProject.viewmodel.QuizViewModel;
import dk.au.mad21spring.AppProject.viewmodel.ScoreViewModel;

public class ScoreActivity extends AppCompatActivity {
    private static final String TAG = "ScoreActivity";

    ScoreViewModel scoreViewModel;

    private List<Score> highscores = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        scoreViewModel = new ViewModelProvider(this).get(ScoreViewModel.class);
        scoreViewModel.getAllScores().observe(this, new Observer<List<Score>>(){
            @Override
            public void onChanged(List<Score> scores) {
                highscores = scores;
                updateUI();
            }
        });
    }
    private void updateUI() {
        for(int i = 0; i < highscores.size(); i++)
        {
            Toast.makeText(this, "" + highscores.get(i).name, Toast.LENGTH_SHORT).show();
        }
    }

}