package com.mylibrary.api.dto;

import com.mylibrary.api.enums.StatusLivro;

public record LivroResponse(
    Long id,
    String titulo,
    String autor,
    String isbn,
    Integer ano,
    StatusLivro status,
    Long categoriaId,
    String categoriaNome
) {
}
