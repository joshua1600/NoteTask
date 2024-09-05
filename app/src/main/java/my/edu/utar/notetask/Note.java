package my.edu.utar.notetask;

import java.io.Serializable;
import java.util.Date;

public class Note implements Serializable {
    private String subject;
    private String content;
    private Date createdDate;
    private long fileSize;
    private String category;
    private String imageUrl;

    public Note(String subject, String content, String category) {
        this.subject = subject;
        this.content = content;
        this.createdDate = new Date();
        this.fileSize = content.length();
        this.category = category;
        this.imageUrl = imageUrl;
    }

    // Getters
    public String getSubject() { return subject; }
    public String getContent() { return content; }
    public Date getCreatedDate() { return createdDate; }
    public long getFileSize() { return fileSize; }
    public String getCategory() { return category; }
    public String getImageUrl() { return imageUrl; }
}