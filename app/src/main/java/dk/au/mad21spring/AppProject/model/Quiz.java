package dk.au.mad21spring.AppProject.model;

public class Quiz {

    private String mockString;//TODO:Delete


    private String difficulity;



    private String category;
    private double latitude;
    private double longitude;

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

    public String getMockString() {
        return mockString;
    }

    public void setMockString(String mockString) {
        this.mockString = mockString;
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
