package com.example.myapplication.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.myapplication.R;

public class HelpDialog extends AppCompatDialogFragment {

    public HelpDialog() {}


    // This dialog is meant for help purposes.
    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.help_alert, null);

        builder.setView(view)
                .setTitle(R.string.welcome)
                .setPositiveButton("Ok", (dialog, which) -> {});

        TextView prompt = view.findViewById(R.id.prompt);
        prompt.setText(getString(R.string.help_dialog));

        return builder.create();
    }

}
