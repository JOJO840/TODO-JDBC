package org.lexicon.model;

import java.time.LocalDate;

public class TodoItem {

    private int id;
    private String title;
    private String description;
    private LocalDate deadline;
    private boolean done;
    private int assigneeId;

    public TodoItem(String title, String description, LocalDate deadline, boolean done, int assigneeId) {
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.done = done;
        this.assigneeId = assigneeId;
    }

    public TodoItem(int id, String title, String description, LocalDate deadline, boolean done, int assigneeId) {
        this(title, description, deadline, done, assigneeId);
        this.id = id;
    }

    // Getters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public LocalDate getDeadline() { return deadline; }
    public boolean isDone() { return done; }
    public int getAssigneeId() { return assigneeId; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }
    public void setDone(boolean done) { this.done = done; }
    public void setAssigneeId(int assigneeId) { this.assigneeId = assigneeId; }

    @Override
    public String toString() {
        return "TodoItem{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", deadline=" + deadline +
                ", done=" + done +
                ", assigneeId=" + assigneeId +
                '}';
    }
}
