package dk.au.mad21spring.AppProject.API;

import android.app.Application;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

public class QuizAPI {

        private final Application app;

        public QuizAPI(Application application)
        {
            app = application;
        }

        public void getQuiz()
        {
            RequestQueue queue = Volley.newRequestQueue(app.getApplicationContext());

            // Make request URL using parameters
            String url ="https://opentdb.com/api.php?amount=10&difficulty=medium&type=multiple";
            // Making GET request for weather model request
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    response -> {
                        // Deserializing result
                        Gson gson = new Gson();
                        QuizModel quiz = gson.fromJson(response, QuizModel.class);
                        Toast.makeText(app.getApplicationContext(), "quiz fetched" + quiz.getResults().get(0).getCategory(),  Toast.LENGTH_SHORT).show();
                    },
                    error -> Toast.makeText(app.getApplicationContext(), "Error while fetching quiz" + error.getMessage(),  Toast.LENGTH_SHORT).show());
            queue.add(stringRequest);
        }
}
