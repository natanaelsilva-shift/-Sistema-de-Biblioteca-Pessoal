package com.mylibrary.api.service;

import com.mylibrary.api.dto.DashboardResponse;
import com.mylibrary.api.enums.StatusLivro;
import com.mylibrary.api.repository.LivroRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DashboardService {

    private final LivroRepository livroRepository;
    private final EmprestimoService emprestimoService;

    public DashboardService(LivroRepository livroRepository, EmprestimoService emprestimoService) {
        this.livroRepository = livroRepository;
        this.emprestimoService = emprestimoService;
    }

    @Transactional(readOnly = true)
    public DashboardResponse obterDashboard() {
        return new DashboardResponse(
            livroRepository.count(),
            livroRepository.countByStatus(StatusLivro.DISPONIVEL),
            livroRepository.countByStatus(StatusLivro.EMPRESTADO),
            emprestimoService.contarAtivos(),
            emprestimoService.ultimosCinco()
        );
    }
}
