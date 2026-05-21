import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { Emprestimo } from '../models/emprestimo.model';
import { Livro, LivroFiltro, LivroPayload } from '../models/livro.model';

@Injectable({ providedIn: 'root' })
export class LivroService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8080/api/livros';

  listar(filtro: LivroFiltro = {}): Observable<Livro[]> {
    let params = new HttpParams();

    if (filtro.categoriaId) {
      params = params.set('categoriaId', filtro.categoriaId);
    }

    if (filtro.status) {
      params = params.set('status', filtro.status);
    }

    if (filtro.busca) {
      params = params.set('busca', filtro.busca);
    }

    return this.http.get<Livro[]>(this.apiUrl, { params });
  }

  buscar(id: number): Observable<Livro> {
    return this.http.get<Livro>(`${this.apiUrl}/${id}`);
  }

  criar(payload: LivroPayload): Observable<Livro> {
    return this.http.post<Livro>(this.apiUrl, payload);
  }

  excluir(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  historico(id: number): Observable<Emprestimo[]> {
    return this.http.get<Emprestimo[]>(`${this.apiUrl}/${id}/emprestimos`);
  }
}
