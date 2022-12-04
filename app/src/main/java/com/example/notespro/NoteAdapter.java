package com.example.notespro;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.text.BreakIterator;

public class NoteAdapter extends FirestoreRecyclerAdapter<Note, NoteAdapter.noteViewHolder> {
    Context context;
    public NoteAdapter(@NonNull FirestoreRecyclerOptions<Note> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull noteViewHolder holder, int position, @NonNull Note note) {
        holder.titleTextView.setText(note.title);
        holder.contentTextView.setText(note.content);
        //holder.timestampTextView.setText(Utility.timestampToString(note.timestamp));
        holder.itemView.setOnClickListener((v)->{
        Intent intent = new Intent(context, NoteDetails.class);
        intent.putExtra("title", note.title);
        intent.putExtra("content", note.content);
        String docId = this.getSnapshots().getSnapshot(position).getId();
        intent.putExtra("docId", docId);
        context.startActivity(intent);

        });


    }

    @NonNull
    @Override
    public noteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_note_item, parent, false);
        return new noteViewHolder(view);

    }

    class noteViewHolder extends RecyclerView.ViewHolder {

        public BreakIterator timestampTextView;
        TextView titleTextView, contentTextView;

        public noteViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.notes_title_text_view);
            contentTextView = itemView.findViewById(R.id.notes_content_text_view);
            //titleTextView = itemView.findViewById(R.id.notes_timestamp_text_view);

        }
    }
}
