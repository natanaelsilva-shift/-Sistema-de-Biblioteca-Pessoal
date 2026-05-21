package com.mylibrary.api.service;

import com.mylibrary.api.dto.DevolucaoRequest;
import com.mylibrary.api.dto.EmprestimoRequest;
import com.mylibrary.api.dto.EmprestimoResponse;
import com.mylibrary.api.entity.Emprestimo;
import com.mylibrary.api.entity.Livro;
import com.mylibrary.api.enums.StatusLivro;
import com.mylibrary.api.exception.BusinessException;
import com.mylibrary.api.exception.ResourceNotFoundException;
import com.mylibrary.api.repository.EmprestimoRepository;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmprestimoService {

    private final EmprestimoRepository emprestimoRepository;
    private final LivroService livroService;

    public EmprestimoService(EmprestimoRepository emprestimoRepository, LivroService livroService) {
        this.emprestimoRepository = emprestimoRepository;
        this.livroService = livroService;
    }

    @Transactional(readOnly = true)
    public List<EmprestimoResponse> listarTodos() {
        return emprestimoRepository.findAllByOrderByDataEmprestimoDescIdDesc().stream()
            .map(this::toResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<EmprestimoResponse> listarAtivos() {
        return emprestimoRepository.findByDataDevolucaoEfetivaIsNullOrderByDataDevolucaoPrevistaAsc().stream()
            .map(this::toResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<EmprestimoResponse> listarAtrasados() {
        return emprestimoRepository
            .findByDataDevolucaoEfetivaIsNullAndDataDevolucaoPrevistaBeforeOrderByDataDevolucaoPrevistaAsc(LocalDate.now())
            .stream()
            .map(this::toResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<EmprestimoResponse> historicoDoLivro(Long livroId) {
        livroService.buscarEntity(livroId);
        return emprestimoRepository.findByLivroIdOrderByDataEmprestimoDescIdDesc(livroId).stream()
            .map(this::toResponse)
            .toList();
    }

    @Transactional
    public EmprestimoResponse emprestar(EmprestimoRequest request) {
        Livro livro = livroService.buscarEntity(request.livroId());

        if (livro.getStatus() == StatusLivro.EMPRESTADO) {
            throw new BusinessException("Nao e permitido emprestar livro que ja esta EMPRESTADO");
        }

        validarDatas(request.dataEmprestimo(), request.dataDevolucaoPrevista());

        livro.setStatus(StatusLivro.EMPRESTADO);
        Emprestimo emprestimo = new Emprestimo(
            livro,
            request.nomePessoa().trim(),
            trimToNull(request.telefone()),
            request.dataEmprestimo(),
            request.dataDevolucaoPrevista()
        );

        return toResponse(emprestimoRepository.save(emprestimo));
    }

    @Transactional
    public EmprestimoResponse devolver(Long id, DevolucaoRequest request) {
        Emprestimo emprestimo = emprestimoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Emprestimo nao encontrado"));

        if (emprestimo.getDataDevolucaoEfetiva() != null) {
            throw new BusinessException("Emprestimo ja foi devolvido");
        }

        Livro livro = emprestimo.getLivro();
        if (livro.getStatus() == StatusLivro.DISPONIVEL) {
            throw new BusinessException("Nao e permitido devolver livro com status DISPONIVEL");
        }

        LocalDate dataEfetiva = request == null || request.dataDevolucaoEfetiva() == null
            ? LocalDate.now()
            : request.dataDevolucaoEfetiva();

        if (dataEfetiva.isBefore(emprestimo.getDataEmprestimo())) {
            throw new BusinessException("Data efetiva de devolucao nao pode ser anterior ao emprestimo");
        }

        emprestimo.setDataDevolucaoEfetiva(dataEfetiva);
        livro.setStatus(StatusLivro.DISPONIVEL);

        return toResponse(emprestimo);
    }

    @Transactional(readOnly = true)
    public long contarAtivos() {
        return emprestimoRepository.countByDataDevolucaoEfetivaIsNull();
    }

    @Transactional(readOnly = true)
    public List<EmprestimoResponse> ultimosCinco() {
        return emprestimoRepository.findTop5ByOrderByDataEmprestimoDescIdDesc().stream()
            .map(this::toResponse)
            .toList();
    }

    EmprestimoResponse toResponse(Emprestimo emprestimo) {
        boolean atrasado = isAtrasado(emprestimo);
        return new EmprestimoResponse(
            emprestimo.getId(),
            emprestimo.getLivro().getId(),
            emprestimo.getLivro().getTitulo(),
            emprestimo.getLivro().getAutor(),
            emprestimo.getNomePessoa(),
            emprestimo.getTelefone(),
            emprestimo.getDataEmprestimo(),
            emprestimo.getDataDevolucaoPrevista(),
            emprestimo.getDataDevolucaoEfetiva(),
            atrasado,
            atrasado ? ChronoUnit.DAYS.between(emprestimo.getDataDevolucaoPrevista(), LocalDate.now()) : 0
        );
    }

    private void validarDatas(LocalDate dataEmprestimo, LocalDate dataDevolucaoPrevista) {
        if (dataDevolucaoPrevista.isBefore(dataEmprestimo)) {
            throw new BusinessException("Data prevista de devolucao nao pode ser anterior ao emprestimo");
        }
    }

    private boolean isAtrasado(Emprestimo emprestimo) {
        return emprestimo.getDataDevolucaoEfetiva() == null
            && emprestimo.getDataDevolucaoPrevista().isBefore(LocalDate.now());
    }

    private String trimToNull(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        return value.trim();
    }
}
