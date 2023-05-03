package it.uniba.eculturetool.experience_lib.fragments.hittheenemy;

import static it.uniba.eculturetool.experience_lib.ExperienceEditorFragment.KEY_EXPERIENCE_ID;
import static it.uniba.eculturetool.experience_lib.ExperienceEditorFragment.KEY_OPERA_ID;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.uniba.eculturetool.experience_lib.models.hittheenemy.HitTheEnemyItem;
import it.uniba.eculturetool.experience_lib.ui.HitTheEnemyUI;

public class HitTheEnemyFragment extends Fragment {
    private final HitTheEnemyUI ui = HitTheEnemyUI.getInstance();
    private String operaId;
    private String hitTheEnemyId;
    private HitTheEnemyViewModel viewModel;

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

        viewModel = new ViewModelProvider(requireActivity()).get(HitTheEnemyViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(ui.hitTheEnemyGeneralUi.layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        HitTheEnemyListFragment fragment = HitTheEnemyListFragment.newInstance(operaId, hitTheEnemyId);
        getChildFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .add(ui.hitTheEnemyGeneralUi.fragmentContainerView, fragment)
                .commit();
    }

    public void addHitTheEnemy() {
        viewModel.setActiveHitTheEnemyItem(new HitTheEnemyItem());

        getChildFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(ui.hitTheEnemyGeneralUi.fragmentContainerView, new HitTheEnemyEditorFragment())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }

    public void onHitEnemyCreted() {
        viewModel.setActiveHitTheEnemyItem(null);

        HitTheEnemyListFragment fragment = HitTheEnemyListFragment.newInstance(operaId, hitTheEnemyId);
        getChildFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(ui.hitTheEnemyGeneralUi.fragmentContainerView, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                .commit();
    }
}