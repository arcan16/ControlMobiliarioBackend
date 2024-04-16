package com.mobiliario.dto.email;

import org.springframework.web.multipart.MultipartFile;

public record EmailFileDTO(String[] toUser,
                           String subject,
                           String message,
                           MultipartFile file) {
}
