import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { Categoria } from '../../models/categoria.model';
import { Livro, StatusLivro } from '../../models/livro.model';
import { getApiErrorMessage } from '../../services/api-error';
import { CategoriaService } from '../../services/categoria.service';
import { EmprestimoService } from '../../services/emprestimo.service';
import { LivroService } from '../../services/livro.service';

type StatusFiltro = StatusLivro | 'TODOS';

@Component({
  selector: 'app-livros',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './livros.component.html'
})
export class LivrosComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly livroService = inject(LivroService);
  private readonly categoriaService = inject(CategoriaService);
  private readonly emprestimoService = inject(EmprestimoService);

  categorias: Categoria[] = [];
  livros: Livro[] = [];
  livroParaEmprestimo?: Livro;
  carregando = true;
  erro = '';
  sucesso = '';

  livroForm = this.fb.nonNullable.group({
    titulo: ['', [Validators.required, Validators.maxLength(180)]],
    autor: ['', [Validators.required, Validators.maxLength(140)]],
    isbn: ['', Validators.maxLength(40)],
    ano: [new Date().getFullYear(), [Validators.required, Validators.min(1), Validators.max(2100)]],
    categoriaId: ['', Validators.required]
  });

  filtroForm = this.fb.nonNullable.group({
    categoriaId: [''],
    status: ['TODOS' as StatusFiltro],
    busca: ['']
  });

  emprestimoForm = this.fb.nonNullable.group({
    livroId: [0, Validators.required],
    nomePessoa: ['', [Validators.required, Validators.maxLength(140)]],
    telefone: ['', Validators.maxLength(30)],
    dataEmprestimo: [this.hoje(), Validators.required],
    dataDevolucaoPrevista: [this.daquiQuatorzeDias(), Validators.required]
  });

  ngOnInit(): void {
    this.carregarCategorias();
    this.carregarLivros();
  }

  carregarCategorias(): void {
    this.categoriaService.listar().subscribe({
      next: (categorias) => {
        this.categorias = categorias;
      },
      error: (error: unknown) => {
        this.erro = getApiErrorMessage(error);
      }
    });
  }

  carregarLivros(): void {
    this.carregando = true;
    this.erro = '';

    const filtro = this.filtroForm.getRawValue();
    this.livroService.listar({
      categoriaId: filtro.categoriaId ? Number(filtro.categoriaId) : undefined,
      status: filtro.status === 'TODOS' ? undefined : filtro.status,
      busca: filtro.busca.trim() || undefined
    }).subscribe({
      next: (livros) => {
        this.livros = livros;
        this.carregando = false;
      },
      error: (error: unknown) => {
        this.erro = getApiErrorMessage(error);
        this.carregando = false;
      }
    });
  }

  limparFiltros(): void {
    this.filtroForm.reset({ categoriaId: '', status: 'TODOS', busca: '' });
    this.carregarLivros();
  }

  salvarLivro(): void {
    if (this.livroForm.invalid) {
      this.livroForm.markAllAsTouched();
      return;
    }

    const value = this.livroForm.getRawValue();
    this.livroService.criar({
      titulo: value.titulo,
      autor: value.autor,
      isbn: value.isbn.trim() || null,
      ano: Number(value.ano),
      categoriaId: Number(value.categoriaId)
    }).subscribe({
      next: () => {
        this.sucesso = 'Livro cadastrado com status DISPONIVEL.';
        this.livroForm.reset({
          titulo: '',
          autor: '',
          isbn: '',
          ano: new Date().getFullYear(),
          categoriaId: ''
        });
        this.carregarLivros();
      },
      error: (error: unknown) => {
        this.erro = getApiErrorMessage(error);
      }
    });
  }

  excluirLivro(livro: Livro): void {
    const confirmado = window.confirm(`Excluir o livro "${livro.titulo}"?`);
    if (!confirmado) {
      return;
    }

    this.livroService.excluir(livro.id).subscribe({
      next: () => {
        this.sucesso = 'Livro excluido com sucesso.';
        this.carregarLivros();
      },
      error: (error: unknown) => {
        this.erro = getApiErrorMessage(error);
      }
    });
  }

  abrirEmprestimo(livro: Livro): void {
    this.erro = '';
    this.sucesso = '';
    this.livroParaEmprestimo = livro;
    this.emprestimoForm.reset({
      livroId: livro.id,
      nomePessoa: '',
      telefone: '',
      dataEmprestimo: this.hoje(),
      dataDevolucaoPrevista: this.daquiQuatorzeDias()
    });
  }

  cancelarEmprestimo(): void {
    this.livroParaEmprestimo = undefined;
  }

  emprestar(): void {
    if (this.emprestimoForm.invalid) {
      this.emprestimoForm.markAllAsTouched();
      return;
    }

    const value = this.emprestimoForm.getRawValue();
    this.emprestimoService.emprestar({
      livroId: Number(value.livroId),
      nomePessoa: value.nomePessoa,
      telefone: value.telefone.trim() || null,
      dataEmprestimo: value.dataEmprestimo,
      dataDevolucaoPrevista: value.dataDevolucaoPrevista
    }).subscribe({
      next: () => {
        this.sucesso = 'Emprestimo registrado com sucesso.';
        this.livroParaEmprestimo = undefined;
        this.carregarLivros();
      },
      error: (error: unknown) => {
        this.erro = getApiErrorMessage(error);
      }
    });
  }

  private hoje(): string {
    return new Date().toISOString().slice(0, 10);
  }

  private daquiQuatorzeDias(): string {
    const data = new Date();
    data.setDate(data.getDate() + 14);
    return data.toISOString().slice(0, 10);
  }
}
