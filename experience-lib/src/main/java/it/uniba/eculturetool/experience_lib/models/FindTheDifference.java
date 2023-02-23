package it.uniba.eculturetool.experience_lib.models;

import android.graphics.Bitmap;

import java.util.List;

public class FindTheDifference extends Experience {
    private transient Bitmap originalImage;
    private transient Bitmap differentImage;
    private List<Coordinate> differencesCoordinates;

    public FindTheDifference(String id, Difficulty difficulty, int points) {
        super(id, difficulty, points);
    }

    public FindTheDifference(Difficulty difficulty, int points) {
        super(difficulty, points);
    }

    public FindTheDifference() {
        super();
    }

    public void setOriginalImage(Bitmap originalImage) {
        this.originalImage = originalImage;
    }

    public Bitmap getOriginalImage() {
        return originalImage;
    }

    public Bitmap getDifferentImage() {
        return differentImage;
    }

    public List<Coordinate> getDifferencesCoordinates() {
        return differencesCoordinates;
    }

    public void setDifferencesCoordinates(List<Coordinate> differencesCoordinates) {
        this.differencesCoordinates = differencesCoordinates;
    }

    public void setDifferentImage(Bitmap differentImage) {
        this.differentImage = differentImage;
    }
}
