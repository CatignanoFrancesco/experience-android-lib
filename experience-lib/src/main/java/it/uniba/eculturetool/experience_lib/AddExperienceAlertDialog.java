package it.uniba.eculturetool.experience_lib;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import it.uniba.eculturetool.experience_lib.models.Experience;
import it.uniba.eculturetool.experience_lib.models.FindDetails;
import it.uniba.eculturetool.experience_lib.models.FindRFID;
import it.uniba.eculturetool.experience_lib.models.FindTheDifference;
import it.uniba.eculturetool.experience_lib.models.Questionnaire;
import it.uniba.eculturetool.experience_lib.models.RecognizeTheObject;
import it.uniba.eculturetool.experience_lib.models.hittheenemy.HitTheEnemy;
import it.uniba.eculturetool.experience_lib.models.Pattern;
import it.uniba.eculturetool.experience_lib.models.Puzzle;
import it.uniba.eculturetool.experience_lib.models.Quiz;
import it.uniba.eculturetool.experience_lib.models.SingleQuestion;

public class AddExperienceAlertDialog {

    private AddExperienceAlertDialog() {}

    /**
     * Mostra un dialog per scegliere quale experience aggiungere
     * @param context Il context
     * @param onOkClick Listener del click dell'experience
     * @param onImportClick Listener del click di importazione
     */
    public static void show(Context context, Consumer<Experience> onOkClick, Runnable onImportClick) {
        show(context, null, onOkClick, onImportClick);
    }

    /**
     * Mostra un dialog per scegliere quale experience aggiungere
     * @param context Il context
     * @param filter Un filtro per mostrare solo alcune experience. Basta inserire nella lista le classi di experience che si vogliono mostrare. Passare un valore nullo o una lista vuota per mostrarle tutte
     * @param onOkClick Listener del click dell'experience
     * @param onImportClick Listener del click di importazione
     */
    public static void show(Context context, @Nullable List<Class<? extends Experience>> filter, Consumer<Experience> onOkClick, Runnable onImportClick) {
        final List<ExperienceCouple> couples = new ArrayList<>();
        couples.add(new ExperienceCouple(context.getString(R.string.puzzle), new Puzzle()));
        couples.add(new ExperienceCouple(context.getString(R.string.quiz), new Quiz()));
        couples.add(new ExperienceCouple(context.getString(R.string.find_the_difference), new FindTheDifference()));
        couples.add(new ExperienceCouple(context.getString(R.string.pattern), new Pattern()));
        couples.add(new ExperienceCouple(context.getString(R.string.find_rfid), new FindRFID()));
        couples.add(new ExperienceCouple(context.getString(R.string.find_details), new FindDetails()));
        couples.add(new ExperienceCouple(context.getString(R.string.single_question), new SingleQuestion()));
        couples.add(new ExperienceCouple(context.getString(R.string.hit_the_enemy), new HitTheEnemy()));
        couples.add(new ExperienceCouple(context.getString(R.string.recognize_the_object), new RecognizeTheObject()));
        couples.add(new ExperienceCouple(context.getString(R.string.questionnaire), new Questionnaire()));
        Log.d("BLABLA", "show: " + couples);

        // Preparo l'array con i nomi
        String[] experienceNames;
        if(filter != null && !filter.isEmpty()) {
            List<ExperienceCouple> temp = couples.stream().filter(couple -> filter.contains(couple.experience.getClass())).collect(Collectors.toList());
            couples.clear();
            couples.addAll(temp);
        }
        Log.d("BLABLA", "show: " + couples);

        experienceNames = couples.stream().map(couple -> couple.name).collect(Collectors.toList()).toArray(new String[] {});
        Log.d("BLABLA", "show: " + Arrays.toString(experienceNames));

        new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.add_experience_title))
                .setItems(
                        experienceNames,
                        (dialogInterface, i) -> onOkClick.accept(couples.get(i).experience)
                )
                .setNeutralButton(context.getString(R.string.import_experience), ((dialogInterface, i) -> onImportClick.run()))
                .show();
    }


    static class ExperienceCouple {
        String name;
        Experience experience;

        ExperienceCouple(String name, Experience experience) {
            this.name = name;
            this.experience = experience;
        }
    }
}
