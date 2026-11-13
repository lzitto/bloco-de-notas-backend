package com.leozito.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public class NoteRequest {

    @NotNull(message = "userId é obrigatório") // UUID não pode ser null
    private UUID userId;                        // dono da nota

    @NotBlank(message = "title é obrigatório")
    private String title;

    @NotBlank(message = "content é obrigatório")
    private String content;

    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}