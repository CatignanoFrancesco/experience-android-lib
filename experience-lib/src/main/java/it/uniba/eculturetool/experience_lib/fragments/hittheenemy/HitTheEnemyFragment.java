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

import org.json.JSONException;

import it.uniba.eculturetool.experience_lib.models.hittheenemy.HitTheEnemy;
import it.uniba.eculturetool.experience_lib.models.hittheenemy.HitTheEnemyItem;
import it.uniba.eculturetool.experience_lib.saving.ExperienceFileParser;
import it.uniba.eculturetool.experience_lib.ui.HitTheEnemyUI;

public class HitTheEnemyFragment extends Fragment {
    private static final String KEY_INITIAL_HIT_THE_ENEMY = "KEY_INITIAL_HIT_THE_ENEMY";

    public static int NO_POSITION = -1;
    private final HitTheEnemyUI ui = HitTheEnemyUI.getInstance();
    private String operaId;
    private String hitTheEnemyId;
    private HitTheEnemyViewModel viewModel;

    public HitTheEnemyFragment() {}
    public static HitTheEnemyFragment newInstance(String operaId, String experienceId) {
        HitTheEnemyFragment fragment = new HitTheEnemyFragment();
        Bundle args = prepareBundle(operaId, experienceId);
        fragment.setArguments(args);
        return fragment;
    }

    public static HitTheEnemyFragment newInstance(String operaId, String experienceId, HitTheEnemy initialHitTheEnemy) {
        HitTheEnemyFragment fragment = new HitTheEnemyFragment();
        Bundle args = prepareBundle(operaId, experienceId);
        args.putString(KEY_INITIAL_HIT_THE_ENEMY, ExperienceFileParser.toJson(initialHitTheEnemy));
        fragment.setArguments(args);
        return fragment;
    }

    private static Bundle prepareBundle(String operaId, String experienceId) {
        Bundle args = new Bundle();
        args.putString(KEY_EXPERIENCE_ID, experienceId);
        args.putString(KEY_OPERA_ID, operaId);
        return args;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(HitTheEnemyViewModel.class);

        if (getArguments() != null) {
            operaId = getArguments().getString(KEY_OPERA_ID);
            hitTheEnemyId = getArguments().getString(KEY_EXPERIENCE_ID);

            try {
                if(getArguments().containsKey(KEY_INITIAL_HIT_THE_ENEMY)) {
                    HitTheEnemy initialHitTheEnemy = (HitTheEnemy) ExperienceFileParser.toExperience(getArguments().getString(KEY_INITIAL_HIT_THE_ENEMY));

                    viewModel.getHitTheEnemy().getHitTheEnemies().addAll(initialHitTheEnemy.getHitTheEnemies());
                }
            }
            catch (JSONException e) {}

        }
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

    public void addOrEditHitTheEnemy(int position) {
        if(position < 0)
            viewModel.setActiveHitTheEnemyItem(new HitTheEnemyItem());
        else viewModel.setActiveHitTheEnemyItem(viewModel.getHitTheEnemy().getHitTheEnemies().get(position));
        getChildFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(ui.hitTheEnemyGeneralUi.fragmentContainerView, new HitTheEnemyEditorFragment())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }

    public void back() {
        HitTheEnemyListFragment fragment = HitTheEnemyListFragment.newInstance(operaId, hitTheEnemyId);
        getChildFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(ui.hitTheEnemyGeneralUi.fragmentContainerView, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                .commit();
    }

    public Fragment getActualFragment() {
        return getChildFragmentManager().findFragmentById(ui.hitTheEnemyGeneralUi.fragmentContainerView);
    }
}