package dk.au.mad21spring.AppProject.database;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

import dk.au.mad21spring.AppProject.model.Quiz;
import dk.au.mad21spring.AppProject.model.Score;

public class Repository {

    Firestore instance;


    public Repository() {
        instance = new Firestore();
    }

    public MutableLiveData<List<Score>> getScoresByQuizId(String quizId){
        return instance.getScoresByQuizId(quizId);
    }



    public void addNewScore(Score score) {
        instance.addScoreToFirestore(score);
    }

    public void addNewQuiz(Quiz quiz) {
        instance.addQuizToFirestore(quiz);
    }

    public ArrayList<Score> getScores() {
        return instance.getScoreFromFirestore();
    }

    public CollectionReference getScoresCollectionReference() {
        return instance.getScoresCollectionReference();
    }

    public ArrayList<Quiz> getQuizes() {
        return instance.getQuizesFromFirestore();
    }
}
