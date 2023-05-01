package it.uniba.eculturetool.experience_lib.fragments.quiz;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;

import it.uniba.eculturetool.experience_lib.R;
import it.uniba.eculturetool.experience_lib.models.Answer;

public class AddAnswerAlertDialog {
    private Context context;
    private View layout;
    private EditText answerEditText;
    private CheckBox checkBox;
    private AnswerManager answerManager;
    private Answer answer;

    public AddAnswerAlertDialog(Context context, AnswerManager answerManager) {
        this(context, answerManager, null);
    }

    public AddAnswerAlertDialog(Context context, AnswerManager answerManager, Answer answer) {
        this.answer = answer;
        this.context = context;
        this.answerManager = answerManager;

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layout = layoutInflater.inflate(R.layout.add_answer_layout, null);

        answerEditText = layout.findViewById(R.id.answer_edit_text);
        checkBox = layout.findViewById(R.id.correct_checkbox);

        loadData();
    }

    public void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setView(layout).setPositiveButton(R.string.save, this::onClickListener).setNegativeButton(R.string.cancel, (dialogInterface, i) -> dialogInterface.dismiss()).show();
    }

    private void loadData() {
        if(answer != null) {
            answerEditText.setText(answer.getAnswerText());
            checkBox.setChecked(answer.isCorrect());
        }
    }

    private void onClickListener(DialogInterface dialogInterface, int i) {
        if(!validate()) return;

        answerManager.onAnswerCreated(answer != null ? answer : new Answer(answerEditText.getText().toString(), checkBox.isChecked()));
    }

    private boolean validate() {
        if(answerEditText.getText().toString().isEmpty()) {
            answerEditText.setError(context.getString(R.string.empty_answer));
            answerEditText.requestFocus();
            return false;
        }
        return true;
    }
}
