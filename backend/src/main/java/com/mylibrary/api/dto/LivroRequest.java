package com.mylibrary.api.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LivroRequest(
    @NotBlank(message = "Titulo e obrigatorio")
    @Size(max = 180, message = "Titulo deve ter no maximo 180 caracteres")
    String titulo,

    @NotBlank(message = "Autor e obrigatorio")
    @Size(max = 140, message = "Autor deve ter no maximo 140 caracteres")
    String autor,

    @Size(max = 40, message = "ISBN deve ter no maximo 40 caracteres")
    String isbn,

    @NotNull(message = "Ano e obrigatorio")
    @Min(value = 1, message = "Ano deve ser positivo")
    @Max(value = 2100, message = "Ano invalido")
    Integer ano,

    @NotNull(message = "Categoria e obrigatoria")
    Long categoriaId
) {
}
