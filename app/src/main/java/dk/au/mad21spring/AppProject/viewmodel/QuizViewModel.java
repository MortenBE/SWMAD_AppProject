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

    private Repository repository;
    private static final String TAG = "QuizViewModel";

    public QuizViewModel(@NonNull Application application) {
        super(application);
        repository = Repository.getInstance();
    }

    public void addNewScore(Score score) {
        repository.addNewScore(score);
    }

    public MutableLiveData<Quiz> GetQuiz(String quizId){
        return repository.GetQuizById(quizId);
    }
}
