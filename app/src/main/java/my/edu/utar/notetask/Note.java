package my.edu.utar.notetask;

import java.io.Serializable;

public class Note implements Serializable {
    private String id;
    private String subject;
    private String content;
    private String imageUrl;
    private long createdDate; // Use long for timestamp
    private String category;

    // Default constructor required for calls to DataSnapshot.getValue(Note.class)
    public Note() {
    }

    public Note(String subject, String content, String category) {
        this.id = id;
        this.subject = subject;
        this.content = content;
        this.createdDate = System.currentTimeMillis(); // Set the current time as timestamp
        this.category = category;
        this.imageUrl = null; // Initialize imageUrl to null
    }
    // Getters
    public String getId() { return id; }
    public String getSubject() { return subject; }
    public String getContent() { return content; }
    public long getCreatedDate() { return createdDate; } // Return the timestamp
    public String getImageUrl() { return imageUrl; }
    public String getCategory() { return category; }

    // Setters
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public void setId(String id) { this.id = id; }
    public void setSubject(String subject) { this.subject = subject; }
    public void setContent(String content) { this.content = content; }
}