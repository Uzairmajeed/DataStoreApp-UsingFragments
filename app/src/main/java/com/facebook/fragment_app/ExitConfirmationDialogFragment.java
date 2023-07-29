package com.facebook.fragment_app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class ExitConfirmationDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), R.style.CustomDialogTheme);
        builder.setTitle("Confirm Exit")
                .setMessage("Do you want to exit?");

        // Set the positive button ("Yes") with custom text color
        builder.setPositiveButton("Yes", (dialog, which) -> {
            // Exit the app
            requireActivity().finish();
        });

        // Set the negative button ("No") with custom text color
        builder.setNegativeButton("No", (dialog, which) -> {
            // Dismiss the dialog (do nothing)
            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();

        // Set the text color for both "Yes" and "No" buttons
        dialog.setOnShowListener(dialogInterface -> {
            Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
            positiveButton.setTextColor(getResources().getColor(R.color.custom_dialog_button_text));

            Button negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
            negativeButton.setTextColor(getResources().getColor(R.color.custom_dialog_button_text));
        });

        return dialog;
    }
}
