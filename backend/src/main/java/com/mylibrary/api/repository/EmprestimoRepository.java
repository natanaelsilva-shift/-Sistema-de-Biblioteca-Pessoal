package com.mylibrary.api.repository;

import com.mylibrary.api.entity.Emprestimo;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {

    @EntityGraph(attributePaths = "livro")
    List<Emprestimo> findAllByOrderByDataEmprestimoDescIdDesc();

    @EntityGraph(attributePaths = "livro")
    List<Emprestimo> findByLivroIdOrderByDataEmprestimoDescIdDesc(Long livroId);

    @EntityGraph(attributePaths = "livro")
    List<Emprestimo> findByDataDevolucaoEfetivaIsNullOrderByDataDevolucaoPrevistaAsc();

    @EntityGraph(attributePaths = "livro")
    List<Emprestimo> findByDataDevolucaoEfetivaIsNullAndDataDevolucaoPrevistaBeforeOrderByDataDevolucaoPrevistaAsc(LocalDate data);

    @EntityGraph(attributePaths = "livro")
    List<Emprestimo> findTop5ByOrderByDataEmprestimoDescIdDesc();

    long countByDataDevolucaoEfetivaIsNull();

    Optional<Emprestimo> findByLivroIdAndDataDevolucaoEfetivaIsNull(Long livroId);
}
