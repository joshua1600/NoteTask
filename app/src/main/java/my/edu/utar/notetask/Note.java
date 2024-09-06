package my.edu.utar.notetask;

import java.io.Serializable;
import java.util.Date;

public class Note implements Serializable {
    private String subject;
    private String content;
    private Date createdDate;
    private long fileSize;
    private String category;

    public Note() {
        // Default constructor required for calls to DataSnapshot.getValue(Note.class)
    }

    public Note(String subject, String content, String category) {
        this.subject = subject;
        this.content = content;
        this.createdDate = new Date();
        this.fileSize = content.length();
        this.category = category;
    }

    // Getters
    public String getSubject() { return subject; }
    public String getContent() { return content; }
    public Date getCreatedDate() { return createdDate; }
    public long getFileSize() { return fileSize; }
    public String getCategory() { return category; }

    // Setters
    public void setSubject(String subject) { this.subject = subject; }
    public void setContent(String content) { this.content = content; }
    public void setCreatedDate(Date createdDate) { this.createdDate = createdDate; }
    public void setFileSize(long fileSize) { this.fileSize = fileSize; }
    public void setCategory(String category) { this.category = category; }
}
