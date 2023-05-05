package it.uniba.eculturetool.experience_lib.fragments.questionnaire;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.uniba.eculturetool.experience_lib.ui.QuestionnaireUI;

public class QuestionnaireOptionAdapter extends RecyclerView.Adapter<QuestionnaireOptionAdapter.OptionViewHolder> {
    private final QuestionnaireUI ui = QuestionnaireUI.getInstance();

    private Context context;
    private List<String> options;

    public QuestionnaireOptionAdapter(Context context, List<String> options) {
        this.context = context;
        this.options = options;
    }

    @NonNull
    @Override
    public OptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OptionViewHolder(LayoutInflater.from(context).inflate(ui.optionAdapterUi.layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OptionViewHolder holder, int position) {
        holder.textView.setText(options.get(position));
    }

    @Override
    public int getItemCount() {
        return options.size();
    }


    class OptionViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public OptionViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(ui.optionAdapterUi.optionTextView);
        }
    }
}
