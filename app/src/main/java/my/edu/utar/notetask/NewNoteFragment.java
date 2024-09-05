package my.edu.utar.notetask;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log; // Import Log class
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NewNoteFragment extends Fragment {
    private Button saveNoteBtn, attachFileButton;
    private TextInputEditText subjectEditText, contentEditText;
    private Uri selectedImageUri; // Store the selected image URI

    private ActivityResultLauncher<Intent> filePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Intent data = result.getData();
                    if (data.getClipData() != null) {
                        // Multiple files selected
                        selectedImageUri = data.getClipData().getItemAt(0).getUri(); // Get the first image
                    } else if (data.getData() != null) {
                        // Single file selected
                        selectedImageUri = data.getData(); // Store the selected image URI
                    }

                    // Log the selected image URI
                    Log.d("NewNoteFragment", "Selected Image URI: " + selectedImageUri.toString());

                    // Show a message indicating the file was selected
                    Toast.makeText(getActivity(), "File attached: " + selectedImageUri.toString(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "No file selected", Toast.LENGTH_SHORT).show();
                }
            }
    );

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_newnote, container, false);

        // Initialize views using the 'view' object
        saveNoteBtn = view.findViewById(R.id.saveNoteBtn);
        attachFileButton = view.findViewById(R.id.attach_files_button);
        subjectEditText = view.findViewById(R.id.subjectEditText);
        contentEditText = view.findViewById(R.id.contentEditText);

        // Set onClickListener for save note button
        saveNoteBtn.setOnClickListener(v -> {
            String subject = subjectEditText.getText().toString();
            String content = contentEditText.getText().toString();

            if (selectedImageUri != null) {
                // Save note with image
                saveNoteWithImage(subject, content, selectedImageUri);
            } else {
                // Save note without image
                saveNote(subject, content, null);
            }
        });

        // Set onClickListener for attach file button
        attachFileButton.setOnClickListener(v -> {
            pickFiles();
        });

        return view;
    }

    // Method to pick files (photos, videos, documents, etc.)
    private void pickFiles() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*"); // Allow only image files
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false); // Allow single file selection
        filePickerLauncher.launch(intent); // Launch the file picker
    }

    private void saveNote(String subject, String content, String imageUrl) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            // Create a new note object
            Note note = new Note(subject, content, imageUrl);

            // Save data to Firebase Realtime Database
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference notesRef = database.getReference("users").child(userId).child("notes");

            // Add note to database (include show feedback)
            notesRef.push().setValue(note, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                    if (error != null) {
                        // If save file fails, show error message
                        Toast.makeText(getActivity(), "Failed to save note: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        // If save successful, show successful message
                        Toast.makeText(getActivity(), "Note saved successfully!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void saveNoteWithImage(String subject, String content, Uri imageUri) {
        if (imageUri != null) {
            // Upload the image to Firebase Storage
            StorageReference storageRef = FirebaseStorage.getInstance().getReference("images/" + UUID.randomUUID().toString());
            storageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Get the download URL of the uploaded image
                        storageRef.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                            // Create a new note with the image URL
                            Note note = new Note(subject, content, downloadUri.toString());

                            // Save the note to Firebase Realtime Database
                            DatabaseReference notesRef = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("notes");
                            notesRef.push().setValue(note, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                    if (error != null) {
                                        Toast.makeText(getActivity(), "Failed to save note: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getActivity(), "Note saved successfully!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        });
                    })
                    .addOnFailureListener(e -> {
                        // Handle any errors during the upload
                        Toast.makeText(getActivity(), "Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(getActivity(), "No image selected.", Toast.LENGTH_SHORT).show();
        }
    }

    class Note {
        private String subject;
        private String content;
        private String imageUrl;

        public Note(String subject, String content, String imageUrl) {
            this.subject = subject;
            this.content = content;
            this.imageUrl = imageUrl;
        }

        public String getSubject() {
            return subject;
        }

        public String getContent() {
            return content;
        }

        public String getImageUrl() {
            return imageUrl;
        }
    }
}