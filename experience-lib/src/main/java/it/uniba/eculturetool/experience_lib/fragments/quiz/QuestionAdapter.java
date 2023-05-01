package it.uniba.eculturetool.experience_lib.fragments.quiz;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.uniba.eculturetool.experience_lib.ExperienceContextMenuListener;
import it.uniba.eculturetool.experience_lib.R;
import it.uniba.eculturetool.experience_lib.listeners.OnClickDeleteListener;
import it.uniba.eculturetool.experience_lib.models.Question;
import it.uniba.eculturetool.experience_lib.ui.QuizUI;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {
    private final QuizUI ui = QuizUI.getInstance();

    private List<Question> questions;
    private Context context;
    private QuizEditorFragment quizFragment;
    private OnClickDeleteListener<Question> onClickDeleteListener;

    public QuestionAdapter(Context context, QuizEditorFragment quizFragment, List<Question> questions, OnClickDeleteListener<Question> onClickDeleteListener) {
        this.context = context;
        this.questions = questions;
        this.quizFragment = quizFragment;
        this.onClickDeleteListener = onClickDeleteListener;
    }

    public void addQuestion(Question question) {
        questions.add(question);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(ui.questionAdapterUI.layout, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        Question question = questions.get(position);
        holder.questionText.setText(question.getQuestionText());
        holder.constraintLayout.setOnClickListener(v -> quizFragment.editQuestion(question));
        holder.question = question;

        Bitmap image = questions.get(position).getImage();
        if(image != null)
            holder.questionImage.setImageDrawable(new BitmapDrawable(context.getResources(), image));
        else
            holder.questionImage.setImageResource(R.drawable.ic_question_24);
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    class QuestionViewHolder extends RecyclerView.ViewHolder {
        TextView questionText;
        ImageView questionImage;
        View constraintLayout;
        Question question;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            questionText = itemView.findViewById(ui.questionAdapterUI.itemQuestionTextViewId);
            constraintLayout = itemView.findViewById(ui.questionAdapterUI.layoutId);
            questionImage = itemView.findViewById(ui.questionAdapterUI.itemImageViewId);

            constraintLayout.setOnCreateContextMenuListener(new ExperienceContextMenuListener(
                    context,
                    this.getAdapterPosition(),
                    question,
                    unused -> quizFragment.editQuestion(question),
                    () -> {
                        questions.remove(question);
                        onClickDeleteListener.onClickDelete(question);
                        notifyDataSetChanged();
                    }
            ));
        }
    }
}
