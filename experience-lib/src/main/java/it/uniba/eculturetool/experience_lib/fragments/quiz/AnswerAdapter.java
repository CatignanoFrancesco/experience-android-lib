package it.uniba.eculturetool.experience_lib.fragments.quiz;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.uniba.eculturetool.experience_lib.ExperienceContextMenuListener;
import it.uniba.eculturetool.experience_lib.listeners.OnClickDeleteListener;
import it.uniba.eculturetool.experience_lib.models.Answer;
import it.uniba.eculturetool.experience_lib.ui.QuizUI;
import it.uniba.eculturetool.tag_lib.tag.model.LanguageTag;

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.AnswerViewHolder> {
    private final QuizUI ui = QuizUI.getInstance();

    private List<Answer> answers;
    private Context context;
    private QuestionEditorFragment questionEditorFragment;
    private List<LanguageTag> languageTags;
    private OnClickDeleteListener<Answer> onClickDeleteListener;

    public AnswerAdapter(Context context, List<Answer> answers, QuestionEditorFragment questionEditorFragment, List<LanguageTag> languageTags, OnClickDeleteListener<Answer> onClickDeleteListener) {
        this.context = context;
        this.answers = answers;
        this.onClickDeleteListener = onClickDeleteListener;
        this.languageTags = languageTags;
        this.questionEditorFragment = questionEditorFragment;
    }

    @NonNull
    @Override
    public AnswerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(ui.answerAdapterUI.layout, parent, false);

        return new AnswerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnswerViewHolder holder, int position) {
        if(answers == null || answers.isEmpty()) return;

        Answer answer = answers.get(position);
        holder.answerTextView.setText(answer.getDefaultText());

        if(answer.isCorrect()) holder.answerTextView.setTextColor(ColorStateList.valueOf(Color.parseColor("#228B22")));
        holder.layout.setOnClickListener(v -> new AddAnswerAlertDialog(context, questionEditorFragment, languageTags, answer).show());

        holder.layout.setOnCreateContextMenuListener(new ExperienceContextMenuListener(
                context,
                position,
                answer,
                unused -> new AddAnswerAlertDialog(context, questionEditorFragment, languageTags, answer).show(),
                () -> {
                    answers.remove(answer);
                    onClickDeleteListener.onClickDelete(answer);
                    notifyDataSetChanged();
                }
        ));
    }

    @Override
    public int getItemCount() {
        return answers.size();
    }

    public void addAnswer(Answer answer) {
        answers.add(answer);
        notifyDataSetChanged();
    }


    class AnswerViewHolder extends RecyclerView.ViewHolder {
        TextView answerTextView;
        View layout;

        public AnswerViewHolder(@NonNull View itemView) {
            super(itemView);

            answerTextView = itemView.findViewById(ui.answerAdapterUI.answerTextViewId);
            layout = itemView.findViewById(ui.answerAdapterUI.layoutId);
        }
    }
}
