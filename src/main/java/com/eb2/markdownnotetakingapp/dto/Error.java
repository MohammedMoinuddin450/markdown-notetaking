package com.eb2.markdownnotetakingapp.dto;

import org.springframework.http.HttpStatus;

public record Error(
        String message,
        int code,
        HttpStatus status
) {
}
