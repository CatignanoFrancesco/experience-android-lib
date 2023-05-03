package it.uniba.eculturetool.experience_lib.fragments.hittheenemy;

import static it.uniba.eculturetool.experience_lib.ExperienceEditorFragment.KEY_EXPERIENCE_ID;
import static it.uniba.eculturetool.experience_lib.ExperienceEditorFragment.KEY_OPERA_ID;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.uniba.eculturetool.experience_lib.R;

public class HitTheEnemyFragment extends Fragment {
    private String operaId;
    private String hitTheEnemyId;

    public HitTheEnemyFragment() {}
    public static HitTheEnemyFragment newInstance(String operaId, String experienceId) {
        HitTheEnemyFragment fragment = new HitTheEnemyFragment();
        Bundle args = new Bundle();
        args.putString(KEY_EXPERIENCE_ID, experienceId);
        args.putString(KEY_OPERA_ID, operaId);
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

        HitTheEnemyListFragment fragment = HitTheEnemyListFragment.newInstance(operaId, hitTheEnemyId);
        getChildFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.hit_the_enemy_fragment_container_view, fragment)
                .commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_hit_the_enemy, container, false);
    }

    public void addHitTheEnemy() {
        getChildFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(null)
                .replace(R.id.hit_the_enemy_fragment_container_view, new HitTheEnemyEditorFragment())
                .commit();
    }
}