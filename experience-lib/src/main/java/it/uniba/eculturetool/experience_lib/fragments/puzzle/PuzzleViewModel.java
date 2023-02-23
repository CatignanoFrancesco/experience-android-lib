package it.uniba.eculturetool.experience_lib.fragments.puzzle;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import it.uniba.eculturetool.experience_lib.models.Puzzle;

public class PuzzleViewModel extends ViewModel {
    private MutableLiveData<Puzzle> puzzle = new MutableLiveData<>(new Puzzle());

    public LiveData<Puzzle> getPuzzle() {
        return puzzle;
    }

    public void setPuzzle(Puzzle puzzle) {
        this.puzzle.setValue(puzzle);
    }

    public void setImageToPuzzle(Bitmap image) {
        if(puzzle.getValue() != null) {
            puzzle.getValue().setImage(image);
            trigger();
        }
    }

    private void trigger() {
        puzzle.setValue(puzzle.getValue());
    }
}
