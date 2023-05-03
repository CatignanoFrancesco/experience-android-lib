package it.uniba.eculturetool.experience_lib.fragments.hittheenemy;

import static it.uniba.eculturetool.experience_lib.ExperienceEditorFragment.KEY_EXPERIENCE_ID;
import static it.uniba.eculturetool.experience_lib.ExperienceEditorFragment.KEY_OPERA_ID;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import it.uniba.eculturetool.experience_lib.R;
import it.uniba.eculturetool.experience_lib.models.hittheenemy.HitTheEnemy;
import it.uniba.eculturetool.experience_lib.ui.HitTheEnemyUI;

public class HitTheEnemyListFragment extends Fragment {
    private final HitTheEnemyUI ui = HitTheEnemyUI.getInstance();
    private String operaId;
    private String hitTheEnemyId;
    private HitTheEnemyViewModel viewModel;

    private RecyclerView recyclerView;
    private HitTheEnemyAdapter adapter;
    private Button addHitTheEnemyButton;

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

        viewModel = new ViewModelProvider(requireActivity()).get(HitTheEnemyViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(ui.hitTheEnemyListUi.layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addHitTheEnemyButton = view.findViewById(ui.hitTheEnemyListUi.addHitTheEnemyButton);
        recyclerView = view.findViewById(ui.hitTheEnemyListUi.recyclerViewId);
    }

    @Override
    public void onStart() {
        super.onStart();

        addHitTheEnemyButton.setOnClickListener(v -> {
            HitTheEnemyFragment fragment = (HitTheEnemyFragment) getParentFragment();
            fragment.addHitTheEnemy();
        });
    }
}