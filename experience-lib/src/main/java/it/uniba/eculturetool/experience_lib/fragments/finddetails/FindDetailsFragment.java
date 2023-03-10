package it.uniba.eculturetool.experience_lib.fragments.finddetails;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.chip.ChipGroup;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import it.uniba.eculturetool.experience_lib.ExperienceDataHolder;
import it.uniba.eculturetool.experience_lib.ExperienceEditorFragment;
import it.uniba.eculturetool.experience_lib.ExperienceViewModel;
import it.uniba.eculturetool.experience_lib.R;
import it.uniba.eculturetool.experience_lib.listeners.OnDataLoadListener;
import it.uniba.eculturetool.experience_lib.models.Coordinate;
import it.uniba.eculturetool.experience_lib.models.Experience;
import it.uniba.eculturetool.experience_lib.models.FindDetails;
import it.uniba.eculturetool.experience_lib.ui.FindDetailsUI;
import it.uniba.eculturetool.experience_lib.utils.ConnectivityUtils;
import it.uniba.eculturetool.tag_lib.tag.model.LanguageTag;
import it.uniba.eculturetool.tag_lib.textmaker.facade.TextMaker;
import it.uniba.eculturetool.tag_lib.viewhelpers.EditingTagViewHelper;
import it.uniba.eculturetool.tag_lib.viewhelpers.LanguageTagViewData;

public class FindDetailsFragment extends Fragment {
    private static final int MARGIN = 20;

    private String operaId;
    private String experienceId;
    private List<LanguageTag> languages;

    private final FindDetailsUI ui = FindDetailsUI.getInstance();
    private EditText messageEditText;
    private ImageButton translateButton;
    private ChipGroup languageChipGroup;
    private ImageView imageView;
    private Bitmap bitmap;
    private Button addImageButton;
    private Button saveButton;

    private final ExperienceDataHolder experienceDataHolder = ExperienceDataHolder.getInstance();
    private FindDetailsViewModel viewModel;
    private ExperienceViewModel experienceViewModel;
    private LanguageTagViewData languageTagViewData;
    private EditingTagViewHelper editingTagViewHelper;
    private ActivityResultLauncher<Intent> pickPhoto;

    public FindDetailsFragment() {}

