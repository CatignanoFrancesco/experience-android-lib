package it.uniba.eculturetool.experience_lib;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;

import java.util.List;

import it.uniba.eculturetool.tag_lib.tag.model.LanguageTag;

public class AddExperienceAlertDialog {

    private AddExperienceAlertDialog() {}

    public static void show(Context context, String operaId, List<LanguageTag> languageTags, Runnable onOkClick, Runnable onImportClick) {
        String puzzle = context.getString(R.string.puzzle);
        String quiz = context.getString(R.string.quiz);
        String findTheDifference = context.getString(R.string.find_the_difference);
        String pattern = context.getString(R.string.pattern);
        String findRfid = context.getString(R.string.find_rfid);
        String findDetails = context.getString(R.string.find_details);

        new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.add_experience_title))
                .setItems(
                        new String[]{puzzle, quiz, findTheDifference, pattern, findRfid, findDetails},
                        (dialogInterface, i) -> onOkClick.run()
                )
                .setNeutralButton(context.getString(R.string.import_experience), ((dialogInterface, i) -> onImportClick.run()))
                .show();
    }
}
