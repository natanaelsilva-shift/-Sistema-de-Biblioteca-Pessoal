package com.mylibrary.api.repository;

import com.mylibrary.api.entity.Livro;
import com.mylibrary.api.enums.StatusLivro;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LivroRepository extends JpaRepository<Livro, Long> {

    long countByCategoriaId(Long categoriaId);

    long countByStatus(StatusLivro status);

    @EntityGraph(attributePaths = "categoria")
    @Query("""
        select l from Livro l
        where (:categoriaId is null or l.categoria.id = :categoriaId)
          and (:status is null or l.status = :status)
          and (
            :busca is null
            or lower(l.titulo) like lower(concat('%', :busca, '%'))
            or lower(l.autor) like lower(concat('%', :busca, '%'))
          )
        order by l.titulo asc
        """)
    List<Livro> filtrar(
        @Param("categoriaId") Long categoriaId,
        @Param("status") StatusLivro status,
        @Param("busca") String busca
    );
}
