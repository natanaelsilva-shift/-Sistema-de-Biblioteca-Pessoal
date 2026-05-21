import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Dashboard } from '../../models/dashboard.model';
import { getApiErrorMessage } from '../../services/api-error';
import { DashboardService } from '../../services/dashboard.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './dashboard.component.html'
})
export class DashboardComponent implements OnInit {
  private readonly dashboardService = inject(DashboardService);

  dashboard?: Dashboard;
  carregando = true;
  erro = '';

  ngOnInit(): void {
    this.carregar();
  }

  carregar(): void {
    this.carregando = true;
    this.erro = '';

    this.dashboardService.obter().subscribe({
      next: (dashboard) => {
        this.dashboard = dashboard;
        this.carregando = false;
      },
      error: (error: unknown) => {
        this.erro = getApiErrorMessage(error);
        this.carregando = false;
      }
    });
  }
}
