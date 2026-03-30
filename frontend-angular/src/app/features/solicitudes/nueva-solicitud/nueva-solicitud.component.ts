import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { SolicitudService, Medicamento, SolicitudRequest } from '../../../core/services/solicitud.service';

@Component({
  selector: 'app-nueva-solicitud',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './nueva-solicitud.component.html'
})
export class NuevaSolicitudComponent implements OnInit {
  medicamentos: Medicamento[] = [];
  selectedMedicamento: Medicamento | null = null;
  formData: SolicitudRequest = {
    medicamentoId: null as any
  };
  error = '';
  fieldErrors: any = {};
  success = false;
  loading = false;

  constructor(
    private solicitudService: SolicitudService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadMedicamentos();
  }

  loadMedicamentos(): void {
    this.solicitudService.getMedicamentos().subscribe({
      next: (data) => {
        this.medicamentos = data;
      },
      error: () => {
        this.error = 'Error al cargar medicamentos';
      }
    });
  }

  onMedicamentoChange(): void {
    const medicamentoId = Number(this.formData.medicamentoId);
    this.selectedMedicamento = this.medicamentos.find(m => m.id === medicamentoId) || null;
    
    if (!this.selectedMedicamento?.esNoPos) {
      this.formData.numeroOrden = undefined;
      this.formData.direccion = undefined;
      this.formData.telefono = undefined;
      this.formData.correo = undefined;
    }
    
    this.fieldErrors = {};
  }

  onSubmit(): void {
    this.loading = true;
    this.error = '';
    this.fieldErrors = {};

    this.solicitudService.createSolicitud(this.formData).subscribe({
      next: () => {
        this.success = true;
        setTimeout(() => {
          this.router.navigate(['/solicitudes']);
        }, 2000);
      },
      error: (err) => {
        if (err.error?.errors) {
          this.fieldErrors = err.error.errors;
          this.error = 'Por favor corrige los errores en el formulario';
        } else {
          this.error = err.error?.message || 'Error al crear la solicitud. Intenta nuevamente.';
        }
        this.loading = false;
      }
    });
  }

  cancel(): void {
    this.router.navigate(['/solicitudes']);
  }
}
