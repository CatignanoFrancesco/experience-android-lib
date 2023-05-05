package it.uniba.eculturetool.experience_lib;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import it.uniba.eculturetool.experience_lib.listeners.OnClickDeleteListener;
import it.uniba.eculturetool.experience_lib.listeners.OnOpenListener;
import it.uniba.eculturetool.experience_lib.models.Experience;
import it.uniba.eculturetool.experience_lib.models.FindDetails;
import it.uniba.eculturetool.experience_lib.models.FindRFID;
import it.uniba.eculturetool.experience_lib.models.FindTheDifference;
import it.uniba.eculturetool.experience_lib.models.Questionnaire;
import it.uniba.eculturetool.experience_lib.models.RecognizeTheObject;
import it.uniba.eculturetool.experience_lib.models.hittheenemy.HitTheEnemy;
import it.uniba.eculturetool.experience_lib.models.Pattern;
import it.uniba.eculturetool.experience_lib.models.Puzzle;
import it.uniba.eculturetool.experience_lib.models.Quiz;
import it.uniba.eculturetool.experience_lib.models.SingleQuestion;
import it.uniba.eculturetool.experience_lib.saving.ExperienceFileParser;
import it.uniba.eculturetool.experience_lib.ui.ExperienceUI;
import it.uniba.eculturetool.experience_lib.utils.EctExpLibFileProvider;

public class ExperienceAdapter extends RecyclerView.Adapter<ExperienceAdapter.ExperienceViewHolder> {
    private final ExperienceUI ui = ExperienceUI.getInstance();

    private Activity activity;
    private List<Experience> experiences;
    private OnOpenListener<Experience> onOpenListener;
    private OnClickDeleteListener<Experience> onClickDeleteListener;

    public ExperienceAdapter(Activity activity, List<Experience> experiences) {
        this.activity = activity;
        this.experiences = experiences;
    }

    public ExperienceAdapter(Activity activity, List<Experience> experiences, OnOpenListener<Experience> onOpenListener, OnClickDeleteListener<Experience> onClickDeleteListener) {
        this(activity, experiences);
        this.onClickDeleteListener = onClickDeleteListener;
        this.onOpenListener = onOpenListener;
    }

    public void addExperiences(List<Experience> experiences) {
        this.experiences = experiences;
    }

    @NonNull
    @Override
    public ExperienceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(ui.experienceAdapterUI.layout, parent, false);
        return new ExperienceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExperienceViewHolder holder, int position) {
        Experience experience = experiences.get(position);

        if(experience.getPoints() > 0) {
            holder.difficultyText.setText(activity.getString(R.string.difficulty_value, experience.getDifficulty().toString(activity)));
            holder.pointsText.setText(activity.getString(R.string.points_value, experience.getPoints()));
        }

        // Immagini
        if(experience instanceof Quiz) {
            holder.typeImage.setImageResource(R.drawable.ic_quiz_48);
            holder.typeText.setText(R.string.quiz);
        }
        else if(experience instanceof Puzzle) {
            holder.typeImage.setImageResource(R.drawable.ic_puzzle_48);
            holder.typeText.setText(R.string.puzzle);
        }
        else if(experience instanceof FindTheDifference) {
            holder.typeImage.setImageResource(R.drawable.ic_find_the_difference);
            holder.typeText.setText(R.string.find_the_difference);
        }
        else if(experience instanceof FindRFID) {
            holder.typeImage.setImageResource(R.drawable.ic_baseline_nfc_24);
            holder.typeText.setText(R.string.find_rfid);
        }
        else if(experience instanceof Pattern) {
            holder.typeImage.setImageResource(R.drawable.ic_baseline_pattern_24);
            holder.typeText.setText(R.string.pattern);
        }
        else if(experience instanceof FindDetails) {
            holder.typeImage.setImageResource(R.drawable.ic_find_details);
            holder.typeText.setText(R.string.find_details);
        }
        else if(experience instanceof SingleQuestion) {
            holder.typeImage.setImageResource(R.drawable.ic_question_24);
            holder.typeText.setText(R.string.single_question);
        }
        else if(experience instanceof HitTheEnemy) {
            holder.typeImage.setImageResource(R.drawable.ic_hit_the_enemy);
            holder.typeText.setText(R.string.hit_the_enemy);
        }
        else if(experience instanceof RecognizeTheObject) {
            holder.typeImage.setImageResource(R.drawable.ic_recognize_the_object);
            holder.typeText.setText(R.string.recognize_the_object);
        }
        else if(experience instanceof Questionnaire) {
            holder.typeImage.setImageResource(R.drawable.ic_questionnaire);
            holder.typeText.setText(((Questionnaire) experience).getName());
        }

        // Click
        if(onOpenListener == null || onClickDeleteListener == null) {
            holder.layout.setClickable(false);
        } else {
            holder.layout.setClickable(true);

            holder.layout.setOnClickListener(v -> onOpenListener.onOpen(experience));
            holder.layout.setOnCreateContextMenuListener(new ExperienceContextMenuListener<>(
                    activity,
                    holder.getAdapterPosition(),
                    experience,
                    onOpenListener,
                    () -> {
                        experiences.remove(experience);
                        onClickDeleteListener.onClickDelete(experience);
                        notifyDataSetChanged();
                    },
                    item -> {
                        try {
                            // Directory
                            File dir = new File(activity.getFilesDir(), "experiences");
                            if(!dir.exists()) dir.mkdirs();

                            // Creazione del file
                            File file;
                            int i = 0;
                            do {
                                file = new File(dir, experience.getClass().getSimpleName() + (i==0 ? "" : i) + ".json");
                                i++;
                            } while (file.exists());

                            // Inserimento dei dati nel file
                            FileWriter writer = new FileWriter(file);
                            writer.append(ExperienceFileParser.toJson(experience));
                            writer.flush();
                            writer.close();

                            // Esportazione
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_SEND);
                            intent.putExtra(Intent.EXTRA_STREAM, EctExpLibFileProvider.getUriForFile(activity, "it.uniba.eculturetool.experience_lib.ectl_provider", file));
                            intent.setType("application/json");
                            activity.startActivity(Intent.createChooser(intent, null));
                        }
                        catch (IOException e) {
                            Log.e(ExperienceAdapter.class.getSimpleName(), "onBindViewHolder: ", e);
                            Toast.makeText(activity, activity.getString(R.string.share_error), Toast.LENGTH_SHORT).show();
                        }
                    }
            ));
        }
    }

    @Override
    public int getItemCount() {
        return experiences.size();
    }


    class ExperienceViewHolder extends RecyclerView.ViewHolder {
        ImageView typeImage;
        TextView typeText, difficultyText, pointsText;
        View layout;

        public ExperienceViewHolder(@NonNull View itemView) {
            super(itemView);

            typeImage = itemView.findViewById(ui.experienceAdapterUI.itemTypeImageId);
            typeText = itemView.findViewById(ui.experienceAdapterUI.itemTypeTextViewId);
            difficultyText = itemView.findViewById(ui.experienceAdapterUI.difficultyTextViewId);
            pointsText = itemView.findViewById(ui.experienceAdapterUI.pointsTextViewId);
            layout = itemView.findViewById(ui.experienceAdapterUI.layoutId);
        }
    }
}
