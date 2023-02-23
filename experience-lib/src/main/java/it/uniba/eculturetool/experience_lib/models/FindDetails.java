package it.uniba.eculturetool.experience_lib.models;

import android.graphics.Bitmap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FindDetails extends Experience {
    private transient Bitmap image;
    private List<Coordinate> coordinates;
    private Map<String, String> messages = new HashMap<>();

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

    public Map<String, String> getMessages() {
        return messages;
    }

    public void setMessages(Map<String, String> messages) {
        this.messages = messages;
    }
}
