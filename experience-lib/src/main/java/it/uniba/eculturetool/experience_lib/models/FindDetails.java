package it.uniba.eculturetool.experience_lib.models;

import android.graphics.Bitmap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FindDetails extends Experience {
    private transient Bitmap image;
    private List<Coordinate> coordinates;
    private String message;

    public FindDetails(String id, Difficulty difficulty, int points) {
        super(id, difficulty, points);
    }

    public FindDetails() {
        super();
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public Bitmap getImage() {
        return image;
    }

    public List<Coordinate> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Coordinate> coordinates) {
        this.coordinates = coordinates;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
