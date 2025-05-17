package com.eb2.markdownnotetakingapp.dto;

import java.util.List;

public record Response (
        Position position,
        String message,
        List<String> suggestedReplacements
) {
}
