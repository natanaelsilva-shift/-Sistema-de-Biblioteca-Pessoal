package com.mylibrary.api.dto;

import java.time.LocalDate;

public record DevolucaoRequest(
    LocalDate dataDevolucaoEfetiva
) {
}
