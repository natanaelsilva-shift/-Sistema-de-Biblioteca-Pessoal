package com.mylibrary.api.service;

import com.mylibrary.api.dto.LivroRequest;
import com.mylibrary.api.dto.LivroResponse;
import com.mylibrary.api.entity.Categoria;
import com.mylibrary.api.entity.Livro;
import com.mylibrary.api.enums.StatusLivro;
import com.mylibrary.api.exception.BusinessException;
import com.mylibrary.api.exception.ResourceNotFoundException;
import com.mylibrary.api.repository.LivroRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LivroService {

    private final LivroRepository livroRepository;
    private final CategoriaService categoriaService;

    public LivroService(LivroRepository livroRepository, CategoriaService categoriaService) {
        this.livroRepository = livroRepository;
        this.categoriaService = categoriaService;
    }

    @Transactional(readOnly = true)
    public List<LivroResponse> listar(Long categoriaId, StatusLivro status, String busca) {
        String buscaNormalizada = normalizarFiltro(busca);
        return livroRepository.filtrar(categoriaId, status, buscaNormalizada).stream()
            .map(this::toResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    public LivroResponse buscar(Long id) {
        return toResponse(buscarEntity(id));
    }

    @Transactional
    public LivroResponse criar(LivroRequest request) {
        Categoria categoria = categoriaService.buscarEntity(request.categoriaId());
        Livro livro = new Livro(
            request.titulo().trim(),
            request.autor().trim(),
            trimToNull(request.isbn()),
            request.ano(),
            categoria
        );
        livro.setStatus(StatusLivro.DISPONIVEL);

        return toResponse(livroRepository.save(livro));
    }

    @Transactional
    public void excluir(Long id) {
        Livro livro = buscarEntity(id);

        if (livro.getStatus() == StatusLivro.EMPRESTADO) {
            throw new BusinessException("So e permitido excluir livro com status DISPONIVEL");
        }

        livroRepository.delete(livro);
    }

    @Transactional(readOnly = true)
    public Livro buscarEntity(Long id) {
        return livroRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Livro nao encontrado"));
    }

    LivroResponse toResponse(Livro livro) {
        return new LivroResponse(
            livro.getId(),
            livro.getTitulo(),
            livro.getAutor(),
            livro.getIsbn(),
            livro.getAno(),
            livro.getStatus(),
            livro.getCategoria().getId(),
            livro.getCategoria().getNome()
        );
    }

    private String normalizarFiltro(String busca) {
        if (busca == null || busca.trim().isEmpty()) {
            return null;
        }
        return busca.trim();
    }

    private String trimToNull(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        return value.trim();
    }
}
