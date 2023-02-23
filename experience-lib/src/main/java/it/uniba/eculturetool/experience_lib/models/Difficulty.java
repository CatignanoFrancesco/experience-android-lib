package it.uniba.eculturetool.experience_lib.models;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import it.uniba.eculturetool.experience_lib.R;

public enum Difficulty {
    LOW, MEDIUM, HIGH, IMPOSSIBLE;

    private static Map<Integer, Difficulty> map = new HashMap<>();

    static {
        map.put(0, LOW);
        map.put(1, MEDIUM);
        map.put(2,  HIGH);
        map.put(3, IMPOSSIBLE);
    }

    public String toString(Context context) {
        if(this == LOW) return context.getString(R.string.diff_low);
        if(this == MEDIUM) return context.getString(R.string.diff_medium);
        if(this == HIGH) return context.getString(R.string.diff_high);
        return context.getString(R.string.diff_impossible);
    }

    public static Difficulty valueOf(int difficultyNumber) {
        return map.getOrDefault(difficultyNumber, LOW);
    }
}