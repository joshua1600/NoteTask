package my.edu.utar.notetask;

// NoteAdapter.java
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import my.edu.utar.notetask.HomeActivity;
import my.edu.utar.notetask.HomeFragment;
import my.edu.utar.notetask.Note;
import my.edu.utar.notetask.NoteFragment;

public class NotesListAdapter extends RecyclerView.Adapter<NotesListAdapter.NoteViewHolder> {

    private List<Note> notes;
    private Context context;

    public NotesListAdapter(List<Note> notes, Context context) {
        this.notes = notes;
        this.context = context;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = notes.get(position);
        holder.subjectTextView.setText(note.getSubject());

        holder.itemView.setOnClickListener(v -> {
            NoteFragment noteFragment = NoteFragment.newInstance(note);
            ((HomeActivity) context).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, noteFragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView subjectTextView;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectTextView = itemView.findViewById(R.id.noteSubjectTextView);
        }
    }
}
