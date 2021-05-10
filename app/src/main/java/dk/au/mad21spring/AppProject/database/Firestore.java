package dk.au.mad21spring.AppProject.database;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import dk.au.mad21spring.AppProject.activity.QuizActivity;
import dk.au.mad21spring.AppProject.model.Score;

public class Firestore {
    private FirebaseFirestore db;

    public Firestore() {
        db = FirebaseFirestore.getInstance();
    }

    //based on https://www.geeksforgeeks.org/how-to-update-data-in-firebase-firestore-in-android/
    public void addScoreToFirestore(Score score) {
        //collection reference for database
        CollectionReference dbQuizResult = db.collection("Scores");

        dbQuizResult.add(score).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                //Toast.makeText(QuizActivity.this, "Results has been added", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Toast.makeText(QuizActivity.this, "Failed to add Quiz Results", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public Score getScoreFromFirestore() {
        return null;
    }

}
