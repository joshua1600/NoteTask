package my.edu.utar.notetask;

import android.content.Context;
import android.content.Intent;
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
public class NotesListAdapter extends RecyclerView.Adapter<NotesListAdapter.NoteViewHolder> {
    private List<Note> notes;
    private Context context;
    private static final int MAX_CONTENT_LENGTH = 50; // Adjust this value as needed

    public NotesListAdapter(List<Note> notes, Context context) {
        this.notes = notes;
        this.context = context;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_note_item, parent, false);
        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note currentNote = notes.get(position);
        //Show the title and content until the maximum text length
        String title = currentNote.getSubject();
        String truncatedTitle = truncateContent(title);
        holder.textViewTitle.setText(truncatedTitle);

        String content = currentNote.getContent();
        String truncatedContent = truncateContent(content);
        holder.textViewContent.setText(truncatedContent);


        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditNoteActivity.class);
            intent.putExtra("noteId", currentNote.getId());
            intent.putExtra("noteSubject", currentNote.getSubject());
            intent.putExtra("noteContent", currentNote.getContent());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    private String truncateContent(String content) {
        int newlineIndex = content.indexOf('\n');
        if (newlineIndex != -1 && newlineIndex < MAX_CONTENT_LENGTH) {
            return content.substring(0, newlineIndex) + "...";
        } else if (content.length() > MAX_CONTENT_LENGTH) {
            return content.substring(0, MAX_CONTENT_LENGTH) + "...";
        } else {
            return content;
        }
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewContent;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewContent = itemView.findViewById(R.id.text_view_content);
        }
    }
}
