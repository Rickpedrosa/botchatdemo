package com.example.ricardopedrosarecupandroid.base;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class YesNoDialogFragment extends DialogFragment {
    private static final String ARG_TITLE = "ARG_TITLE";
    private static final String ARG_MESSAGE = "ARG_MESSAGE";
    private static final String ARG_YES_TEXT = "ARG_YES_TEXT";
    private static final String ARG_NO_TEXT = "ARG_NO_TEXT";

    private Listener listener = null;
    private String title;
    private String message;
    private String yesText;
    private String noText;

    @SuppressWarnings("UnusedParameters")
    public interface Listener {
        void onPositiveButtonClick(DialogInterface dialog);

        void onNegativeButtonClick(DialogInterface dialog);
    }

    public static YesNoDialogFragment newInstance(String title, String message, String yesText,
                                                  String noText) {
        YesNoDialogFragment frg = new YesNoDialogFragment();
        Bundle arguments = new Bundle();
        arguments.putString(ARG_TITLE, title);
        arguments.putString(ARG_MESSAGE, message);
        arguments.putString(ARG_YES_TEXT, yesText);
        arguments.putString(ARG_NO_TEXT, noText);
        frg.setArguments(arguments);
        return frg;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        obtainArguments();
        setCancelable(false);
    }

    private void obtainArguments() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            title = arguments.getString(ARG_TITLE);
            message = arguments.getString(ARG_MESSAGE);
            yesText = arguments.getString(ARG_YES_TEXT);
            noText = arguments.getString(ARG_NO_TEXT);
        }
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder b = new AlertDialog.Builder(requireActivity());
        b.setTitle(title);
        b.setMessage(message);
        b.setPositiveButton(yesText, (dialog, which) -> listener.onPositiveButtonClick(dialog));
        b.setNegativeButton(noText, (dialog, which) -> listener.onNegativeButtonClick(dialog));
        return b.create();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }
}
