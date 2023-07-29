package com.facebook.fragment_app;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class SaveFragment extends Fragment {
    private Toolbar toolbar1;
    private ImageButton backButton;
    private EditText editname, editphone, editbio, editaddress, editTextEmail;
    private Button buttonsave, buttonupload;
    private CoordinatorLayout coordinatorLayout;
    private Spinner spinnerGender;
    private ImageView imageView;
    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "my_shared_pref";
    private static final String KEY_NAME = "name";
    private static final String KEY_BIO = "bio";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_GENDER = "gender";

    private static final String KEY_IMAGE_PATH = "image_path";
    private static final int REQUEST_IMAGE_GALLERY = 1000;
    private static final int REQUEST_NOTIFICATION_PERMISSION = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_save, container, false);
        coordinatorLayout = view.findViewById(R.id.coordinator);
        editTextEmail = view.findViewById(R.id.editTextEmail);
        spinnerGender = view.findViewById(R.id.spinnerGender);
        editname = view.findViewById(R.id.editTextName);
        editphone = view.findViewById(R.id.editTextPhone);
        editbio = view.findViewById(R.id.editTextBio);
        editaddress = view.findViewById(R.id.editTextAddress);
        buttonsave = view.findViewById(R.id.buttonSaves);
        buttonupload = view.findViewById(R.id.buttonUploadImage);
        imageView = view.findViewById(R.id.imageView);

        // Set the adapter for the gender spinner
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.gender_options, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(genderAdapter);
        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    buttonsave.setEnabled(false);
                    buttonsave.setTextColor(Color.BLACK);
                } else if (position == 1) {
                    buttonsave.setEnabled(true);
                    buttonsave.setTextColor(Color.WHITE);

                } else if (position == 2) {
                    buttonsave.setEnabled(true);
                    buttonsave.setTextColor(Color.WHITE);

                } else if (position == 3) {
                    buttonsave.setEnabled(true);
                    buttonsave.setTextColor(Color.WHITE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        editphone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed for this implementation
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Not needed for this implementation
            }

            @Override
            public void afterTextChanged(Editable s) {
                String phoneNumber = s.toString();
                if (phoneNumber.length() > 10) {
                    editphone.setError("Phone number cannot exceed 10 digits");
                } else if (!isValidPhoneNumber(phoneNumber)) {
                    editphone.setError("Invalid phone number");
                } else {
                    editphone.setError(null);
                }
            }
        });
        editTextEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                if (input.matches(".*\\d.*") && input.matches(".*[a-zA-Z].*") && input.matches(".*[@].*")) {
                    editTextEmail.setError(null);
                } else {
                    editTextEmail.setError("Please enter a combination of alphanumeric characters with at least one '@' symbol");
                }
            }
        });

        sharedPreferences = requireContext().getSharedPreferences(SHARED_PREF_NAME, requireContext().MODE_PRIVATE);
        backButton = view.findViewById(R.id.backButton);
        toolbar1 = view.findViewById(R.id.toolbarsave);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar1);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to MainActivity
                Intent intent = new Intent(requireContext(), MainActivity.class);
                startActivity(intent);
                requireActivity().finish();
            }
        });

        // Load saved values from SharedPreferences
        editname.setText(sharedPreferences.getString(KEY_NAME, ""));
        editbio.setText(sharedPreferences.getString(KEY_BIO, ""));
        editphone.setText(sharedPreferences.getString(KEY_PHONE, ""));
        editaddress.setText(sharedPreferences.getString(KEY_ADDRESS, ""));
        // Load saved image from SharedPreferences
        String imageBase64 = sharedPreferences.getString(KEY_IMAGE_PATH, "");
        if (!imageBase64.isEmpty()) {
            Bitmap imageBitmap = BitmapFactory.decodeFile(imageBase64);
            imageView.setImageBitmap(imageBitmap);
        }

        createNotificationChannel();

        buttonsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the text from the EditText fields
                String name = editname.getText().toString();
                String bio = editbio.getText().toString();
                String phone = editphone.getText().toString();
                String address = editaddress.getText().toString();
                String email = editTextEmail.getText().toString();
                String gender = spinnerGender.getSelectedItem().toString();

                // Generate a unique identifier for the image file
                String imageFileName = UUID.randomUUID().toString() + ".png";

                // Check if an image is selected
                Bitmap imageBitmap = null;
                if (imageView.getDrawable() != null) {
                    imageBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                }

                // Save the image to a file if imageBitmap is not null
                if (imageBitmap != null) {
                    saveImageToFile(imageBitmap, imageFileName);
                }

                // Create an instance of ImageData with the details
                String imagePath = imageBitmap != null ? requireContext().getFilesDir() + "/" + imageFileName : "";
                ImageData imageData = new ImageData(name, imagePath, bio, phone, address, email, gender);

                // Generate a unique identifier for the data entry
                String uniqueId = UUID.randomUUID().toString();

                // Save the ImageData object to SharedPreferences using the unique key
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(KEY_NAME + "_" + uniqueId, imageData.getName());
                editor.putString(KEY_IMAGE_PATH + "_" + uniqueId, imageData.getImagePath());
                editor.putString(KEY_BIO + "_" + uniqueId, imageData.getBio());
                editor.putString(KEY_PHONE + "_" + uniqueId, imageData.getPhone());
                editor.putString(KEY_ADDRESS + "_" + uniqueId, imageData.getAddress());
                editor.putString(KEY_EMAIL + "_" + uniqueId, imageData.getEmail());
                editor.putString(KEY_GENDER + "_" + uniqueId, imageData.getGender());
                editor.apply();
                //Toast.makeText(save_activity.this, "Data Saved", Toast.LENGTH_SHORT).show();

                notification();
                snacKbar();
                // Introduce a 5-second delay before starting the new activity
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Navigate back to MainActivity
                        Activityfragment fragment1 = new Activityfragment();
                        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragmentContainer, fragment1);
                        fragmentTransaction.commit();
                    }
                }, 4000); // 5000 milliseconds = 5 seconds
            }

        });

        buttonupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch image picker options
                Intent iGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(iGallery, REQUEST_IMAGE_GALLERY);

            }
        });

        return view;
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("[0-9]+") && phoneNumber.length() == 10;
    }


    // Helper method to save the image bitmap to a file
    private void saveImageToFile(Bitmap bitmap, String fileName) {
        File file = new File(requireContext().getFilesDir(), fileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == Activity.RESULT_OK && data != null) {
            Uri imageUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), imageUri);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private Bitmap base64ToBitmap(String base64String) {
        byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public void snacKbar() {
        // Show the Snackbar with the message "Data is saved" and an "Undo" button
        Snackbar snackbar = Snackbar.make(coordinatorLayout, "Data is saved", 3000);
        snackbar.setAction("Undo", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar snackbar1 = Snackbar.make(coordinatorLayout, "Undo Sucessfull", 3000);
                snackbar1.show();
            }
        });
        snackbar.show();
    }

    public void notification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(requireActivity(), "My Notification");
        builder.setContentTitle("DatabaseApp");
        builder.setContentText("Database_Updated");
        builder.setSmallIcon(R.drawable.icon);
        builder.setAutoCancel(true);
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(requireContext());
        if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // Request the notification permission
            ActivityCompat.requestPermissions(requireActivity(), new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, REQUEST_NOTIFICATION_PERMISSION);
            return;
        }
        managerCompat.notify(1, builder.build());
    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("My Notification", "My Notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = requireContext().getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_NOTIFICATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, show the notification
                notification();
            } else {
                // Permission denied, show a message or take appropriate action
                Toast.makeText(requireContext(), "Notification permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}