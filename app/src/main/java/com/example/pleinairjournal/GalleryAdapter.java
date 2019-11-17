package com.example.pleinairjournal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

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
        holder.text_entryDate.setText(entry.getDate());

        Glide.with(mContext)
                .load(entry.getImageFilePath())
                .transition(withCrossFade())
                .into(holder.squareImage_photoPreview);
//                .into(holder.image_photoPreview);
    }

    @Override
    public int getItemCount() {
        return mEntriesList.size();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("PLEINAIR_DEBUG", "Gallery received intent from deleted entry");

        // An intent has been returned that an entry has been deleted
        if (resultCode == Activity.RESULT_OK && requestCode == DELETE_ENTRY) {
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

        private TextView text_entryLocation, text_entryDate, text_entryDaysSinceCreated;
        private ImageView image_photoPreview;
        private SquareImageView squareImage_photoPreview;
        private final Context mContext;

        public GalleryViewHolder(View v, GalleryAdapter adapter) {
            super(v);
            mLayout = (LinearLayout) v;
            mAdapter = adapter;
            mContext = v.getContext();

            text_entryLocation = mLayout.findViewById(R.id.text_galleryEntryLocation);
            text_entryDate = mLayout.findViewById(R.id.text_galleryEntryDate);
            image_photoPreview = mLayout.findViewById(R.id.image_viewPhotoPreview);
            squareImage_photoPreview = mLayout.findViewById(R.id.squareImage_photoPreview);

            v.setOnClickListener(this);
        }

//        public Context getContext() {
//            return mContext;
//        }

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
        }

    }
}
