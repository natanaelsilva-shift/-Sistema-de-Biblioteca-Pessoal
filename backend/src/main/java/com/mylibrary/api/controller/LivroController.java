package com.mylibrary.api.controller;

import com.mylibrary.api.dto.EmprestimoResponse;
import com.mylibrary.api.dto.LivroRequest;
import com.mylibrary.api.dto.LivroResponse;
import com.mylibrary.api.enums.StatusLivro;
import com.mylibrary.api.service.EmprestimoService;
import com.mylibrary.api.service.LivroService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/livros")
public class LivroController {

    private final LivroService livroService;
    private final EmprestimoService emprestimoService;

    public LivroController(LivroService livroService, EmprestimoService emprestimoService) {
        this.livroService = livroService;
        this.emprestimoService = emprestimoService;
    }

    @GetMapping
    public List<LivroResponse> listar(
        @RequestParam(required = false) Long categoriaId,
        @RequestParam(required = false) StatusLivro status,
        @RequestParam(required = false) String busca
    ) {
        return livroService.listar(categoriaId, status, busca);
    }

    @GetMapping("/{id}")
    public LivroResponse buscar(@PathVariable Long id) {
        return livroService.buscar(id);
    }

    @PostMapping
    public ResponseEntity<LivroResponse> criar(@Valid @RequestBody LivroRequest request) {
        LivroResponse response = livroService.criar(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(response.id())
            .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        livroService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/emprestimos")
    public List<EmprestimoResponse> historico(@PathVariable Long id) {
        return emprestimoService.historicoDoLivro(id);
    }
}
