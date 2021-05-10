package dk.au.mad21spring.AppProject.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.ArrayList;

import dk.au.mad21spring.AppProject.database.Repository;
import dk.au.mad21spring.AppProject.model.Score;

public class ScoreViewModel extends AndroidViewModel {
    private Repository instance;

    public ScoreViewModel(@NonNull Application application) {
        super(application);
        instance = new Repository();
    }

    public ArrayList<Score> getAllScores() {
        return instance.getScores();
    }
}
