package com.facebook.fragment_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ViewFragment extends Fragment implements ImageAdapter.OnItemClickListener {
    private ImageButton backButton;
    private RecyclerView recyclerViewMen;
    private RecyclerView recyclerViewWomen;
    private RecyclerView recyclerViewOthers;
    private ImageAdapter imageAdapterMen;
    private ImageAdapter imageAdapterWomen;
    private ImageAdapter imageAdapterOthers;
    private List<ImageData> imageDataListMen;
    private List<ImageData> imageDataListWomen;
    private List<ImageData> imageDataListOthers;
    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "my_shared_pref";
    private static final String KEY_NAME = "name";
    private static final String KEY_IMAGE_PATH = "image_path";
    private static final String KEY_BIO = "bio";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_ADDRESS = "address";
    private static final int GRID_SPAN_COUNT_PORTRAIT = 3;
    private static final int GRID_SPAN_COUNT_LANDSCAPE = 6;

    private Spinner spinnerLayoutStyle;
    private int gridSpanCount = 3;

    private boolean isGridBased = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view, container, false);

        backButton = view.findViewById(R.id.backButton);
        spinnerLayoutStyle = view.findViewById(R.id.spinnerLayoutStyle);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to MainActivity
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                requireActivity().finish();
            }
        });

        recyclerViewMen = view.findViewById(R.id.recyclerViewMen);
        recyclerViewWomen = view.findViewById(R.id.recyclerViewWomen);
        recyclerViewOthers = view.findViewById(R.id.recyclerViewOthers);




        sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREF_NAME, requireActivity().MODE_PRIVATE);

        imageDataListMen = new ArrayList<>();
        imageDataListWomen = new ArrayList<>();
        imageDataListOthers = new ArrayList<>();

        imageAdapterMen = new ImageAdapter(requireContext(), imageDataListMen, isGridBased);
        imageAdapterMen.setOnItemClickListener(this); // Set the listener to handle click events
        recyclerViewMen.setAdapter(imageAdapterMen);

        imageAdapterWomen = new ImageAdapter(requireContext(), imageDataListWomen, isGridBased);
        imageAdapterWomen.setOnItemClickListener(this); // Set the listener to handle click events
        recyclerViewWomen.setAdapter(imageAdapterWomen);

        imageAdapterOthers = new ImageAdapter(requireContext(), imageDataListOthers, isGridBased);
        imageAdapterOthers.setOnItemClickListener(this); // Set the listener to handle click events
        recyclerViewOthers.setAdapter(imageAdapterOthers);


        Map<String, ?> allEntries = sharedPreferences.getAll();

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String key = entry.getKey();
            if (key.startsWith(KEY_NAME)) {
                String name = sharedPreferences.getString(key, "");
                String imagePathKey = KEY_IMAGE_PATH + key.substring(KEY_NAME.length());
                String imagePath = sharedPreferences.getString(imagePathKey, "");
                String bioKey = KEY_BIO + key.substring(KEY_NAME.length());
                String bio = sharedPreferences.getString(bioKey, "");
                String phoneKey = KEY_PHONE + key.substring(KEY_NAME.length());
                String phone = sharedPreferences.getString(phoneKey, "");
                String addressKey = KEY_ADDRESS + key.substring(KEY_NAME.length());
                String address = sharedPreferences.getString(addressKey, "");
                String emailKey = KEY_EMAIL + key.substring(KEY_NAME.length());
                String email = sharedPreferences.getString(emailKey, "");
                String genderKey = KEY_GENDER + key.substring(KEY_NAME.length());
                String gender = sharedPreferences.getString(genderKey, "");

                ImageData imageData = new ImageData(name, imagePath, bio, phone, address, email, gender);

                if (gender.equalsIgnoreCase("male")) {
                    imageDataListMen.add(imageData);
                } else if (gender.equalsIgnoreCase("female")) {
                    imageDataListWomen.add(imageData);
                } else {
                    imageDataListOthers.add(imageData);
                }
            }
        }

        setupLayoutStyleSpinner();
        setLayoutStyle(isGridBased);

        if (imageDataListMen.isEmpty() && imageDataListWomen.isEmpty() && imageDataListOthers.isEmpty()) {
            Toast.makeText(requireActivity(), "No data available", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    // Rest of the code remains the same as in the original ViewActivity class
    // ...
    public void navigateToDetailsFragment(ImageData imageData) {
        DetailsFragment detailsFragment = new DetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("name", imageData.getName());
        bundle.putString("bio", imageData.getBio());
        bundle.putString("phone", imageData.getPhone());
        bundle.putString("address", imageData.getAddress());
        bundle.putString("email", imageData.getEmail());
        bundle.putString("gender", imageData.getGender());
        bundle.putString("imagePath", imageData.getImagePath());
        detailsFragment.setArguments(bundle);

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, detailsFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    private void setupLayoutStyleSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(), R.array.layout_style_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLayoutStyle.setAdapter(adapter);
        // Retrieve the saved spinner position from SharedPreferences
        int spinnerPosition = sharedPreferences.getInt("spinner_position", spinnerLayoutStyle.getSelectedItemPosition());
        spinnerLayoutStyle.setSelection(spinnerPosition);
        spinnerLayoutStyle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    // No action needed for the first item
                } else if (position == 1) {
                    setLayoutStyleForHorizontalList();
                } else if (position == 2) {
                    // Update the layout style based on the spinner selection
                    isGridBased = true; // Position 1 corresponds to "Grid-based"
                    setGridSpanCount(); // Set the grid span count based on the isGridBased flag
                    setLayoutStyle(isGridBased);
                } else if (position == 3) {
                    // Update the layout style based on the spinner selection
                    isGridBased = false; // Position 3 corresponds to "List-based"
                    setLayoutStyleForListBased();
                }
                // Save the selected position in SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("spinner_position", position);
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setLayoutStyleForListBased() {
        // Use ListBasedImageAdapter for the "List-based" option
        ListBasedImageAdapter listBasedImageAdapterMen = new ListBasedImageAdapter(requireContext(), imageDataListMen);
        ListBasedImageAdapter listBasedImageAdapterWomen = new ListBasedImageAdapter(requireContext(), imageDataListWomen);
        ListBasedImageAdapter listBasedImageAdapterOthers = new ListBasedImageAdapter(requireContext(), imageDataListOthers);

        // Set item click listener for the ListBasedImageAdapters
        listBasedImageAdapterMen.setOnItemClickListener(new ListBasedImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ImageData imageData) {
                navigateToDetailsFragment(imageData);
            }
        });

        listBasedImageAdapterWomen.setOnItemClickListener(new ListBasedImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ImageData imageData) {
                navigateToDetailsFragment(imageData);
            }
        });

        listBasedImageAdapterOthers.setOnItemClickListener(new ListBasedImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ImageData imageData) {
                navigateToDetailsFragment(imageData);
            }
        });

        recyclerViewMen.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        recyclerViewWomen.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        recyclerViewOthers.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));

        recyclerViewMen.setAdapter(listBasedImageAdapterMen);
        recyclerViewWomen.setAdapter(listBasedImageAdapterWomen);
        recyclerViewOthers.setAdapter(listBasedImageAdapterOthers);
    }
    public void setGridSpanCount() {
        int spanCount = isGridBased ? (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT
                ? GRID_SPAN_COUNT_PORTRAIT : GRID_SPAN_COUNT_LANDSCAPE) : 1;

        this.gridSpanCount = spanCount;
        imageAdapterMen.setGridSpanCount(spanCount);
        imageAdapterWomen.setGridSpanCount(spanCount);
        imageAdapterOthers.setGridSpanCount(spanCount);

        recyclerViewMen.setLayoutManager(new GridLayoutManager(requireContext(), spanCount));
        recyclerViewWomen.setLayoutManager(new GridLayoutManager(requireContext(), spanCount));
        recyclerViewOthers.setLayoutManager(new GridLayoutManager(requireContext(), spanCount));
    }
    private void setLayoutStyle(boolean isGridBased) {
        this.isGridBased = isGridBased;

        if (isGridBased) {
            setGridSpanCount();

            // Set GridLayoutManager for the RecyclerViews
            GridLayoutManager gridLayoutManagerMen = new GridLayoutManager(requireContext(), gridSpanCount);
            GridLayoutManager gridLayoutManagerWomen = new GridLayoutManager(requireContext(), gridSpanCount);
            GridLayoutManager gridLayoutManagerOthers = new GridLayoutManager(requireContext(), gridSpanCount);

            recyclerViewMen.setLayoutManager(gridLayoutManagerMen);
            recyclerViewWomen.setLayoutManager(gridLayoutManagerWomen);
            recyclerViewOthers.setLayoutManager(gridLayoutManagerOthers);

            // Use the ImageAdapter for Grid-based layout
            if (imageAdapterMen == null) {
                imageAdapterMen = new ImageAdapter(requireActivity(), imageDataListMen, isGridBased);
                recyclerViewMen.setAdapter(imageAdapterMen);
            } else {
                imageAdapterMen.setGridBased(isGridBased);
                recyclerViewMen.setAdapter(imageAdapterMen);
            }

            if (imageAdapterWomen == null) {
                imageAdapterWomen = new ImageAdapter(requireContext(), imageDataListWomen, isGridBased);
                recyclerViewWomen.setAdapter(imageAdapterWomen);
            } else {
                imageAdapterWomen.setGridBased(isGridBased);
                recyclerViewWomen.setAdapter(imageAdapterWomen);
            }

            if (imageAdapterOthers == null) {
                imageAdapterOthers = new ImageAdapter(requireContext(), imageDataListOthers, isGridBased);
                recyclerViewOthers.setAdapter(imageAdapterOthers);
            } else {
                imageAdapterOthers.setGridBased(isGridBased);
                recyclerViewOthers.setAdapter(imageAdapterOthers);
            }
        } else {
            // Set LinearLayoutManager for the RecyclerViews
            LinearLayoutManager layoutManagerMen = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
            LinearLayoutManager layoutManagerWomen = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
            LinearLayoutManager layoutManagerOthers = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);

            recyclerViewMen.setLayoutManager(layoutManagerMen);
            recyclerViewWomen.setLayoutManager(layoutManagerWomen);
            recyclerViewOthers.setLayoutManager(layoutManagerOthers);

            // Use the ImageAdapter for List-based layout
            if (imageAdapterMen == null) {
                imageAdapterMen = new ImageAdapter(requireContext(), imageDataListMen, isGridBased);
                recyclerViewMen.setAdapter(imageAdapterMen);
            } else {
                imageAdapterMen.setGridBased(isGridBased);
                recyclerViewMen.setAdapter(imageAdapterMen);
            }

            if (imageAdapterWomen == null) {
                imageAdapterWomen = new ImageAdapter(requireContext(), imageDataListWomen, isGridBased);
                recyclerViewWomen.setAdapter(imageAdapterWomen);
            } else {
                imageAdapterWomen.setGridBased(isGridBased);
                recyclerViewWomen.setAdapter(imageAdapterWomen);
            }

            if (imageAdapterOthers == null) {
                imageAdapterOthers = new ImageAdapter(requireContext(), imageDataListOthers, isGridBased);
                recyclerViewOthers.setAdapter(imageAdapterOthers);
            } else {
                imageAdapterOthers.setGridBased(isGridBased);
                recyclerViewOthers.setAdapter(imageAdapterOthers);
            }
        }
    }

    private void updateLayoutStyle(boolean isGridBased) {
        setLayoutStyle(isGridBased);

        // Get the selected position of the spinner
        int spinnerPosition = spinnerLayoutStyle.getSelectedItemPosition();

        if (spinnerPosition == 1) {
            // If the selected position is 1 (List-based), update the layout to show images in a horizontal list
            setLayoutStyleForHorizontalList();
        } else if (isGridBased) {
            // For grid-based layout, we need to adjust the grid span count based on orientation
            setGridSpanCount();
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                setGridLayoutForPortrait();
            } else {
                setGridLayoutForLandscape();
            }
        } else if (!isGridBased) {
            setLayoutStyleForListBased();
        }
        else {}
    }


    private void setGridLayoutForPortrait() {
        int spanCountPortrait = GRID_SPAN_COUNT_PORTRAIT;
        recyclerViewMen.setLayoutManager(new GridLayoutManager(requireContext(), spanCountPortrait));
        recyclerViewWomen.setLayoutManager(new GridLayoutManager(requireContext(), spanCountPortrait));
        recyclerViewOthers.setLayoutManager(new GridLayoutManager(requireContext(), spanCountPortrait));

        // Use the ImageAdapter for Grid-based layout
        if (imageAdapterMen == null) {
            imageAdapterMen = new ImageAdapter(requireContext(), imageDataListMen, isGridBased);
            recyclerViewMen.setAdapter(imageAdapterMen);
        } else {
            imageAdapterMen.setGridBased(isGridBased);
            recyclerViewMen.setAdapter(imageAdapterMen);
        }

        if (imageAdapterWomen == null) {
            imageAdapterWomen = new ImageAdapter(requireContext(), imageDataListWomen, isGridBased);
            recyclerViewWomen.setAdapter(imageAdapterWomen);
        } else {
            imageAdapterWomen.setGridBased(isGridBased);
            recyclerViewWomen.setAdapter(imageAdapterWomen);
        }

        if (imageAdapterOthers == null) {
            imageAdapterOthers = new ImageAdapter(requireContext(), imageDataListOthers, isGridBased);
            recyclerViewOthers.setAdapter(imageAdapterOthers);
        } else {
            imageAdapterOthers.setGridBased(isGridBased);
            recyclerViewOthers.setAdapter(imageAdapterOthers);
        }
    }

    private void setGridLayoutForLandscape() {
        int spanCountLandscape = GRID_SPAN_COUNT_LANDSCAPE;
        recyclerViewMen.setLayoutManager(new GridLayoutManager(requireContext(), spanCountLandscape));
        recyclerViewWomen.setLayoutManager(new GridLayoutManager(requireContext(), spanCountLandscape));
        recyclerViewOthers.setLayoutManager(new GridLayoutManager(requireContext(), spanCountLandscape));

        // Use the ImageAdapter for Grid-based layout
        if (imageAdapterMen == null) {
            imageAdapterMen = new ImageAdapter(requireContext(), imageDataListMen, isGridBased);
            recyclerViewMen.setAdapter(imageAdapterMen);
        } else {
            imageAdapterMen.setGridBased(isGridBased);
            recyclerViewMen.setAdapter(imageAdapterMen);
        }

        if (imageAdapterWomen == null) {
            imageAdapterWomen = new ImageAdapter(requireContext(), imageDataListWomen, isGridBased);
            recyclerViewWomen.setAdapter(imageAdapterWomen);
        } else {
            imageAdapterWomen.setGridBased(isGridBased);
            recyclerViewWomen.setAdapter(imageAdapterWomen);
        }

        if (imageAdapterOthers == null) {
            imageAdapterOthers = new ImageAdapter(requireContext(), imageDataListOthers, isGridBased);
            recyclerViewOthers.setAdapter(imageAdapterOthers);
        } else {
            imageAdapterOthers.setGridBased(isGridBased);
            recyclerViewOthers.setAdapter(imageAdapterOthers);
        }
    }


    // New method to display images in a horizontal list
    private void setLayoutStyleForHorizontalList() {
        // Check if the screen orientation is in landscape mode
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE||getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            // Set LinearLayoutManager for the RecyclerViews
            LinearLayoutManager layoutManagerMen = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
            LinearLayoutManager layoutManagerWomen = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
            LinearLayoutManager layoutManagerOthers = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);

            recyclerViewMen.setLayoutManager(layoutManagerMen);
            recyclerViewWomen.setLayoutManager(layoutManagerWomen);
            recyclerViewOthers.setLayoutManager(layoutManagerOthers);

            // Use the ImageAdapter for List-based layout
            if (imageAdapterMen == null) {
                imageAdapterMen = new ImageAdapter(requireContext(), imageDataListMen, isGridBased);
                recyclerViewMen.setAdapter(imageAdapterMen);
            } else {
                imageAdapterMen.setGridBased(isGridBased);
                recyclerViewMen.setAdapter(imageAdapterMen);
            }

            if (imageAdapterWomen == null) {
                imageAdapterWomen = new ImageAdapter(requireContext(), imageDataListWomen, isGridBased);
                recyclerViewWomen.setAdapter(imageAdapterWomen);
            } else {
                imageAdapterWomen.setGridBased(isGridBased);
                recyclerViewWomen.setAdapter(imageAdapterWomen);
            }

            if (imageAdapterOthers == null) {
                imageAdapterOthers = new ImageAdapter(requireContext(), imageDataListOthers, isGridBased);
                recyclerViewOthers.setAdapter(imageAdapterOthers);
            } else {
                imageAdapterOthers.setGridBased(isGridBased);
                recyclerViewOthers.setAdapter(imageAdapterOthers);
            }
        }
    }




    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (isGridBased) {
            updateLayoutStyle(isGridBased);
        }
    }

    @Override
    public void onItemClick(ImageData imageData) {
        navigateToDetailsFragment(imageData);
    }
}
