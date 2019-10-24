package com.example.pleinairjournal;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
    private Cursor mCursor;
    private List<JournalEntry> mEntriesList;

    public GalleryAdapter(Context context, List<JournalEntry> entriesList) {
        mContext = context;
        mEntriesList = entriesList;
    }

    /**
     * For now, the adapter accepts a Cursor object, which is the result of a query
     * */
//    public GalleryAdapter(Context context, Cursor cursor) {
//        mContext = context;
//        mCursor = cursor;
//    }

    @NonNull
    @Override
    public GalleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.journal_entry, parent, false);
        return new GalleryViewHolder(v);
    }

    /**
     * TODO: Replace using a raw Cursor with CursorAdapter?
     * Not too sure, but because we need to release the cursor at some point, just using the raw
     * Cursor is unreliable and may be prone to memory leaks... Can't seem to find anything on the
     * Android guides yet...
     * */
    @Override
    public void onBindViewHolder(@NonNull GalleryViewHolder holder, int position) {
//        mCursor.moveToPosition(position);
//
//        Log.i("PLEINAIR_DEBUG", "Position: " + position);
//        Log.i("PLEINAIR_DEBUG", "Location: " + mCursor.getString(mCursor.getColumnIndexOrThrow(JournalEntry.LOCATION)));
//
//        holder.text_entryLocation.setText(mCursor.getString(
//                mCursor.getColumnIndexOrThrow(JournalEntry.LOCATION)));
//        holder.text_entryComment.setText(mCursor.getString(
//                mCursor.getColumnIndexOrThrow(JournalEntry.COMMENT)));

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
//        return mCursor.getCount();
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
