package com.mylibrary.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoriaRequest(
    @NotBlank(message = "Nome da categoria e obrigatorio")
    @Size(max = 120, message = "Nome deve ter no maximo 120 caracteres")
    String nome,

    @Size(max = 500, message = "Descricao deve ter no maximo 500 caracteres")
    String descricao
) {
}
