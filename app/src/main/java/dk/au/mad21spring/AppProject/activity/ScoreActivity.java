package dk.au.mad21spring.AppProject.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
    ListView highscores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        scoreViewModel = new ViewModelProvider(this).get(ScoreViewModel.class);

        ArrayList<Score> listOfScores = scoreViewModel.getAllScores();
        if(listOfScores == null) {
            Toast.makeText(this, "There is nothing here", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "" + listOfScores.size(), Toast.LENGTH_SHORT).show();
        }

    }

}