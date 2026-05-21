package com.mylibrary.api.controller;

import com.mylibrary.api.dto.DevolucaoRequest;
import com.mylibrary.api.dto.EmprestimoRequest;
import com.mylibrary.api.dto.EmprestimoResponse;
import com.mylibrary.api.service.EmprestimoService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/emprestimos")
public class EmprestimoController {

    private final EmprestimoService emprestimoService;

    public EmprestimoController(EmprestimoService emprestimoService) {
        this.emprestimoService = emprestimoService;
    }

    @GetMapping
    public List<EmprestimoResponse> listarTodos() {
        return emprestimoService.listarTodos();
    }

    @GetMapping("/ativos")
    public List<EmprestimoResponse> listarAtivos() {
        return emprestimoService.listarAtivos();
    }

    @GetMapping("/atrasados")
    public List<EmprestimoResponse> listarAtrasados() {
        return emprestimoService.listarAtrasados();
    }

    @PostMapping("/emprestar")
    public ResponseEntity<EmprestimoResponse> emprestar(@Valid @RequestBody EmprestimoRequest request) {
        EmprestimoResponse response = emprestimoService.emprestar(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequestUri()
            .replacePath("/api/emprestimos/{id}")
            .buildAndExpand(response.id())
            .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @PostMapping("/{id}/devolver")
    public EmprestimoResponse devolver(
        @PathVariable Long id,
        @RequestBody(required = false) DevolucaoRequest request
    ) {
        return emprestimoService.devolver(id, request);
    }
}
