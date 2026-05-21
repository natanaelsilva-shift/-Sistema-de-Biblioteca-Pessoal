package com.mylibrary.api.dto;

public record CategoriaResponse(
    Long id,
    String nome,
    String descricao,
    long quantidadeLivros
) {
}
