import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { forkJoin } from 'rxjs';
import { Emprestimo } from '../../models/emprestimo.model';
import { Livro } from '../../models/livro.model';
import { getApiErrorMessage } from '../../services/api-error';
import { LivroService } from '../../services/livro.service';

@Component({
  selector: 'app-historico-livro',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './historico-livro.component.html'
})
export class HistoricoLivroComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly livroService = inject(LivroService);

  livro?: Livro;
  historico: Emprestimo[] = [];
  carregando = true;
  erro = '';

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));

    if (!id) {
      this.erro = 'Livro invalido.';
      this.carregando = false;
      return;
    }

    forkJoin({
      livro: this.livroService.buscar(id),
      historico: this.livroService.historico(id)
    }).subscribe({
      next: ({ livro, historico }) => {
        this.livro = livro;
        this.historico = historico;
        this.carregando = false;
      },
      error: (error: unknown) => {
        this.erro = getApiErrorMessage(error);
        this.carregando = false;
      }
    });
  }
}
