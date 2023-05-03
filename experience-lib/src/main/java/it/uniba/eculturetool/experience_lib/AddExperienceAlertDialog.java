package it.uniba.eculturetool.experience_lib;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;

import java.util.List;
import java.util.function.Consumer;

import it.uniba.eculturetool.experience_lib.models.Experience;
import it.uniba.eculturetool.experience_lib.models.FindDetails;
import it.uniba.eculturetool.experience_lib.models.FindRFID;
import it.uniba.eculturetool.experience_lib.models.FindTheDifference;
import it.uniba.eculturetool.experience_lib.models.hittheenemy.HitTheEnemy;
import it.uniba.eculturetool.experience_lib.models.Pattern;
import it.uniba.eculturetool.experience_lib.models.Puzzle;
import it.uniba.eculturetool.experience_lib.models.Quiz;
import it.uniba.eculturetool.experience_lib.models.SingleQuestion;

public class AddExperienceAlertDialog {

    private AddExperienceAlertDialog() {}

    public static void show(Context context, List<Experience> addedExperiences, Consumer<Experience> onOkClick, Runnable onImportClick) {
        String puzzle = context.getString(R.string.puzzle);
        String quiz = context.getString(R.string.quiz);
        String findTheDifference = context.getString(R.string.find_the_difference);
        String pattern = context.getString(R.string.pattern);
        String findRfid = context.getString(R.string.find_rfid);
        String findDetails = context.getString(R.string.find_details);
        String singleQuestion = context.getString(R.string.single_question);
        String hitTheEnemy = context.getString(R.string.hit_the_enemy);

        new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.add_experience_title))
                .setItems(
                        new String[]{puzzle, quiz, findTheDifference, pattern, findRfid, findDetails, singleQuestion, hitTheEnemy},
                        (dialogInterface, i) -> {
                            Experience experience;
                            switch (i) {
                                case 0:
                                    experience = new Puzzle();
                                    break;
                                case 1:
                                    experience = new Quiz();
                                    break;
                                case 2:
                                    experience = new FindTheDifference();
                                    break;
                                case 3:
                                    experience = new Pattern();
                                    break;
                                case 4:
                                    experience = new FindRFID();
                                    break;
                                case 5:
                                    experience = new FindDetails();
                                    break;
                                case 6:
                                    experience = new SingleQuestion();
                                    break;
                                case 7:
                                    experience = new HitTheEnemy();
                                    break;
                                default: experience = null;
                            }

                            if(addedExperiences != null) {
                                for(Experience exp : addedExperiences) {
                                    if(experience.getClass().equals(exp.getClass())) {
                                        new AlertDialog.Builder(context)
                                                .setTitle(R.string.warning)
                                                .setMessage(R.string.error_selection)
                                                .setNeutralButton(R.string.understand, (dialogInterface1, i1) -> {})
                                                .show();
                                        return;
                                    }
                                }
                            }
                            onOkClick.accept(experience);
                        }
                )
                .setNeutralButton(context.getString(R.string.import_experience), ((dialogInterface, i) -> onImportClick.run()))
                .show();
    }
}
