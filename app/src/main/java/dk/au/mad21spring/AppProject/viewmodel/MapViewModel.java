package dk.au.mad21spring.AppProject.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import android.location.Location;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import dk.au.mad21spring.AppProject.database.Repository;
import dk.au.mad21spring.AppProject.model.Quiz;
import dk.au.mad21spring.AppProject.model.Score;

public class MapViewModel extends AndroidViewModel {

    private Repository repository;
    private static final String TAG = "MapViewModel";

    public MapViewModel(@NonNull Application application) {
        super(application);
        repository = Repository.getInstance();
    }

    public MutableLiveData<Location> getCurrentLocation()
    {
        return repository.getCurrentLocation();
    }

    public void addQuiz(Quiz quiz) {
        repository.addNewQuiz(quiz);
    }

    public MutableLiveData<List<Quiz>> getQuizzes(){
        return repository.getQuizzes();
    }
}

