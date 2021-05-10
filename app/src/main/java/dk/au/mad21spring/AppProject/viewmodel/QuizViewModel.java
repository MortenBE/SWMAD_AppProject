package dk.au.mad21spring.AppProject.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import dk.au.mad21spring.AppProject.database.Repository;
import dk.au.mad21spring.AppProject.model.Score;

public class QuizViewModel extends AndroidViewModel {
    private Repository instance;



    public QuizViewModel(@NonNull Application application) {
        super(application);
        instance = new Repository();
    }

    public void addNewScore(Score score) {
        instance.addNewScore(score);
    }
}
