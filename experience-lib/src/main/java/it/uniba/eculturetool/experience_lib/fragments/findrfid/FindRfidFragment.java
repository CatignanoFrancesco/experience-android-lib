package it.uniba.eculturetool.experience_lib.fragments.findrfid;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.chip.ChipGroup;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import it.uniba.eculturetool.experience_lib.ExperienceDataHolder;
import it.uniba.eculturetool.experience_lib.ExperienceEditorFragment;
import it.uniba.eculturetool.experience_lib.ExperienceViewModel;
import it.uniba.eculturetool.experience_lib.R;
import it.uniba.eculturetool.experience_lib.models.Experience;
import it.uniba.eculturetool.experience_lib.models.FindRFID;
import it.uniba.eculturetool.experience_lib.ui.FindRfidUI;
import it.uniba.eculturetool.experience_lib.utils.ConnectivityUtils;
import it.uniba.eculturetool.tag_lib.tag.model.LanguageTag;
import it.uniba.eculturetool.tag_lib.textmaker.facade.TextMaker;
import it.uniba.eculturetool.tag_lib.viewhelpers.EditingTagViewHelper;
import it.uniba.eculturetool.tag_lib.viewhelpers.LanguageTagViewData;

public class FindRfidFragment extends Fragment {
    private final FindRfidUI ui = FindRfidUI.getInstance();

    private String operaId;
    private String experienceId;
    private List<LanguageTag> languages;
    private LanguageTagViewData languageTagViewData;
    private EditingTagViewHelper editingTagViewHelper;
    private FindRfidViewModel viewModel;
    private ExperienceViewModel experienceViewModel;
    private final ExperienceDataHolder dataHolder = ExperienceDataHolder.getInstance();

    private EditText messageEditText;
    private ImageButton translateButton;
    private ChipGroup chipGroup;
    private EditText rfidEditText;
    private Button saveButton;

    public FindRfidFragment() {}

    public static FindRfidFragment newInstance(List<LanguageTag> languages, String operaId, String experienceId) {
        FindRfidFragment fragment = new FindRfidFragment();

        Bundle args = new Bundle();
        args.putString(ExperienceEditorFragment.KEY_OPERA_ID, operaId);
        args.putString(ExperienceEditorFragment.KEY_EXPERIENCE_ID, experienceId);
        args.putSerializable(ExperienceEditorFragment.KEY_LANGUAGES, (Serializable) languages);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Dati dal bundle
        operaId = getArguments().getString(ExperienceEditorFragment.KEY_OPERA_ID);
        experienceId = getArguments().getString(ExperienceEditorFragment.KEY_EXPERIENCE_ID);
        languages = (List) getArguments().getSerializable(ExperienceEditorFragment.KEY_LANGUAGES);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(ui.findRfidFragmentUi.layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Ui
        messageEditText = view.findViewById(ui.findRfidFragmentUi.messageEditText);
        translateButton = view.findViewById(ui.findRfidFragmentUi.translateImageButton);
        chipGroup = view.findViewById(ui.findRfidFragmentUi.languageChipGroup);
        rfidEditText = view.findViewById(ui.findRfidFragmentUi.rfidCodeEditText);
        saveButton = view.findViewById(ui.findRfidFragmentUi.saveButton);

        // Caricamento dei dati
        viewModel = new ViewModelProvider(requireActivity()).get(FindRfidViewModel.class);
        experienceViewModel = new ViewModelProvider(requireActivity()).get(ExperienceViewModel.class);

        Set<Experience> experiences = dataHolder.getExperienceByOperaId(operaId);
        if(experiences != null) {
            for (Experience experience : experiences) {
                if (experience.getId().equals(experienceId)) {
                    viewModel.setFindRfid((FindRFID) experience);
                    break;
                }
            }
        }
        experienceViewModel.setExperience(viewModel.getFindRfid().getValue());

        // Chip
        languageTagViewData = new LanguageTagViewData(Locale.getDefault().getLanguage().toUpperCase());
        editingTagViewHelper = new EditingTagViewHelper(requireContext(), languageTagViewData, messageEditText, translateButton, chipGroup);
        if(!viewModel.getFindRfid().getValue().getMessages().isEmpty()) languageTagViewData.setDescriptions(viewModel.getFindRfid().getValue().getMessages());
        for(LanguageTag languageTag : languages) languageTagViewData.addTag(languageTag);
        editingTagViewHelper.setDescriptionEditTextBehavior();
        viewModel.getFindRfid().getValue().setMessages(languageTagViewData.getDescriptions());
        editingTagViewHelper.setSimpleChipGroupBehaviorForLanguages();

        translation();
        setRfidEditText();

        saveButton.setOnClickListener(v -> {
            if(validate()) {
                dataHolder.addExperienceToOpera(operaId, viewModel.getFindRfid().getValue());
                requireActivity().finish();
            }
        });
    }

    private void translation() {
        // Generazione della traduzione
        Context context = requireContext();
        TextMaker textMaker = TextMaker.getInstance(context.getString(R.string.deepl_auth_key));

        translateButton.setOnClickListener(view -> {
            if (!ConnectivityUtils.isNetworkAvailable(context)) {
                Toast.makeText(context, context.getString(R.string.msg_internet_non_disponibile), Toast.LENGTH_LONG).show();
                return;
            }

            // Se c'è la connessione ad internet, si può effettuare la traduzione
            for (LanguageTag languageTag : languageTagViewData.getTargetLanguages()) {
                textMaker.generateText(
                        messageEditText.getText().toString(),
                        languageTag,
                        bundle -> {
                            translateButton.setVisibility(View.GONE);
                            viewModel.getFindRfid().getValue().getMessages().put(languageTag.getLanguage(), bundle.getString(languageTag.getLanguage()));
                            languageTagViewData.setDescriptions(viewModel.getFindRfid().getValue().getMessages());
                            Toast.makeText(context, context.getString(it.uniba.eculturetool.tag_lib.R.string.successo_traduzione) + languageTag.getTitle(), Toast.LENGTH_LONG).show();
                        },
                        tag -> Toast.makeText(context, context.getString(it.uniba.eculturetool.tag_lib.R.string.errore_traduzione) + tag.getTitle(), Toast.LENGTH_LONG).show()
                );
            }
        });
    }

    private void setRfidEditText() {
        UUID rfidId = viewModel.getFindRfid().getValue().getRfidId();

        if(rfidId != null && !rfidId.toString().isEmpty()) {
            rfidEditText.setText(rfidId.toString());
        }
    }

    private boolean validate() {
        FindRFID fr = viewModel.getFindRfid().getValue();

        if(fr.getMessages().size() < languages.size()) {
            messageEditText.setError(getString(R.string.message_missing));
            messageEditText.requestFocus();
            return false;
        }

        if(rfidEditText.getText().toString().isEmpty()) {
            rfidEditText.setError(getString(R.string.rfid_code_missing));
            rfidEditText.requestFocus();
            return false;
        }

        try {
            fr.setRfidId(UUID.fromString(rfidEditText.getText().toString()));
        }
        catch (IllegalArgumentException e) {
            rfidEditText.setError(getString(R.string.rfid_code_wrong_format));
            rfidEditText.requestFocus();
            return false;
        }

        ExperienceEditorFragment fragment = (ExperienceEditorFragment) getChildFragmentManager().findFragmentById(ui.findRfidFragmentUi.experienceFragmentContainerView);
        return fragment.validate();
    }
}