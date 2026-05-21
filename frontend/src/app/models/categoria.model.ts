export interface Categoria {
  id: number;
  nome: string;
  descricao: string | null;
  quantidadeLivros: number;
}

export interface CategoriaPayload {
  nome: string;
  descricao: string | null;
}
