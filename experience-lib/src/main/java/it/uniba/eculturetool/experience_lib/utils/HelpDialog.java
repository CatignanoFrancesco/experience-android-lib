package it.uniba.eculturetool.experience_lib.utils;

import android.app.AlertDialog;
import android.content.Context;

import it.uniba.eculturetool.experience_lib.R;

public class HelpDialog {
    private HelpDialog() {}

    public static void show(Context context, int stringMessageResource) {
        new AlertDialog.Builder(context)
                .setTitle(R.string.help_title)
                .setMessage(stringMessageResource)
                .setPositiveButton(R.string.ok_understand, null)
                .show();
    }
}
