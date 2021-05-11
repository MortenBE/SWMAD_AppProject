package dk.au.mad21spring.AppProject.database;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;
import dk.au.mad21spring.AppProject.model.Quiz;
import dk.au.mad21spring.AppProject.model.Score;


public class Firestore {
    private static final String TAG = "Firestore";
    private final FirebaseFirestore db;

    public Firestore() {
        db = FirebaseFirestore.getInstance();
    }

    //based on https://www.geeksforgeeks.org/how-to-update-data-in-firebase-firestore-in-android/
    public void addScoreToFirestore(Score score) {
        //collection reference for database
        CollectionReference dbQuizResult = db.collection("Scores");

        dbQuizResult.add(score).addOnSuccessListener(documentReference -> {
        }).addOnFailureListener(e -> {
        });
    }

    public void addQuizToFirestore(Quiz quiz) {
        //collection reference for database
        CollectionReference dbQuizResult = db.collection("Quizes");

        dbQuizResult.add(quiz).addOnSuccessListener(documentReference -> {
        }).addOnFailureListener(e -> {
        });
    }

    public ArrayList<Score> getScoreFromFirestore() {
        ArrayList<Score> arrayList = new ArrayList<>();
        db.collection("Scores").get().addOnSuccessListener(queryDocumentSnapshots -> {
            //after getting the data we are calling on success
            //checking also whether the document is empty or not
            if(!queryDocumentSnapshots.isEmpty()) {
                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot d : list) {
                    Score newScore = d.toObject(Score.class);
                    arrayList.add(newScore);
                    Log.d(TAG, "" + newScore.getQuizzersName());
                }
                Log.d(TAG, "1: " + arrayList.size());
            } else {
                Log.d(TAG, "No data found in database");
            }
        }).addOnFailureListener(e -> Log.d(TAG, "A Failure has occurred with Firestore"));
        Log.d(TAG, "2: " + arrayList.size());
        return arrayList;
    }

    public ArrayList<Quiz> getQuizesFromFirestore() {
        ArrayList<Quiz> arrayList = new ArrayList<>();
        db.collection("Quizes").get().addOnSuccessListener(queryDocumentSnapshots -> {
            //after getting the data we are calling on success
            //checking also whether the document is empty or not
            if(!queryDocumentSnapshots.isEmpty()) {
                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot d : list) {
                    Quiz quiz = d.toObject(Quiz.class);
                    arrayList.add(quiz);
                }
                Log.d(TAG, "Quizes: " + arrayList.size());
            } else {
                Log.d(TAG, "No data found in database");
            }
        }).addOnFailureListener(e -> Log.d(TAG, "A Failure has occurred with Firestore"));
        Log.d(TAG, "2: " + arrayList.size());
        return arrayList;
    }



    CollectionReference getScoresCollectionReference() {
        return db.collection("Scores");


    }





}
