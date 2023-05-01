package it.uniba.eculturetool.experience_lib.fragments.quiz;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.uniba.eculturetool.experience_lib.ExperienceContextMenuListener;
import it.uniba.eculturetool.experience_lib.listeners.OnClickDeleteListener;
import it.uniba.eculturetool.experience_lib.models.Answer;
import it.uniba.eculturetool.experience_lib.ui.QuizUI;

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.AnswerViewHolder> {
    private final QuizUI ui = QuizUI.getInstance();

    private List<Answer> answers;
    private Context context;
    private AnswerManager answerManager;
    private OnClickDeleteListener<Answer> onClickDeleteListener;

    public AnswerAdapter(Context context, List<Answer> answers, AnswerManager answerManager, OnClickDeleteListener<Answer> onClickDeleteListener) {
        this.context = context;
        this.answers = answers;
        this.onClickDeleteListener = onClickDeleteListener;
        this.answerManager = answerManager;
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
        holder.answerTextView.setText(answer.getAnswerText());

        if(answer.isCorrect()) holder.answerTextView.setTextColor(ColorStateList.valueOf(Color.parseColor("#228B22")));
        holder.layout.setOnClickListener(v -> new AddAnswerAlertDialog(context, answerManager, answer).show());

        holder.layout.setOnCreateContextMenuListener(new ExperienceContextMenuListener(
                context,
                position,
                answer,
                unused -> new AddAnswerAlertDialog(context, answerManager, answer).show(),
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
