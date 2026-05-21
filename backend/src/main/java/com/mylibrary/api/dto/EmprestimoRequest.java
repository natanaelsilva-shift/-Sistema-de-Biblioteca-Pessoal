package com.mylibrary.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record EmprestimoRequest(
    @NotNull(message = "Livro e obrigatorio")
    Long livroId,

    @NotBlank(message = "Nome da pessoa e obrigatorio")
    @Size(max = 140, message = "Nome da pessoa deve ter no maximo 140 caracteres")
    String nomePessoa,

    @Size(max = 30, message = "Telefone deve ter no maximo 30 caracteres")
    String telefone,

    @NotNull(message = "Data do emprestimo e obrigatoria")
    LocalDate dataEmprestimo,

    @NotNull(message = "Data prevista de devolucao e obrigatoria")
    LocalDate dataDevolucaoPrevista
) {
}
