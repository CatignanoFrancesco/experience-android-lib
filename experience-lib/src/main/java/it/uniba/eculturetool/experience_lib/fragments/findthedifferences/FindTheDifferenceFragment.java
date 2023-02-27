package it.uniba.eculturetool.experience_lib.fragments.findthedifferences;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.uniba.eculturetool.experience_lib.ExperienceDataHolder;
import it.uniba.eculturetool.experience_lib.ExperienceEditorFragment;
import it.uniba.eculturetool.experience_lib.ExperienceViewModel;
import it.uniba.eculturetool.experience_lib.R;
import it.uniba.eculturetool.experience_lib.listeners.OnDataLoadListener;
import it.uniba.eculturetool.experience_lib.models.Coordinate;
import it.uniba.eculturetool.experience_lib.models.Experience;
import it.uniba.eculturetool.experience_lib.models.FindTheDifference;
import it.uniba.eculturetool.experience_lib.ui.FindTheDifferenceUI;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FindTheDifferenceFragment extends Fragment {
    private static final int MARGIN = 20; // pixel
    private static final int ORIGINAL = 0;
    private static final int DIFFERENT = 1;
    private int currentImgType = 0;

    private final FindTheDifferenceUI ui = FindTheDifferenceUI.getInstance();

    private String operaId;
    private String findTheDifferenceId;

    private ImageView originalImageView, differentImageView;
    private Bitmap differentBitmap;
    private Button addOriginalImageButton, addDifferentImageButton, saveButton;
    private ActivityResultLauncher<Intent> pickPhoto;

    private FindTheDifferenceViewModel ftdViewModel;
    private ExperienceViewModel experienceViewModel;
    private final ExperienceDataHolder dataHolder = ExperienceDataHolder.getInstance();

    public FindTheDifferenceFragment() {}

    public static FindTheDifferenceFragment newInstance(String operaId, String findTheDifferenceId) {
        FindTheDifferenceFragment fragment = new FindTheDifferenceFragment();

        Bundle args = new Bundle();
        args.putString(ExperienceEditorFragment.KEY_EXPERIENCE_ID, findTheDifferenceId);
        args.putString(ExperienceEditorFragment.KEY_OPERA_ID, operaId);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pickPhoto = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if(result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                Uri uri = result.getData().getData();

                ImageView imageView;
                if(currentImgType == ORIGINAL) imageView = originalImageView;
                else imageView = differentImageView;

                imageView.setAlpha(1f);

                try {
                    InputStream inputStream = requireActivity().getContentResolver().openInputStream(uri);
                    if(currentImgType == ORIGINAL) ftdViewModel.setOriginalImage(BitmapFactory.decodeStream(inputStream));  // Nel metodo di set c'è l'observer che inserisce l'immagine
                    else ftdViewModel.setDifferentImage(BitmapFactory.decodeStream(inputStream));
                }
                catch(FileNotFoundException ex) {}
            }
        });

        experienceViewModel = new ViewModelProvider(requireActivity()).get(ExperienceViewModel.class);
        ftdViewModel = new ViewModelProvider(requireActivity()).get(FindTheDifferenceViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(ui.findTheDifferenceFragmentUI.layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.operaId = getArguments().getString(ExperienceEditorFragment.KEY_OPERA_ID);
        this.findTheDifferenceId = getArguments().getString(ExperienceEditorFragment.KEY_EXPERIENCE_ID);

        originalImageView = view.findViewById(ui.findTheDifferenceFragmentUI.originalImageView);
        differentImageView = view.findViewById(ui.findTheDifferenceFragmentUI.differentImageView);
        addOriginalImageButton = view.findViewById(ui.findTheDifferenceFragmentUI.originalAddImageButton);
        addDifferentImageButton = view.findViewById(ui.findTheDifferenceFragmentUI.differentAddImageButton);
        saveButton = view.findViewById(ui.findTheDifferenceFragmentUI.saveButton);

        Set<Experience> experiences = dataHolder.getExperienceByOperaId(operaId);
        if(experiences != null) {
            for(Experience experience : experiences) {
                if(experience.getId().equals(findTheDifferenceId)) {
                    ftdViewModel.setFindTheDifference((FindTheDifference) experience);
                }
            }
        }
        experienceViewModel.setExperience(ftdViewModel.getFindTheDifference().getValue());

        ((OnDataLoadListener) requireActivity()).onDataLoad();
        setOriginalImage();
        setDifferentImage();

        saveButton.setOnClickListener(v -> {
            if(validate()) {
                dataHolder.addExperienceToOpera(operaId, ftdViewModel.getFindTheDifference().getValue());
                requireActivity().finish();
            }
        });
    }

    private void setOriginalImage() {
        ftdViewModel.getFindTheDifference().observe(getViewLifecycleOwner(), findTheDifference -> {
            originalImageView.setAlpha(1f);
            originalImageView.setImageBitmap(findTheDifference.getOriginalImage());
        });

        addOriginalImageButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            currentImgType = ORIGINAL;
            pickPhoto.launch(intent);
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setDifferentImage() {
        ftdViewModel.getFindTheDifference().observe(getViewLifecycleOwner(), findTheDifference ->  {
            differentImageView.setAlpha(1f);
            Glide.with(requireContext())
                    .load(findTheDifference.getDifferentImage())
                    .apply(RequestOptions.centerCropTransform())
                    .override(differentImageView.getWidth())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {return false;}

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            differentBitmap = ((BitmapDrawable) resource).getBitmap();
                            differentImageView.setImageBitmap(differentBitmap);
                            findTheDifference.setDifferentImage(differentBitmap);

                            List<Coordinate> coordinates = findTheDifference.getDifferencesCoordinates();
                            if(findTheDifference.getDifferentImage() != null && coordinates != null && !coordinates.isEmpty()) {    // Il trigger viene chiamato due volte. La prima è quando si avvalora l'oggetto ftd e la seconda è quando viene inserita l'immagine
                                addAllCircle();
                            }

                            return true;
                        }
                    }).into(differentImageView);
        });

        addDifferentImageButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            currentImgType = DIFFERENT;
            pickPhoto.launch(intent);
        });

        differentImageView.setOnTouchListener((view, motionEvent) -> {
            int x = (int) motionEvent.getX();
            int y = (int) motionEvent.getY();

            if(motionEvent.getAction() == MotionEvent.ACTION_DOWN && ftdViewModel.getFindTheDifference().getValue().getDifferentImage() != null) {
                drawCircle(x, y);
                ftdViewModel.addCoordinate(new Coordinate(x, y));
                return true;
            }

            return false;
        });
    }

    private void drawCircle(int x, int y) {
        differentBitmap = ((BitmapDrawable) differentImageView.getDrawable()).getBitmap();
        Bitmap tempBitmap = Bitmap.createBitmap(differentBitmap.getWidth(), differentBitmap.getHeight(), Bitmap.Config.ARGB_8888);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);

        Canvas canvas = new Canvas(tempBitmap);
        canvas.drawBitmap(differentBitmap, 0, 0, null);
        canvas.drawCircle(x, y, MARGIN*2, paint);
        differentImageView.setImageBitmap(tempBitmap);
    }

    private void addAllCircle() {
        for(Coordinate c : new ArrayList<>(ftdViewModel.getFindTheDifference().getValue().getDifferencesCoordinates())) {
            drawCircle(c.getX(), c.getY());
        }
    }

    private boolean validate() {
        if(ftdViewModel.getFindTheDifference().getValue().getOriginalImage() == null) {
            Toast.makeText(requireActivity(), getString(R.string.ftd_original_image_missing), Toast.LENGTH_LONG).show();
            return false;
        }

        if(ftdViewModel.getFindTheDifference().getValue().getDifferentImage() == null) {
            Toast.makeText(requireActivity(), getString(R.string.ftd_different_image_missing), Toast.LENGTH_LONG).show();
            return false;
        }

        List<Coordinate> coordinates = ftdViewModel.getFindTheDifference().getValue().getDifferencesCoordinates();
        if(coordinates == null || coordinates.isEmpty()) {
            Toast.makeText(requireActivity(), getString(R.string.ftd_differnces_missing), Toast.LENGTH_LONG).show();
            return false;
        }

        ExperienceEditorFragment fragment = (ExperienceEditorFragment) getChildFragmentManager().findFragmentById(ui.findTheDifferenceFragmentUI.experienceFragmentContainerView);
        return fragment.validate();
    }
}