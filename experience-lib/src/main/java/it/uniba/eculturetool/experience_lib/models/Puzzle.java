package it.uniba.eculturetool.experience_lib.models;

import android.graphics.Bitmap;

public class Puzzle extends Experience {
    private transient Bitmap image;
    private int gridDimension;
    private boolean rotationEnabled;

    public Puzzle(String id, Difficulty difficulty, int points) {
        super(id, difficulty, points);
    }

    public Puzzle(Difficulty difficulty, int points) {
        super(difficulty, points);
    }

    public Puzzle() {
        super();
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public Bitmap getImage() {
        return image;
    }

    public int getGridDimension() {
        return gridDimension;
    }

    public void setGridDimension(int gridDimension) {
        this.gridDimension = gridDimension;
    }

    public boolean isRotationEnabled() {
        return rotationEnabled;
    }

    public void setRotationEnabled(boolean rotationEnabled) {
        this.rotationEnabled = rotationEnabled;
    }
}
