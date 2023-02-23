package it.uniba.eculturetool.experience_lib.fragments.finddetails;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import it.uniba.eculturetool.experience_lib.models.Coordinate;
import it.uniba.eculturetool.experience_lib.models.FindDetails;

public class FindDetailsViewModel extends ViewModel {
    private MutableLiveData<FindDetails> findDetails = new MutableLiveData<>(new FindDetails());

    public LiveData<FindDetails> getFindDetails() {
        return findDetails;
    }

    public void setFindDetails(FindDetails findDetails) {
        this.findDetails.setValue(findDetails);
    }

    public void setImage(Bitmap image) {
        findDetails.getValue().setImage(image);
        this.findDetails.setValue(findDetails.getValue());
    }

    public void addCoordinate(Coordinate c) {
        if(findDetails.getValue().getCoordinates() == null) findDetails.getValue().setCoordinates(new ArrayList<>());

        findDetails.getValue().getCoordinates().add(c);
    }
}
