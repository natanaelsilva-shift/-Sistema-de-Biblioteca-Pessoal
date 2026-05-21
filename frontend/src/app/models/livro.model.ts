export type StatusLivro = 'DISPONIVEL' | 'EMPRESTADO';

export interface Livro {
  id: number;
  titulo: string;
  autor: string;
  isbn: string | null;
  ano: number;
  status: StatusLivro;
  categoriaId: number;
  categoriaNome: string;
}

export interface LivroPayload {
  titulo: string;
  autor: string;
  isbn: string | null;
  ano: number;
  categoriaId: number;
}

export interface LivroFiltro {
  categoriaId?: number;
  status?: StatusLivro;
  busca?: string;
}
