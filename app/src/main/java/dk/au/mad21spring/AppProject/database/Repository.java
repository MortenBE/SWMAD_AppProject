package dk.au.mad21spring.AppProject.database;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;
import android.location.Location;

import dk.au.mad21spring.AppProject.model.Quiz;
import dk.au.mad21spring.AppProject.model.Score;

public class Repository {

    private Firestore firestore;
    private static Repository instance;
    private MutableLiveData<Location> currentLocation;

    public MutableLiveData<Location> getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation.setValue(currentLocation);
    }

    private Repository() {

        firestore = new Firestore();
        currentLocation = new MutableLiveData<>();
    }

    // if instance is null create new instance of Repository
    public static Repository getInstance(){
        if(instance == null){
            instance = new Repository();
        }
        return instance;
    }

    //Firestore database
    public MutableLiveData<List<Score>> getScoresByQuizId(String quizId){
        return firestore.getScoresByQuizId(quizId);
    }

    public MutableLiveData<Quiz> GetQuizById(String quizId){
        return firestore.GetQuizbyId(quizId);
    }

    public MutableLiveData<List<Quiz>> getQuizzes(){
        return firestore.getQuizzes();
    }

    public void addNewScore(Score score) {
        firestore.addScoreToFirestore(score);
    }

    public void addNewQuiz(Quiz quiz) {
        firestore.addQuizToFirestore(quiz);
    }
}
