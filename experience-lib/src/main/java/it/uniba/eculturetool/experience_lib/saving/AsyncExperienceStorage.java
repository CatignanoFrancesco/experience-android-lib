package it.uniba.eculturetool.experience_lib.saving;

import android.graphics.Bitmap;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import it.uniba.eculturetool.experience_lib.models.Answer;
import it.uniba.eculturetool.experience_lib.models.Experience;
import it.uniba.eculturetool.experience_lib.models.FindDetails;
import it.uniba.eculturetool.experience_lib.models.FindRFID;
import it.uniba.eculturetool.experience_lib.models.FindTheDifference;
import it.uniba.eculturetool.experience_lib.models.Pattern;
import it.uniba.eculturetool.experience_lib.models.Puzzle;
import it.uniba.eculturetool.experience_lib.models.Question;
import it.uniba.eculturetool.experience_lib.models.Quiz;

public interface AsyncExperienceStorage {
    void saveQuiz(Quiz quiz, Consumer<Void> successDataListener, Consumer<String> failureDataListener);
    void savePuzzle(Puzzle puzzle, Consumer<Void> successDataListener, Consumer<String> failureDataListener);
    void saveFindTheDifference(FindTheDifference findTheDifference, Consumer<Void> successDataListener, Consumer<String> failureDataListener);
    void savePattern(Pattern pattern, Consumer<Void> successDataListener, Consumer<String> failureDataListener);
    void saveFindRFID(FindRFID findRFID, Consumer<Void> successDataListener, Consumer<String> failureDataListener);
    void saveFindDetails(FindDetails findDetails, Consumer<Void> successDataListener, Consumer<String> failureDataListener);
    void getExperiencesByOperaId(Object operaId, Consumer<Set<Experience>> successDataListener, Consumer<String> failureDataListener);
    void getQuestionImageById(Object questionId, Consumer<Bitmap> successDataListener, Consumer<String> failureDataListener);
    void getPuzzleImageById(Object puzzleId, Consumer<Bitmap> successDataListener, Consumer<String> failureDataListener);
    void getFindTheDifferenceImagesById(Object ftdId, BiConsumer<Bitmap, Bitmap> successDataListener, Consumer<String> failureDataListener);
    void getFindDetailsImageById(Object findDetailsId, Consumer<Bitmap> successDataListener, Consumer<String> failureDataListener);
    void removeExperienceById(Object operaId, Experience experience, Consumer<Void> successDataListener, Consumer<String> failureDataListener);
    void removeQuestionById(String quizId, Question question, Consumer<Void> successDataListener, Consumer<String> failureDataListener);
    void removeAnswerById(String quizId, String questionId, Answer answerId, Consumer<Void> successDataListener, Consumer<String> failureDataListener);
}