    public static FindDetailsFragment newInstance(String operaId, String experienceId, List<LanguageTag> languages) {
        FindDetailsFragment fragment = new FindDetailsFragment();
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

        pickPhoto = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if(result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                Uri uri = result.getData().getData();

                imageView.setAlpha(1f);

                try {
                    InputStream inputStream = requireActivity().getContentResolver().openInputStream(uri);
                    viewModel.setImage(BitmapFactory.decodeStream(inputStream));   // Nel metodo di set c'?? l'observer che inserisce l'immagine
                }
                catch(FileNotFoundException ex) {}
            }
        });

        operaId = getArguments().getString(ExperienceEditorFragment.KEY_OPERA_ID);
        experienceId = getArguments().getString(ExperienceEditorFragment.KEY_EXPERIENCE_ID);
        languages = (List) getArguments().getSerializable(ExperienceEditorFragment.KEY_LANGUAGES);

        viewModel = new ViewModelProvider(requireActivity()).get(FindDetailsViewModel.class);
        experienceViewModel = new ViewModelProvider(requireActivity()).get(ExperienceViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(ui.findDetailsFragmentUI.layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        messageEditText = view.findViewById(ui.findDetailsFragmentUI.messageEditText);
        translateButton = view.findViewById(ui.findDetailsFragmentUI.translateImageButton);
        languageChipGroup = view.findViewById(ui.findDetailsFragmentUI.languageChipGroup);
        imageView = view.findViewById(ui.findDetailsFragmentUI.detailsImageView);
        addImageButton = view.findViewById(ui.findDetailsFragmentUI.addImageButton);
        saveButton = view.findViewById(ui.findDetailsFragmentUI.saveButton);

        Set<Experience> experiences = experienceDataHolder.getExperienceByOperaId(operaId);
        if(experiences != null) {
            for(Experience experience : experiences) {
                if(experience.getId().equals(experienceId)) {
                    viewModel.setFindDetails((FindDetails) experience);
                }
            }
        }
        experienceViewModel.setExperience(viewModel.getFindDetails().getValue());

        // Comportamento dei chip
        languageTagViewData = new LanguageTagViewData(Locale.getDefault().getLanguage().toUpperCase());
        editingTagViewHelper = new EditingTagViewHelper(requireContext(), languageTagViewData, messageEditText, translateButton, languageChipGroup);
        if(!viewModel.getFindDetails().getValue().getMessages().isEmpty()) languageTagViewData.setDescriptions(viewModel.getFindDetails().getValue().getMessages());
        for(LanguageTag languageTag : languages) languageTagViewData.addTag(languageTag);
        editingTagViewHelper.setDescriptionEditTextBehavior();
        viewModel.getFindDetails().getValue().setMessages(languageTagViewData.getDescriptions());
        editingTagViewHelper.setSimpleChipGroupBehaviorForLanguages(languages);

        ((OnDataLoadListener) requireActivity()).onDataLoad();
        setImage();
        translation();

        saveButton.setOnClickListener(v -> {
            if(validate()) {
                experienceDataHolder.addExperienceToOpera(operaId, viewModel.getFindDetails().getValue());
                requireActivity().finish();
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setImage() {
        viewModel.getFindDetails().observe(getViewLifecycleOwner(), findDetails ->  {
            imageView.setAlpha(1f);
            Glide.with(requireContext())
                    .load(findDetails.getImage())
                    .apply(RequestOptions.centerCropTransform())
                    .override(imageView.getWidth())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {return false;}

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            // Pulisco le coordinate perch?? non devono essere pi?? disegnate una volta che ?? stata aggiunta una nuova immagine
                            if(bitmap != null) {    // Se bitmap ?? nullo, vuol dire che ?? la prima volta che viene caricata l'immagine
                                findDetails.getCoordinates().clear();
                            }

                            bitmap = ((BitmapDrawable) resource).getBitmap();
                            imageView.setImageBitmap(bitmap);
                            findDetails.setImage(bitmap);

                            List<Coordinate> coordinates = findDetails.getCoordinates();
                            if(findDetails.getImage() != null && coordinates != null && !coordinates.isEmpty()) {    // Il trigger viene chiamato due volte. La prima ?? quando si avvalora l'oggetto find details e la seconda ?? quando viene inserita l'immagine
                                addAllCircle();
                            }

                            return true;
                        }
                    }).into(imageView);
        });

        addImageButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickPhoto.launch(intent);
        });

        imageView.setOnTouchListener((view, motionEvent) -> {
            int x = (int) motionEvent.getX();
            int y = (int) motionEvent.getY();

            if(motionEvent.getAction() == MotionEvent.ACTION_DOWN && viewModel.getFindDetails().getValue().getImage() != null) {
                drawCircle(x, y);
                viewModel.addCoordinate(new Coordinate(x, y));
                return true;
            }

            return false;
        });
    }

    private void drawCircle(int x, int y) {
        bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        Bitmap tempBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);

        Canvas canvas = new Canvas(tempBitmap);
        canvas.drawBitmap(bitmap, 0, 0, null);
        canvas.drawCircle(x, y, MARGIN*2, paint);
        imageView.setImageBitmap(tempBitmap);
    }

    private void addAllCircle() {
        for(Coordinate c : new ArrayList<>(viewModel.getFindDetails().getValue().getCoordinates())) {
            drawCircle(c.getX(), c.getY());
        }
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

            // Se c'?? la connessione ad internet, si pu?? effettuare la traduzione
            for (LanguageTag languageTag : languageTagViewData.getTargetLanguages()) {
                textMaker.generateText(
                        messageEditText.getText().toString(),
                        languageTag,
                        bundle -> {
                            translateButton.setVisibility(View.GONE);
                            viewModel.getFindDetails().getValue().getMessages().put(languageTag.getLanguage(), bundle.getString(languageTag.getLanguage()));
                            languageTagViewData.setDescriptions(viewModel.getFindDetails().getValue().getMessages());
                            Toast.makeText(context, context.getString(it.uniba.eculturetool.tag_lib.R.string.successo_traduzione) + languageTag.getTitle(), Toast.LENGTH_LONG).show();
                        },
                        tag -> Toast.makeText(context, context.getString(it.uniba.eculturetool.tag_lib.R.string.errore_traduzione) + tag.getTitle(), Toast.LENGTH_LONG).show()
                );
            }
        });
    }

    private boolean validate() {
        if(viewModel.getFindDetails().getValue().getMessages().size() < languages.size() || languageTagViewData.areDescriptionsEmpty()) {
            messageEditText.setError(getString(R.string.message_missing));
            messageEditText.requestFocus();
            return false;
        }

        List<Coordinate> coordinates = viewModel.getFindDetails().getValue().getCoordinates();
        if(coordinates == null || coordinates.isEmpty()) {
            Toast.makeText(requireContext(), getString(R.string.details_missing), Toast.LENGTH_LONG).show();
            return false;
        }

        ExperienceEditorFragment fragment = (ExperienceEditorFragment) getChildFragmentManager().findFragmentById(ui.findDetailsFragmentUI.experienceFragmentContainerView);
        return fragment.validate();
    }
}