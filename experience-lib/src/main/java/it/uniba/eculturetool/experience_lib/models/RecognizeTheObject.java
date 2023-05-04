package it.uniba.eculturetool.experience_lib.models;

import android.graphics.Bitmap;

public class RecognizeTheObject extends Experience {
    private transient Bitmap referenceImage;
    private String uriReferenceImage;
    private String description;
    private String modelName;

    public RecognizeTheObject(String id, Difficulty difficulty, int points) {
        super(id, difficulty, points);
    }

    public RecognizeTheObject(Difficulty difficulty, int points) {
        super(difficulty, points);
    }

    public RecognizeTheObject() {}

    public Bitmap getReferenceImage() {
        return referenceImage;
    }

    public void setReferenceImage(Bitmap referenceImage) {
        this.referenceImage = referenceImage;
    }

    public String getUriReferenceImage() {
        return uriReferenceImage;
    }

    public void setUriReferenceImage(String uriReferenceImage) {
        this.uriReferenceImage = uriReferenceImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }
}
