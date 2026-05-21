import { HttpErrorResponse } from '@angular/common/http';

interface ApiErrorBody {
  message?: string;
}

export function getApiErrorMessage(error: unknown): string {
  if (error instanceof HttpErrorResponse) {
    const body = error.error as ApiErrorBody | null;
    return body?.message ?? 'Nao foi possivel concluir a operacao.';
  }

  return 'Nao foi possivel concluir a operacao.';
}
