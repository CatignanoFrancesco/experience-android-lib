package it.uniba.eculturetool.experience_lib.saving;

import android.graphics.Bitmap;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import it.uniba.eculturetool.experience_lib.models.Answer;
import it.uniba.eculturetool.experience_lib.models.Experience;
import it.uniba.eculturetool.experience_lib.models.Question;

public interface ExperienceStorage {
    void saveExperience(Object operaId, Experience experience);
    Set<Experience> getExperiencesByOperaId(Object operaId);
    void getQuestionImageById(Object questionId, Consumer<Bitmap> onImageReady, Consumer<String> onFailureListener);
    void getPuzzleImageById(Object puzzleId, Consumer<Bitmap> onImageReady, Consumer<String> onFailureListener);
    void getFindTheDifferenceImagesById(Object ftdId, BiConsumer<Bitmap, Bitmap> onImagesReady, Consumer<String> onFailureListener);
    void getFindDetailsImageById(Object findDetailsId, Consumer<Bitmap> onImageReady, Consumer<String> onFailureListener);
    void removeExperienceById(Object operaId, Experience experience);
    void removeQuestionById(String quizId, Question question);
    void removeAnswerById(String quizId, String questionId, Answer answer);
    void removeAnswerBySingleQuestion(String singleQuestionId, Answer answer);
}
