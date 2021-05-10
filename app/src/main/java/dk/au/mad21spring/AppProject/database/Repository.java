package dk.au.mad21spring.AppProject.database;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import dk.au.mad21spring.AppProject.model.Score;

public class Repository {

    Firestore instance;

    public Repository() {
        instance = new Firestore();

    }

    public void addNewScore(Score score) {
        instance.addScoreToFirestore(score);
    }


    public ArrayList<Score> getScores() {
        return instance.getScoreFromFirestore();
    }

    public CollectionReference getScoresCollectionReference() {
        return instance.getScoresCollectionReference();
    }
}
