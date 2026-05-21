import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { Emprestimo } from '../../models/emprestimo.model';
import { getApiErrorMessage } from '../../services/api-error';
import { EmprestimoService } from '../../services/emprestimo.service';

@Component({
  selector: 'app-emprestimos',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './emprestimos.component.html'
})
export class EmprestimosComponent implements OnInit {
  private readonly emprestimoService = inject(EmprestimoService);

  emprestimos: Emprestimo[] = [];
  carregando = true;
  erro = '';
  sucesso = '';

  ngOnInit(): void {
    this.carregar();
  }

  carregar(): void {
    this.carregando = true;
    this.erro = '';

    this.emprestimoService.listarAtivos().subscribe({
      next: (emprestimos) => {
        this.emprestimos = emprestimos;
        this.carregando = false;
      },
      error: (error: unknown) => {
        this.erro = getApiErrorMessage(error);
        this.carregando = false;
      }
    });
  }

  devolver(emprestimo: Emprestimo): void {
    const confirmado = window.confirm(`Confirmar devolucao de "${emprestimo.livroTitulo}"?`);
    if (!confirmado) {
      return;
    }

    this.emprestimoService.devolver(emprestimo.id).subscribe({
      next: () => {
        this.sucesso = 'Livro devolvido com sucesso.';
        this.carregar();
      },
      error: (error: unknown) => {
        this.erro = getApiErrorMessage(error);
      }
    });
  }
}
