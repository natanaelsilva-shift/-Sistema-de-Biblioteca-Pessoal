import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { Emprestimo } from '../../models/emprestimo.model';
import { getApiErrorMessage } from '../../services/api-error';
import { EmprestimoService } from '../../services/emprestimo.service';

@Component({
  selector: 'app-atrasados',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './atrasados.component.html'
})
export class AtrasadosComponent implements OnInit {
  private readonly emprestimoService = inject(EmprestimoService);

  atrasados: Emprestimo[] = [];
  carregando = true;
  erro = '';

  ngOnInit(): void {
    this.carregar();
  }

  carregar(): void {
    this.carregando = true;
    this.erro = '';

    this.emprestimoService.listarAtrasados().subscribe({
      next: (atrasados) => {
        this.atrasados = atrasados;
        this.carregando = false;
      },
      error: (error: unknown) => {
        this.erro = getApiErrorMessage(error);
        this.carregando = false;
      }
    });
  }
}
