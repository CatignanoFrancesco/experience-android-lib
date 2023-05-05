package it.uniba.eculturetool.experience_lib.fragments.pattern;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import it.uniba.eculturetool.experience_lib.models.Pattern;

public class PatternViewModel extends ViewModel {
    private int maxNum = 0;
    private MutableLiveData<Pattern> pattern = new MutableLiveData<>(new Pattern());

    public LiveData<Pattern> getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern.setValue(pattern);
    }

    public void setMatrix(int[][] matrix) {
        pattern.getValue().setMatrixPattern(matrix);
    }

    public int getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
    }

    public int incrementMaxNum() {
        return ++maxNum;
    }
}
