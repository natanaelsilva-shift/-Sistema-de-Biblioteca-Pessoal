import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { Emprestimo, EmprestimoPayload } from '../models/emprestimo.model';

@Injectable({ providedIn: 'root' })
export class EmprestimoService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8080/api/emprestimos';

  listarTodos(): Observable<Emprestimo[]> {
    return this.http.get<Emprestimo[]>(this.apiUrl);
  }

  listarAtivos(): Observable<Emprestimo[]> {
    return this.http.get<Emprestimo[]>(`${this.apiUrl}/ativos`);
  }

  listarAtrasados(): Observable<Emprestimo[]> {
    return this.http.get<Emprestimo[]>(`${this.apiUrl}/atrasados`);
  }

  emprestar(payload: EmprestimoPayload): Observable<Emprestimo> {
    return this.http.post<Emprestimo>(`${this.apiUrl}/emprestar`, payload);
  }

  devolver(id: number, dataDevolucaoEfetiva?: string): Observable<Emprestimo> {
    const body = dataDevolucaoEfetiva ? { dataDevolucaoEfetiva } : {};
    return this.http.post<Emprestimo>(`${this.apiUrl}/${id}/devolver`, body);
  }
}
