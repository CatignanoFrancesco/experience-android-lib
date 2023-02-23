package it.uniba.eculturetool.experience_lib;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import it.uniba.eculturetool.experience_lib.models.Experience;


public class ExperienceViewModel extends ViewModel {
    private MutableLiveData<Experience> experience = new MutableLiveData<>();


    public void setExperience(Experience experience) {
        this.experience.setValue(experience);
    }

    public LiveData<Experience> getExperience() {
        return experience;
    }
}
