package com.mylibrary.api.dto;

import java.time.LocalDate;

public record EmprestimoResponse(
    Long id,
    Long livroId,
    String livroTitulo,
    String livroAutor,
    String nomePessoa,
    String telefone,
    LocalDate dataEmprestimo,
    LocalDate dataDevolucaoPrevista,
    LocalDate dataDevolucaoEfetiva,
    boolean atrasado,
    long diasAtraso
) {
}
