package it.uniba.eculturetool.experience_lib.fragments.recognizetheobject;

import androidx.lifecycle.ViewModel;

import it.uniba.eculturetool.experience_lib.models.RecognizeTheObject;

public class RecognizeTheObjectViewModel extends ViewModel {
    private RecognizeTheObject recognizeTheObject = new RecognizeTheObject();

    public RecognizeTheObject getRecognizeTheObject() {
        return recognizeTheObject;
    }

    public void setRecognizeTheObject(RecognizeTheObject recognizeTheObject) {
        this.recognizeTheObject = recognizeTheObject;
    }
}
