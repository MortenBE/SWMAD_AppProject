package dk.au.mad21spring.AppProject.database;

import com.google.firebase.firestore.FirebaseFirestore;

import dk.au.mad21spring.AppProject.model.Score;

public class Repository {

    Firestore instance;

    public Repository() {
        instance = new Firestore();

    }

    public void addNewScore(Score score) {
        instance.addScoreToFirestore(score);
    }


    public Score getScores() {
        return null; //TO DO: Edit

    }
}
