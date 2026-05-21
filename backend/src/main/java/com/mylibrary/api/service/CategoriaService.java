package com.mylibrary.api.service;

import com.mylibrary.api.dto.CategoriaRequest;
import com.mylibrary.api.dto.CategoriaResponse;
import com.mylibrary.api.entity.Categoria;
import com.mylibrary.api.exception.BusinessException;
import com.mylibrary.api.exception.ResourceNotFoundException;
import com.mylibrary.api.repository.CategoriaRepository;
import com.mylibrary.api.repository.LivroRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final LivroRepository livroRepository;

    public CategoriaService(CategoriaRepository categoriaRepository, LivroRepository livroRepository) {
        this.categoriaRepository = categoriaRepository;
        this.livroRepository = livroRepository;
    }

    @Transactional(readOnly = true)
    public List<CategoriaResponse> listar() {
        return categoriaRepository.findAll().stream()
            .map(this::toResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    public CategoriaResponse buscar(Long id) {
        return toResponse(buscarEntity(id));
    }

    @Transactional
    public CategoriaResponse criar(CategoriaRequest request) {
        String nome = request.nome().trim();
        if (categoriaRepository.existsByNomeIgnoreCase(nome)) {
            throw new BusinessException("Nome da categoria deve ser unico");
        }

        Categoria categoria = new Categoria(nome, trimToNull(request.descricao()));
        return toResponse(categoriaRepository.save(categoria));
    }

    @Transactional
    public void excluir(Long id) {
        Categoria categoria = buscarEntity(id);
        long quantidadeLivros = livroRepository.countByCategoriaId(id);

        if (quantidadeLivros > 0) {
            throw new BusinessException("Nao e permitido excluir categoria que possui livros");
        }

        categoriaRepository.delete(categoria);
    }

    @Transactional(readOnly = true)
    public Categoria buscarEntity(Long id) {
        return categoriaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Categoria nao encontrada"));
    }

    private CategoriaResponse toResponse(Categoria categoria) {
        return new CategoriaResponse(
            categoria.getId(),
            categoria.getNome(),
            categoria.getDescricao(),
            livroRepository.countByCategoriaId(categoria.getId())
        );
    }

    private String trimToNull(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        return value.trim();
    }
}
