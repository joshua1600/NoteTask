package my.edu.utar.notetask;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.view.ContextThemeWrapper;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class NotesListAdapter extends RecyclerView.Adapter<NotesListAdapter.NoteViewHolder> {
    private List<Note> notes;
    private Context context;
    private static final int MAX_CONTENT_LENGTH = 50;

    public NotesListAdapter(List<Note> notes, Context context) {
        this.notes = notes;
        this.context = context;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_note_item, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note currentNote = notes.get(position);
        String truncatedTitle = truncateContent(currentNote.getSubject());
        String truncatedContent = truncateContent(currentNote.getContent());

        holder.textViewTitle.setText(truncatedTitle);
        holder.textViewContent.setText(truncatedContent);

        // Set bookmark icon
        holder.imageViewBookmark.setImageResource(currentNote.isBookmarked() ? R.drawable.nav_bookmark : R.drawable.add_bookmark);

        // Overflow menu click
        holder.imageViewOverflow.setOnClickListener(v -> showPopupMenu(holder, currentNote, position));

        holder.itemView.setOnClickListener(v -> openEditNoteActivity(currentNote));
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    private void showPopupMenu(NoteViewHolder holder, Note currentNote, int position) {
        PopupMenu popupMenu = new PopupMenu(new ContextThemeWrapper(context, R.style.PopupMenuStyle), holder.imageViewOverflow);
        popupMenu.inflate(R.menu.note_overflow_menu);
        popupMenu.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.action_delete) {
                moveToBin(currentNote, position);
                return true;
            } else if (itemId == R.id.action_bookmark) {
                toggleBookmark(currentNote, position);
                return true;
            } else if (itemId == R.id.action_share) {
                shareNote(currentNote);
                return true;
            }
            return false;
        });
        popupMenu.show();
    }

    private void moveToBin(Note note, int position) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference noteRef = FirebaseDatabase.getInstance()
                    .getReference("users").child(userId).child("notes").child(note.getId());
            noteRef.child("deleted").setValue(true);
            noteRef.child("bookmarked").setValue(false)
                    .addOnSuccessListener(aVoid -> {
                        notes.remove(note);
                        notifyItemRemoved(position);
                    })
                    .addOnFailureListener(e -> Toast.makeText(context, "Error deleting note: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }

    private void toggleBookmark(Note note, int position) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference noteRef = FirebaseDatabase.getInstance()
                    .getReference("users").child(userId).child("notes").child(note.getId());
            boolean newBookmarkStatus = !note.isBookmarked();
            noteRef.child("bookmarked").setValue(newBookmarkStatus)
                    .addOnSuccessListener(aVoid -> {
                        note.setBookmarked(newBookmarkStatus);
                        notifyItemChanged(position);
                    })
                    .addOnFailureListener(e -> Toast.makeText(context, "Error updating bookmark: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
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

    private void shareNote(Note note) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain"); // Sharing text data

        // Preparing the note content to share
        String shareBody = "Title: " + note.getSubject() + "\n\nContent: " + note.getContent();
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, note.getSubject());
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);

        // Launch the share intent
        context.startActivity(Intent.createChooser(shareIntent, "Share Note via"));
    }

    private void openEditNoteActivity(Note note) {
        Intent intent = new Intent(context, EditNoteActivity.class);
        intent.putExtra("noteId", note.getId());
        intent.putExtra("noteSubject", note.getSubject());
        intent.putExtra("noteContent", note.getContent());
        intent.putExtra("noteBookmarked", note.isBookmarked());
        context.startActivity(intent);
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle, textViewContent;
        private ImageView imageViewBookmark, imageViewOverflow;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewContent = itemView.findViewById(R.id.text_view_content);
            imageViewBookmark = itemView.findViewById(R.id.image_view_bookmark);
            imageViewOverflow = itemView.findViewById(R.id.image_view_overflow);
        }
    }
}