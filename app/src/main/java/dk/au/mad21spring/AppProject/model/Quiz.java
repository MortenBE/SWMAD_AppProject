package dk.au.mad21spring.AppProject.model;

import com.google.firebase.firestore.DocumentId;

public class Quiz {
    @DocumentId
    private String documentId;
    private String difficulity;
    private String category;
    private double latitude;
    private double longitude;

    public String getDocumentId(){
        return this.documentId;
    }
    public void setDifficulity(String difficulity) {

        this.difficulity = difficulity;
    }
    public String getDifficulity() {
        return difficulity;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public double getLatitude() {
        return latitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public double getLongitude() {
        return longitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
