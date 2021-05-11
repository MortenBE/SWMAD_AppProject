package dk.au.mad21spring.AppProject.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

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
    private final Repository instance;
    private final FirebaseFirestore db;
    private final MutableLiveData<List<Quiz>> quizzes = new MutableLiveData<>();
    private static final String TAG = "MapViewModel";

    public MapViewModel(@NonNull Application application) {
        super(application);
        instance = new Repository();
        db = FirebaseFirestore.getInstance();
    }

    public void addQuiz(Quiz quiz) {
        instance.addNewQuiz(quiz);
    }

    private void ObserveQuizzes(){
        ArrayList<Quiz> arrayList = new ArrayList<>();
        db.collection("Quizes").get().addOnSuccessListener(queryDocumentSnapshots -> {
            if(!queryDocumentSnapshots.isEmpty()) {
                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot d : list) {
                    Quiz newQuiz = d.toObject(Quiz.class);
                    arrayList.add(newQuiz);
                }
                quizzes.setValue(arrayList);
            } else {
                Log.d(TAG, "No data found in database");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "A Failure has occurred with Firestore");
            }
        });
    }

    public MutableLiveData<List<Quiz>> getQuizes(){
        ObserveQuizzes();
        return quizzes;
    }
}

