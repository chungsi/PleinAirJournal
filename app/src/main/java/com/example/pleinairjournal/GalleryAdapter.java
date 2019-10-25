package com.example.pleinairjournal;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder> {
    private Context mContext;
    private List<JournalEntry> mEntriesList;

    private final LayoutInflater mInflater;

    public GalleryAdapter(Context context) {
       mInflater = LayoutInflater.from(context);
       mContext = context;
    }

    public void setEntries(List<JournalEntry> entries) {
        mEntriesList = entries;
        notifyDataSetChanged();
    }

    public void deleteEntry(int position) {

//        notifyItemRemoved(position);
    }

    @NonNull
    @Override
    public GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = mInflater.inflate(R.layout.journal_entry, parent, false);
        return new GalleryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryViewHolder holder, int position) {
        final JournalEntry entry = mEntriesList.get(position);
        holder.text_entryLocation.setText(entry.getLocation());
        holder.text_entryComment.setText(entry.getComment());

        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, ViewEntryActivity.class);
                i.putExtra("id", entry.getId());
                mContext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mEntriesList.size();
    }

    /**
     * ViewHolder
     * */
    public static class GalleryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private LinearLayout mLayout;
        private TextView text_entryLocation, text_entryComment;
        Context mContext;

        public GalleryViewHolder(View v) {
            super(v);
            mLayout = (LinearLayout) v;
            text_entryLocation = mLayout.findViewById(R.id.text_createEntryComment);
            text_entryComment = mLayout.findViewById(R.id.text_createEntryLocation);
            v.setOnClickListener(this);
            mContext = v.getContext();
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(mContext, "Item clicked", Toast.LENGTH_SHORT).show();
        }
    }
}
