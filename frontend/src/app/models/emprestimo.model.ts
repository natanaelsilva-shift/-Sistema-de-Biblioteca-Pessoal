export interface Emprestimo {
  id: number;
  livroId: number;
  livroTitulo: string;
  livroAutor: string;
  nomePessoa: string;
  telefone: string | null;
  dataEmprestimo: string;
  dataDevolucaoPrevista: string;
  dataDevolucaoEfetiva: string | null;
  atrasado: boolean;
  diasAtraso: number;
}

export interface EmprestimoPayload {
  livroId: number;
  nomePessoa: string;
  telefone: string | null;
  dataEmprestimo: string;
  dataDevolucaoPrevista: string;
}
