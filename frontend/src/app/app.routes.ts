import { Routes } from '@angular/router';
import { AtrasadosComponent } from './pages/atrasados/atrasados.component';
import { CategoriasComponent } from './pages/categorias/categorias.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { EmprestimosComponent } from './pages/emprestimos/emprestimos.component';
import { HistoricoLivroComponent } from './pages/historico-livro/historico-livro.component';
import { LivrosComponent } from './pages/livros/livros.component';

export const routes: Routes = [
  { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
  { path: 'dashboard', component: DashboardComponent },
  { path: 'categorias', component: CategoriasComponent },
  { path: 'livros', component: LivrosComponent },
  { path: 'emprestimos', component: EmprestimosComponent },
  { path: 'atrasados', component: AtrasadosComponent },
  { path: 'livros/:id/historico', component: HistoricoLivroComponent },
  { path: '**', redirectTo: 'dashboard' }
];
