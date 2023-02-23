package it.uniba.eculturetool.experience_lib.fragments.findthedifferences;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import it.uniba.eculturetool.experience_lib.models.Coordinate;
import it.uniba.eculturetool.experience_lib.models.FindTheDifference;

public class FindTheDifferenceViewModel extends ViewModel {
    private MutableLiveData<FindTheDifference> findTheDifference = new MutableLiveData<>(new FindTheDifference());

    public LiveData<FindTheDifference> getFindTheDifference() {
        return findTheDifference;
    }

    public void setFindTheDifference(FindTheDifference findTheDifference) {
        this.findTheDifference.setValue(findTheDifference);
    }

    public void setOriginalImage(Bitmap originalImage) {
        findTheDifference.getValue().setOriginalImage(originalImage);
        trigger();
    }

    public void setDifferentImage(Bitmap differentImage) {
        findTheDifference.getValue().setDifferentImage(differentImage);
        trigger();
    }

    public void addCoordinate(Coordinate c) {
        if(findTheDifference.getValue().getDifferencesCoordinates() == null) findTheDifference.getValue().setDifferencesCoordinates(new ArrayList<>());

        findTheDifference.getValue().getDifferencesCoordinates().add(c);
    }

    public void addAllCoordinates(List<Coordinate> coordinates) {
        if(findTheDifference.getValue().getDifferencesCoordinates() == null) findTheDifference.getValue().setDifferencesCoordinates(new ArrayList<>());

        findTheDifference.getValue().getDifferencesCoordinates().clear();
        findTheDifference.getValue().getDifferencesCoordinates().addAll(coordinates);
    }

    private void trigger() {
        findTheDifference.setValue(findTheDifference.getValue());
    }
}
