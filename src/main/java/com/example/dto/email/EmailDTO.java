package com.example.dto.email;

public record EmailDTO(String[] toUser,
                       String subject,
                       String message) {
}
