package it.uniba.eculturetool.experience_lib.fragments.hittheenemy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import it.uniba.eculturetool.experience_lib.R;
import it.uniba.eculturetool.experience_lib.models.hittheenemy.HitTheEnemyItem;
import it.uniba.eculturetool.experience_lib.ui.HitTheEnemyUI;

public class HitTheEnemyAdapter extends RecyclerView.Adapter<HitTheEnemyAdapter.HitTheEnemyViewHolder> {
    private HitTheEnemyUI ui = HitTheEnemyUI.getInstance();

    private Context context;
    private List<HitTheEnemyItem> hitTheEnemies;

    public HitTheEnemyAdapter(Context context, List<HitTheEnemyItem> hitTheEnemies) {
        this.context = context;
        this.hitTheEnemies = hitTheEnemies;
    }

    @NonNull
    @Override
    public HitTheEnemyAdapter.HitTheEnemyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HitTheEnemyViewHolder(LayoutInflater.from(context).inflate(ui.hitTheEnemyAdapterUi.layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HitTheEnemyAdapter.HitTheEnemyViewHolder holder, int position) {
        HitTheEnemyItem hte = hitTheEnemies.get(position);

        Picasso.get().load(hte.getUriCharacter()).into(holder.image);
        holder.name.setText(hte.getCharacterName());
        holder.difficulty.setText(context.getString(R.string.difficulty_text_indicator, hte.getDifficulty().toString()));
    }

    @Override
    public int getItemCount() {
        return hitTheEnemies.size();
    }

    class HitTheEnemyViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;
        TextView difficulty;
        View layout;

        public HitTheEnemyViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(ui.hitTheEnemyAdapterUi.characterImageView);
            name = itemView.findViewById(ui.hitTheEnemyAdapterUi.characterNameTextView);
            difficulty = itemView.findViewById(ui.hitTheEnemyAdapterUi.difficultyTextView);
            layout = itemView.findViewById(ui.hitTheEnemyAdapterUi.layoutId);
        }
    }
}
