package dk.au.mad21spring.AppProject.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import dk.au.mad21spring.AppProject.database.Repository;
import dk.au.mad21spring.AppProject.model.Quiz;
import dk.au.mad21spring.AppProject.model.Score;

public class QuizViewModel extends AndroidViewModel {
    private final Repository instance;
    private final FirebaseFirestore db; //TODO:Remove
    private MutableLiveData<Quiz> quiz = new MutableLiveData<>();
    private static final String TAG = "QuizViewModel";

    public QuizViewModel(@NonNull Application application) {
        super(application);
        instance = new Repository();
        db = FirebaseFirestore.getInstance();
    }

    public void addNewScore(Score score) {
        instance.addNewScore(score);
    }

    //TODO:Remove insert into repository
    //https://firebase.google.com/docs/firestore/query-data/get-data
    private void GetQuizFromDb(String quizId){
        Quiz newQuiz = new Quiz();
        DocumentReference docRef = db.collection("Quizes").document(quizId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        quiz.setValue(document.toObject(Quiz.class));
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public MutableLiveData<Quiz> GetQuiz(String quizId){
        GetQuizFromDb(quizId);
        return quiz;
    }


}
