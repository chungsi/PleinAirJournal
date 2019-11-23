package com.example.pleinairjournal;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DeleteConfirmationDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(R.string.dialog_delete_entry_message)
                .setTitle(R.string.dialog_delete_entry_title);
        builder.setNegativeButton(R.string.dialog_delete_entry_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //
                dismiss();
            }
        });
        builder.setPositiveButton(R.string.dialog_delete_entry_confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //
                listener.onDialogConfirmDeleteClick();
            }
        });

        return builder.create();
    }

    public interface DeleteConfirmationDialogListener {
        void onDialogConfirmDeleteClick();
    }

    DeleteConfirmationDialogListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (DeleteConfirmationDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " must implement DeleteConfirmationDialogListener");
        }
    }
}
