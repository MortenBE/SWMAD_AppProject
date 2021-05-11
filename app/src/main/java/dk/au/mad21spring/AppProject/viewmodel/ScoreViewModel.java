package dk.au.mad21spring.AppProject.viewmodel;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import java.util.Observable;

import dk.au.mad21spring.AppProject.database.Firestore;
import dk.au.mad21spring.AppProject.database.Repository;
import dk.au.mad21spring.AppProject.model.Score;

public class ScoreViewModel extends AndroidViewModel {

    private static final String TAG = "ScoreViewModel";
    private FirebaseFirestore db;
    private MutableLiveData<List<Score>> scores = new MutableLiveData<>();

    public ScoreViewModel(@NonNull Application application) {
        super(application);
        db = FirebaseFirestore.getInstance();
    }

    private void ObserveScores(){
        ArrayList<Score> arrayList = new ArrayList<>();
        db.collection("Scores").get().addOnSuccessListener(queryDocumentSnapshots -> {
            if(!queryDocumentSnapshots.isEmpty()) {
                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot d : list) {
                    Score newScore = d.toObject(Score.class);
                    Log.d(TAG, "ID: " + newScore.getDocumentId());
                    arrayList.add(newScore);
                }
                scores.setValue(arrayList);
            } else {
                Log.d(TAG, "No data found in database");
            }
        }).addOnFailureListener(e -> Log.d(TAG, "A Failure has occurred with Firestore"));
    }

    public LiveData<List<Score>> getAllScores(){
        ObserveScores();
        return scores;
    }

}
