package it.uniba.eculturetool.experience_lib.fragments.hittheenemy;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.uniba.eculturetool.experience_lib.R;

public class HitTheEnemyFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public HitTheEnemyFragment() {}
    public static HitTheEnemyFragment newInstance(String param1, String param2) {
        HitTheEnemyFragment fragment = new HitTheEnemyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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