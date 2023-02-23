package it.uniba.eculturetool.experience_lib.fragments.findrfid;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import it.uniba.eculturetool.experience_lib.models.FindRFID;

public class FindRfidViewModel extends ViewModel {
    private MutableLiveData<FindRFID> findRfid = new MutableLiveData<>(new FindRFID());

    public LiveData<FindRFID> getFindRfid() {
        return findRfid;
    }

    public void setFindRfid(FindRFID findRfid) {
        this.findRfid.setValue(findRfid);
    }
}
