package com.example.pleinairjournal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder> {
    private Context mContext;
    private final LayoutInflater mInflater;
    private List<JournalEntry> mEntriesList;
    private final static int DELETE_ENTRY = 1;

    public GalleryAdapter(Context context) {
       mInflater = LayoutInflater.from(context);
       mContext = context;
    }

    public void setEntries(List<JournalEntry> entries) {
        mEntriesList = entries;
        notifyDataSetChanged();
    }

    public JournalEntry getEntryAtPosition(int position) {
        return mEntriesList.get(position);
    }

    public void deleteEntry(int position) {
        mEntriesList.remove(position);
        notifyItemRemoved(position);
    }

    @NonNull
    @Override
    public GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = mInflater.inflate(R.layout.journal_entry, parent, false);
        return new GalleryViewHolder(v, this);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryViewHolder holder, int position) {
        final JournalEntry entry = mEntriesList.get(position);
        holder.text_entryLocation.setText(entry.getLocation());
        holder.text_entryComment.setText(entry.getComment());

        // TODO: remove this code because no longer needed, I think?
        // create a binding to the GalleryViewModel for every RecyclerView item so that we can
        // make changes to the database from within each view
//        GalleryViewModel viewModel = ViewModelProviders.of((FragmentActivity)holder.getContext()).get(GalleryViewModel.class);
//        holder.setViewModel(viewModel);
    }

    @Override
    public int getItemCount() {
        return mEntriesList.size();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("PLEINAIR_DEBUG", "Gallery received intent from deleted entry");
        if (resultCode == Activity.RESULT_OK && requestCode == DELETE_ENTRY) {
            // this should only be called when an activity has been deleted
            int position = data.getIntExtra("position", -1);

            if (position != -1) {
                deleteEntry(position);
            }
        }
    }

    /**
     * ViewHolder
     * */
    public static class GalleryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private LinearLayout mLayout;
        private GalleryAdapter mAdapter;
//        private GalleryViewModel mViewModel;

        private TextView text_entryLocation, text_entryComment;
//        private Button button_deleteEntry;
        private final Context mContext;

        public GalleryViewHolder(View v, GalleryAdapter adapter) {
            super(v);
            mLayout = (LinearLayout) v;
            mAdapter = adapter;
            mContext = v.getContext();

            text_entryLocation = mLayout.findViewById(R.id.text_createEntryComment);
            text_entryComment = mLayout.findViewById(R.id.text_createEntryLocation);
//            button_deleteEntry = mLayout.findViewById(R.id.button_deleteEntry);
//            button_deleteEntry.setOnClickListener(this);

            v.setOnClickListener(this);
        }

//        public void setViewModel(GalleryViewModel viewModel) {
//            mViewModel = viewModel;
//        }

        public Context getContext() {
            return mContext;
        }

        @Override
        public void onClick(View view) {
            JournalEntry entry = mAdapter.getEntryAtPosition(getLayoutPosition());

            if (view.equals(mLayout)) {
                Toast.makeText(mContext, "Item clicked", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(mContext, ViewEntryActivity.class);
                i.putExtra("position", getLayoutPosition());
                i.putExtra("id", entry.getId());
                ((Activity)mContext).startActivityForResult(i, DELETE_ENTRY);
            }

//            if (view.equals(button_deleteEntry)) {
//                Toast.makeText(mContext, "item should delete!", Toast.LENGTH_SHORT).show();
//                mAdapter.deleteEntry(getLayoutPosition());
//                mViewModel.deleteEntry(entry);
//            }
        }

    }
}
