import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { SolicitudService, Solicitud } from '../../../core/services/solicitud.service';

@Component({
  selector: 'app-lista-solicitudes',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './lista-solicitudes.component.html'
})
export class ListaSolicitudesComponent implements OnInit {
  solicitudes: Solicitud[] = [];
  currentPage = 0;
  totalPages = 0;
  totalItems = 0;
  loading = true;
  error = '';
  pageSize = 10;

  constructor(
    private solicitudService: SolicitudService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadSolicitudes();
  }

  loadSolicitudes(): void {
    this.loading = true;
    this.error = '';

    this.solicitudService.getMySolicitudes(this.currentPage, this.pageSize).subscribe({
      next: (response) => {
        this.solicitudes = response.solicitudes;
        this.currentPage = response.currentPage;
        this.totalPages = response.totalPages;
        this.totalItems = response.totalItems;
        this.loading = false;
      },
      error: (err) => {
        this.error = err.error?.message || 'Error al cargar las solicitudes';
        this.loading = false;
      }
    });
  }

  nuevaSolicitud(): void {
    this.router.navigate(['/solicitudes/nueva']);
  }

  nextPage(): void {
    if (this.currentPage < this.totalPages - 1) {
      this.currentPage++;
      this.loadSolicitudes();
    }
  }

  previousPage(): void {
    if (this.currentPage > 0) {
      this.currentPage--;
      this.loadSolicitudes();
    }
  }

  getEstadoBadgeClass(estado: string): string {
    switch (estado) {
      case 'PENDIENTE':
        return 'bg-yellow-100 text-yellow-800';
      case 'APROBADA':
        return 'bg-green-100 text-green-800';
      case 'RECHAZADA':
        return 'bg-red-100 text-red-800';
      default:
        return 'bg-gray-100 text-gray-800';
    }
  }

  formatDate(dateString: string): string {
    const date = new Date(dateString);
    return date.toLocaleDateString('es-ES', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  }
}
