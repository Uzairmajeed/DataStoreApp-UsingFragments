package com.facebook.fragment_app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ExitConfirmationBottomSheet extends BottomSheetDialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exit_confirmation, container, false);

        // Handle interactions or set up the content of the bottom sheet here
        // For example, you can add "Yes" and "No" buttons to confirm the exit.

        view.findViewById(R.id.buttonYes).setOnClickListener(v -> {
            // Exit the app
            requireActivity().finish();
        });

        view.findViewById(R.id.buttonNo).setOnClickListener(v -> {
            // Dismiss the dialog (do nothing)
            dismiss();
        });

        return view;
    }
}

