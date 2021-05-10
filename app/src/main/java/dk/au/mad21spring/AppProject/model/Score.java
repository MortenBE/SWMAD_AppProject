package dk.au.mad21spring.AppProject.model;
import com.google.firebase.firestore.FirebaseFirestore;

public class Score {
        public String name;
        private int score;
        private int id;

        public Score() {

        }

        public Score(String name, int score) {
            this.name = name;
            this.score = score;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public int getScore() { return score; }
        public void setScore(int score) { this.score = score; }

}

