package it.uniba.eculturetool.experience_lib.fragments.questionnaire;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.uniba.eculturetool.experience_lib.models.PreparedQuestion;
import it.uniba.eculturetool.experience_lib.ui.QuestionnaireUI;

public class QuestionnaireAdapter extends RecyclerView.Adapter<QuestionnaireAdapter.QuestionnaireViewHolder> {
    private final QuestionnaireUI ui = QuestionnaireUI.getInstance();

    private Context context;
    private List<PreparedQuestion> preparedQuestions;

    public QuestionnaireAdapter(Context context, List<PreparedQuestion> preparedQuestions) {
        this.context = context;
        this.preparedQuestions = preparedQuestions;
    }

    @NonNull
    @Override
    public QuestionnaireViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new QuestionnaireViewHolder(LayoutInflater.from(context).inflate(ui.questionAdapterUi.layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionnaireViewHolder holder, int position) {
        PreparedQuestion preparedQuestion = preparedQuestions.get(position);

        holder.text.setText(preparedQuestion.getText());
    }

    @Override
    public int getItemCount() {
        return preparedQuestions.size();
    }

    class QuestionnaireViewHolder extends RecyclerView.ViewHolder {
        TextView text;
        RecyclerView recyclerView;

        public QuestionnaireViewHolder(@NonNull View itemView) {
            super(itemView);

            text = itemView.findViewById(ui.questionAdapterUi.questionTextView);
            recyclerView = itemView.findViewById(ui.questionAdapterUi.answersRecyclerView);
        }
    }
}
