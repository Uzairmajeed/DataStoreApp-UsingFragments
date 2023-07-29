package com.facebook.fragment_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class Activityfragment extends Fragment {
    Button buttonView, buttonSave, buttonDelete;
    private ImageButton closeButton;
    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "my_shared_pref";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activtyfragment, container, false);

        closeButton = view.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the exit confirmation dialog
                ExitConfirmationDialogFragment dialogFragment = new ExitConfirmationDialogFragment();
                dialogFragment.show(requireActivity().getSupportFragmentManager(), "exit_dialog");

                // Show the exit confirmation bottom sheet
               /* ExitConfirmationBottomSheet bottomSheetFragment = new ExitConfirmationBottomSheet();
                bottomSheetFragment.show(requireActivity().getSupportFragmentManager(), bottomSheetFragment.getTag());*/
            }
        });

        buttonView = view.findViewById(R.id.button1);
        buttonSave = view.findViewById(R.id.button2);
        buttonDelete = view.findViewById(R.id.button3);
        sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREF_NAME, MainActivity.MODE_PRIVATE);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    SaveFragment fragment2 = new SaveFragment();
                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentContainer, fragment2);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                } catch (Exception e) {
                    e.printStackTrace(); // Print the stack trace to the console

                    // Display the stack trace in a toast message
                    Toast.makeText(requireContext(), "Error occurred: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        buttonView.setOnClickListener(new View.OnClickListener() {
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

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear all data from SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();

                // Notify the user that the data has been deleted
                Toast.makeText(requireContext(), "All data deleted", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}