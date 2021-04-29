package dk.au.mad21spring.AppProject.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import dk.au.mad21spring.AppProject.R;

public class MainActivity extends AppCompatActivity {
    Button QuizButton, ScoreButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initWigdet();
        initButtons();
    }



    private void initWigdet() {
        QuizButton = findViewById(R.id.MainQuizButton);
        ScoreButton = findViewById(R.id.MainScoreButton);
    }

    private void initButtons() {
        QuizButton.setOnClickListener(v -> {
            GoToQuizActivity();
        });

        ScoreButton.setOnClickListener(v -> {
            GoToScoreActivity();
        });


    }

    private void GoToScoreActivity() {
        Toast.makeText(this, "Score", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(MainActivity.this, ScoreActivity.class);

        try {
            MainActivity.this.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Error" + e, Toast.LENGTH_SHORT).show();
        }
    }

    private void GoToQuizActivity() {
        Toast.makeText(this, "Quiz", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(MainActivity.this, QuizActivity.class);

        try {
            MainActivity.this.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Error" + e, Toast.LENGTH_SHORT).show();
        }
    }


}