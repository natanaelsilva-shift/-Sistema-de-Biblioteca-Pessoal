package com.mylibrary.api.config;

import com.mylibrary.api.entity.Categoria;
import com.mylibrary.api.entity.Emprestimo;
import com.mylibrary.api.entity.Livro;
import com.mylibrary.api.enums.StatusLivro;
import com.mylibrary.api.repository.CategoriaRepository;
import com.mylibrary.api.repository.EmprestimoRepository;
import com.mylibrary.api.repository.LivroRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {

    private final CategoriaRepository categoriaRepository;
    private final LivroRepository livroRepository;
    private final EmprestimoRepository emprestimoRepository;

    public DataLoader(
        CategoriaRepository categoriaRepository,
        LivroRepository livroRepository,
        EmprestimoRepository emprestimoRepository
    ) {
        this.categoriaRepository = categoriaRepository;
        this.livroRepository = livroRepository;
        this.emprestimoRepository = emprestimoRepository;
    }

    @Bean
    CommandLineRunner carregarDadosIniciais() {
        return args -> {
            if (categoriaRepository.count() > 0) {
                return;
            }

            Categoria tecnologia = new Categoria("Tecnologia", "Livros sobre desenvolvimento de software e arquitetura");
            Categoria literatura = new Categoria("Literatura", "Romances, contos e ficcao");
            Categoria negocios = new Categoria("Negocios", "Gestao, produto e empreendedorismo");
            categoriaRepository.saveAll(List.of(tecnologia, literatura, negocios));

            Livro cleanCode = new Livro("Clean Code", "Robert C. Martin", "9780132350884", 2008, tecnologia);
            Livro ddd = new Livro("Domain-Driven Design", "Eric Evans", "9780321125217", 2003, tecnologia);
            Livro hobbit = new Livro("O Hobbit", "J. R. R. Tolkien", "9788595084742", 1937, literatura);
            Livro startup = new Livro("A Startup Enxuta", "Eric Ries", "9788581780047", 2011, negocios);
            Livro arquitetura = new Livro("Arquitetura Limpa", "Robert C. Martin", "9788550804606", 2019, tecnologia);

            livroRepository.saveAll(List.of(cleanCode, ddd, hobbit, startup, arquitetura));

            LocalDate hoje = LocalDate.now();

            emprestar(hobbit, "Mariana Souza", "(11) 99999-1001", hoje.minusDays(10), hoje.plusDays(4));
            emprestar(startup, "Carlos Lima", "(21) 98888-2002", hoje.minusDays(25), hoje.minusDays(8));

            Emprestimo devolvido = new Emprestimo(
                cleanCode,
                "Ana Martins",
                "(31) 97777-3003",
                hoje.minusDays(40),
                hoje.minusDays(25)
            );
            devolvido.setDataDevolucaoEfetiva(hoje.minusDays(24));
            emprestimoRepository.save(devolvido);
        };
    }

    private void emprestar(
        Livro livro,
        String pessoa,
        String telefone,
        LocalDate dataEmprestimo,
        LocalDate dataDevolucaoPrevista
    ) {
        livro.setStatus(StatusLivro.EMPRESTADO);
        livroRepository.save(livro);
        emprestimoRepository.save(new Emprestimo(livro, pessoa, telefone, dataEmprestimo, dataDevolucaoPrevista));
    }
}
