package com.facebook.fragment_app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class DetailsFragment extends Fragment {
    private Toolbar toolbar1;
    private ImageButton backButton;
    private ImageView imageView;
    private TextView textViewName, textViewBio, textViewPhone, textViewAddress;
    private TextView textViewEmail;
    private TextView textViewGender;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        imageView = view.findViewById(R.id.imageView);
        textViewName = view.findViewById(R.id.textViewName);
        textViewBio = view.findViewById(R.id.textViewBio);
        textViewPhone = view.findViewById(R.id.textViewPhone);
        textViewAddress = view.findViewById(R.id.textViewAddress);
        textViewEmail = view.findViewById(R.id.textViewEmail);
        textViewGender = view.findViewById(R.id.textViewGender);
        backButton = view.findViewById(R.id.backButton);
        toolbar1 = view.findViewById(R.id.toolbardetails);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar1);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewFragment fragment3 = new ViewFragment();
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainer, fragment3);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        // Retrieve the data passed from ViewFragment
        Bundle arguments = getArguments();
        if (arguments != null) {
            String name = arguments.getString("name");
            String bio = arguments.getString("bio");
            String phone = arguments.getString("phone");
            String address = arguments.getString("address");
            String email = arguments.getString("email");
            String gender = arguments.getString("gender");

            // Set the text details
            textViewName.setText("Name: " + name);
            textViewBio.setText("Bio: " + bio);
            textViewPhone.setText("Phone No: " + phone);
            textViewAddress.setText("Address: " + address);
            textViewEmail.setText("Email: " + email);
            textViewGender.setText("Gender: " + gender);

            // Retrieve the image path from the intent extras
            String imagePath = arguments.getString("imagePath");

            if (imagePath == null || imagePath.isEmpty()) {
                imageView.setImageResource(R.drawable.image2); // Set the default image resource
            } else {
                Bitmap imageBitmap = BitmapFactory.decodeFile(imagePath);
                if (imageBitmap != null) {
                    imageView.setImageBitmap(imageBitmap);
                } else {
                    imageView.setImageResource(R.drawable.image2); // Set the default image resource
                }
            }
        }

        return view;
    }
}