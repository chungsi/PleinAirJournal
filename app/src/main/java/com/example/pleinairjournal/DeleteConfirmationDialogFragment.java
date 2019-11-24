package com.example.pleinairjournal;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DeleteConfirmationDialogFragment extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_delete_confirmation_dialog, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button button_nevermindDelete = view.findViewById(R.id.button_nevermindDelete);
        Button button_confirmDelete = view.findViewById(R.id.button_confirmDelete);

        button_nevermindDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        button_confirmDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onDialogConfirmDeleteClick();
            }
        });
    }

    /**
     * Interface to communicate with activity implementing this fragment.
     * */
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
