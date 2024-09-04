package my.edu.utar.notetask;

import java.util.Date;

public class Note {
    private String title;
    private String content;
    private Date createdDate;
    private long fileSize;
    private String category;

    public Note(String title, String content, String category) {
        this.title = title;
        this.content = content;
        this.createdDate = new Date();
        this.fileSize = content.length();
        this.category = category;
    }

    // Getters
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public Date getCreatedDate() { return createdDate; }
    public long getFileSize() { return fileSize; }
    public String getCategory() { return category; }
}