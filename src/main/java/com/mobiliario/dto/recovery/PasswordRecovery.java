package com.mobiliario.dto.recovery;

import jakarta.validation.constraints.NotNull;

public record PasswordRecovery(@NotNull String username,
                               @NotNull String newPassword,
                               @NotNull String authorization) {
}
