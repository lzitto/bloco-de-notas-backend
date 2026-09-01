package com.leozito.domain;

import java.util.UUID;

public class Note {
    private UUID id;
    private String text;
    private UUID userId;

    public Note() {
        this.id = UUID.randomUUID();
    }

    public Note(String text, UUID userId) {
        this.id = UUID.randomUUID();
        this.text = text;
        this.userId = userId;
    }

    public UUID getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}