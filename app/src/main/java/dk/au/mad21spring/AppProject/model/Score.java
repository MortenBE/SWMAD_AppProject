package dk.au.mad21spring.AppProject.model;
import com.google.firebase.firestore.DocumentId;

public class Score {
        @DocumentId
        private String documentId;
        private String quizId;
        public String quizzersName;
        private int score;

        public Score() {

        }
        public Score(String quizzersName, int score, String quizId) {
            this.quizzersName = quizzersName;
            this.score = score;
            this.quizId = quizId;
        }
        public String getDocumentId() {
                return documentId;
        }
        public String getQuizId() {
                return quizId;
        }
        public void setQuizId(String quizId) {
                this.quizId = quizId;
        }
        public String getQuizzersName() {
                return quizzersName;
        }
        public void setQuizzersName(String quizzersName) {
                this.quizzersName = quizzersName;
        }
        public int getScore() {
                return score;
        }
        public void setScore(int score) {
                this.score = score;
        }
}

