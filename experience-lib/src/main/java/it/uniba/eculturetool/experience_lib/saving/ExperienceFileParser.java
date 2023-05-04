package it.uniba.eculturetool.experience_lib.saving;

import android.content.Context;
import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import it.uniba.eculturetool.experience_lib.models.Experience;
import it.uniba.eculturetool.experience_lib.models.FindDetails;
import it.uniba.eculturetool.experience_lib.models.FindRFID;
import it.uniba.eculturetool.experience_lib.models.FindTheDifference;
import it.uniba.eculturetool.experience_lib.models.RecognizeTheObject;
import it.uniba.eculturetool.experience_lib.models.hittheenemy.HitTheEnemy;
import it.uniba.eculturetool.experience_lib.models.Pattern;
import it.uniba.eculturetool.experience_lib.models.Puzzle;
import it.uniba.eculturetool.experience_lib.models.Quiz;
import it.uniba.eculturetool.experience_lib.models.SingleQuestion;

/**
 * Classe che si occupa di creare e leggere i file relativi alle experience
 */
public class ExperienceFileParser {
    private static final String TYPE = "type";
    private static final String TYPE_QUIZ = "quiz";
    private static final String TYPE_PUZZLE = "puzzle";
    private static final String TYPE_PATTERN = "pattern";
    private static final String TYPE_FIND_DETAILS = "find_details";
    private static final String TYPE_FIND_RFID = "find_rfid";
    private static final String TYPE_FIND_THE_DIFFERENCE = "find_the_difference";
    private static final String TYPE_SINGLE_QUESTION = "single_question";
    private static final String TYPE_HIT_THE_ENEMY = "hit_the_enemy";
    private static final String TYPE_RECOGNIZE_OBJECT = "recognize_the_object";

    private ExperienceFileParser() {}

    /**
     * Restituisce l'oggetto sotto forma di JSON
     * @param experience L'experience da convertire
     * @return La stringa nel formato JSON
     */
    public static String toJson(Experience experience) {
        Gson gson = new Gson();
        JsonElement element = gson.toJsonTree(experience);
        element.getAsJsonObject().addProperty(TYPE, getType(experience));

        return gson.toJson(element);
    }

    public static Experience toExperience(Context context, Uri uri) throws IOException, JSONException {
        // Ottenimento del file json sotto forma di stringa
        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        for(String line; (line = bufferedReader.readLine()) != null;) sb.append(line);

        return toExperience(sb.toString());
    }

    public static Experience toExperience(String jsonExperience) throws JSONException {
        // Eliminazione del campo "type" in modo da non creare problemi con la deserializzazione
        JSONObject jsonObject = new JSONObject(jsonExperience);
        String type = (String) jsonObject.remove(TYPE);

        return new Gson().fromJson(jsonObject.toString(), getClass(type));
    }

    private static String getType(Experience experience) {
        if(experience instanceof Quiz) return TYPE_QUIZ;
        if(experience instanceof Puzzle) return TYPE_PUZZLE;
        if(experience instanceof Pattern) return TYPE_PATTERN;
        if(experience instanceof FindDetails) return TYPE_FIND_DETAILS;
        if(experience instanceof FindRFID) return TYPE_FIND_RFID;
        if(experience instanceof HitTheEnemy) return TYPE_HIT_THE_ENEMY;
        if(experience instanceof RecognizeTheObject) return TYPE_RECOGNIZE_OBJECT;
        return TYPE_FIND_THE_DIFFERENCE;
    }

    private static Class<? extends Experience> getClass(String type) {
        if(type.equals(TYPE_QUIZ)) return Quiz.class;
        if(type.equals(TYPE_PUZZLE)) return Puzzle.class;
        if(type.equals(TYPE_PATTERN)) return Pattern.class;
        if(type.equals(TYPE_FIND_DETAILS)) return FindDetails.class;
        if(type.equals(TYPE_FIND_RFID)) return FindRFID.class;
        if(type.equals(TYPE_SINGLE_QUESTION)) return SingleQuestion.class;
        if(type.equals(TYPE_HIT_THE_ENEMY)) return HitTheEnemy.class;
        if(type.equals(TYPE_RECOGNIZE_OBJECT)) return RecognizeTheObject.class;
        return FindTheDifference.class;
    }
}
