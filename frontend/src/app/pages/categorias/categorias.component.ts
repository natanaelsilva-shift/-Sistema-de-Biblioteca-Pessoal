import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Categoria } from '../../models/categoria.model';
import { getApiErrorMessage } from '../../services/api-error';
import { CategoriaService } from '../../services/categoria.service';

@Component({
  selector: 'app-categorias',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './categorias.component.html'
})
export class CategoriasComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly categoriaService = inject(CategoriaService);

  categorias: Categoria[] = [];
  carregando = true;
  erro = '';
  sucesso = '';

  form = this.fb.nonNullable.group({
    nome: ['', [Validators.required, Validators.maxLength(120)]],
    descricao: ['', Validators.maxLength(500)]
  });

  ngOnInit(): void {
    this.carregar();
  }

  carregar(): void {
    this.carregando = true;
    this.erro = '';

    this.categoriaService.listar().subscribe({
      next: (categorias) => {
        this.categorias = categorias;
        this.carregando = false;
      },
      error: (error: unknown) => {
        this.erro = getApiErrorMessage(error);
        this.carregando = false;
      }
    });
  }

  salvar(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const value = this.form.getRawValue();
    this.categoriaService.criar({
      nome: value.nome,
      descricao: value.descricao.trim() || null
    }).subscribe({
      next: () => {
        this.sucesso = 'Categoria criada com sucesso.';
        this.form.reset();
        this.carregar();
      },
      error: (error: unknown) => {
        this.erro = getApiErrorMessage(error);
      }
    });
  }

  excluir(categoria: Categoria): void {
    const confirmado = window.confirm(`Excluir a categoria "${categoria.nome}"?`);
    if (!confirmado) {
      return;
    }

    this.categoriaService.excluir(categoria.id).subscribe({
      next: () => {
        this.sucesso = 'Categoria excluida com sucesso.';
        this.carregar();
      },
      error: (error: unknown) => {
        this.erro = getApiErrorMessage(error);
      }
    });
  }
}
