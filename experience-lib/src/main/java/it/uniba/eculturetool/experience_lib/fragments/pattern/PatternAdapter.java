package it.uniba.eculturetool.experience_lib.fragments.pattern;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import java.util.Arrays;

import it.uniba.eculturetool.experience_lib.models.Pattern;
import it.uniba.eculturetool.experience_lib.ui.PatternUI;

public class PatternAdapter extends BaseAdapter {
    private int[] matrix = {0, 0, 0, 0, 0, 0, 0, 0, 0};
    private static int maxNum = 0;
    private FragmentActivity activity;

    private final PatternUI ui = PatternUI.getInstance();
    private PatternViewModel viewModel;

    public PatternAdapter(FragmentActivity activity) {
        this.activity = activity;

        viewModel = new ViewModelProvider(activity).get(PatternViewModel.class);
    }

    public PatternAdapter(FragmentActivity activity, int[] matrix) {
        this(activity);
        this.matrix = matrix;
        maxNum = max(matrix);
    }

    public int getNumberOfSetDots() {
        int cont = 0;

        for (int j : matrix) {
            if (j != 0) cont++;
        }

        return cont;
    }

    @Override
    public int getCount() {
        return matrix.length;
    }

    @Override
    public Object getItem(int i) {
        return matrix;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        PatternViewHolder holder;

        if(view == null) {
            view = LayoutInflater.from(activity).inflate(ui.patternItemUI.layout, viewGroup, false);
            holder = new PatternViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (PatternViewHolder) view.getTag();
        }

        if(matrix[i] != 0) holder.numberTextView.setText(String.valueOf(matrix[i]));
        else holder.numberTextView.setText("");

        holder.numberTextView.setOnClickListener(v -> {
            if(matrix[i] == 0) {
                matrix[i] = ++maxNum;
                holder.numberTextView.setText(String.valueOf(matrix[i]));
            } else {
                for(int j=0; j<matrix.length; j++) {   // Update di tutti i numeri
                    if(matrix[j] > matrix[i]) matrix[j]--;
                }
                matrix[i] = 0;
                if(maxNum > 0) maxNum--;
            }

            viewModel.setMatrix(toMatrix(matrix));
            notifyDataSetChanged();
        });

        return view;
    }

    private static int[][] toMatrix(int[] array) {
        int[][] matrix = new int[Pattern.NUM_DOT][Pattern.NUM_DOT];
        int rowNum = array.length / Pattern.NUM_DOT;

        int rowIndex = 0;
        for(int i=0; i<rowNum; i++) {
            matrix[i] = Arrays.copyOfRange(array, rowIndex, rowIndex + Pattern.NUM_DOT);
            rowIndex += Pattern.NUM_DOT;
        }

        return matrix;
    }

    private static int max(int[] array) {
        int max = 0;

        for(int e : array) {
            if(e > max) max = e;
        }

        return max;
    }

    class PatternViewHolder {
        ConstraintLayout layout;
        TextView numberTextView;

        public PatternViewHolder(View view) {
            layout = view.findViewById(ui.patternItemUI.numberLayout);
            numberTextView = view.findViewById(ui.patternItemUI.numberTextView);
        }
    }
}
