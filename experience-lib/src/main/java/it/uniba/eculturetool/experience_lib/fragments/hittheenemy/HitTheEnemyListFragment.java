package it.uniba.eculturetool.experience_lib.fragments.hittheenemy;

import static it.uniba.eculturetool.experience_lib.ExperienceEditorFragment.KEY_EXPERIENCE_ID;
import static it.uniba.eculturetool.experience_lib.ExperienceEditorFragment.KEY_OPERA_ID;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.uniba.eculturetool.experience_lib.R;
public class HitTheEnemyListFragment extends Fragment {
    private String operaId;
    private String hitTheEnemyId;

    public HitTheEnemyListFragment() {}

    public static HitTheEnemyListFragment newInstance(String operaId, String hitTheEnemyId) {
        HitTheEnemyListFragment fragment = new HitTheEnemyListFragment();
        Bundle args = new Bundle();
        args.putString(KEY_OPERA_ID, operaId);
        args.putString(KEY_EXPERIENCE_ID, hitTheEnemyId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            operaId = getArguments().getString(KEY_OPERA_ID);
            hitTheEnemyId = getArguments().getString(KEY_EXPERIENCE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return null;//inflater.inflate(R.layout.fragment_hit_the_enemy_list, container, false);
    }
}