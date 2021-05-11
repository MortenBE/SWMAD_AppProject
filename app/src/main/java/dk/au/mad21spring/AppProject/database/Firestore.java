package dk.au.mad21spring.AppProject.database;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

import dk.au.mad21spring.AppProject.activity.MainActivity;
import dk.au.mad21spring.AppProject.activity.QuizActivity;
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
            //Toast.makeText(GetContext, "Results has been added", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            //Toast.makeText(QuizActivity.this, "Failed to add Quiz Results", Toast.LENGTH_SHORT).show();
        });
    }

    public void addQuizToFirestore(Quiz quiz) {
        //collection reference for database
        CollectionReference dbQuizResult = db.collection("Quizes");

        dbQuizResult.add(quiz).addOnSuccessListener(documentReference -> {
            //Toast.makeText(GetContext, "Results has been added", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            //Toast.makeText(QuizActivity.this, "Failed to add Quiz Results", Toast.LENGTH_SHORT).show();
        });
    }

    public ArrayList<Score> getScoreFromFirestore() {
        ArrayList<Score> arrayList = new ArrayList<>();
        //LiveData<Score> scoreLiveData = new MutableLiveData<>();
        db.collection("Scores").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        //after getting the data we are calling on success
                        //checking also whether the document is empty or not
                        if(!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot d : list) {
                                Score newScore = d.toObject(Score.class);
                                arrayList.add(newScore);
                                Log.d(TAG, "" + newScore.getName());
                            }
                            Log.d(TAG, "1: " + arrayList.size());
                        } else {
                            Log.d(TAG, "No data found in database");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "A Failure has occurred with Firestore");
            }
        });
        Log.d(TAG, "2: " + arrayList.size());
        return arrayList;
    }

    public ArrayList<Quiz> getQuizesFromFirestore() {
        ArrayList<Quiz> arrayList = new ArrayList<>();
        //LiveData<Score> scoreLiveData = new MutableLiveData<>();
        db.collection("Quizes").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
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
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "A Failure has occurred with Firestore");
            }
        });
        Log.d(TAG, "2: " + arrayList.size());
        return arrayList;
    }



    CollectionReference getScoresCollectionReference() {
        return db.collection("Scores");


    }





}
