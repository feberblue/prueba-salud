import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Medicamento {
  id: number;
  nombre: string;
  descripcion: string;
  esNoPos: boolean;
}

export interface SolicitudRequest {
  medicamentoId: number;
  numeroOrden?: string;
  direccion?: string;
  telefono?: string;
  correo?: string;
}

export interface Solicitud {
  id: number;
  usuarioId: number;
  usuarioUsername: string;
  medicamento: Medicamento;
  numeroOrden?: string;
  direccion?: string;
  telefono?: string;
  correo?: string;
  estado: string;
  createdAt: string;
}

export interface SolicitudResponse {
  message: string;
  solicitud: Solicitud;
}

export interface SolicitudesPageResponse {
  solicitudes: Solicitud[];
  currentPage: number;
  totalItems: number;
  totalPages: number;
}

@Injectable({
  providedIn: 'root'
})
export class SolicitudService {
  private readonly API_URL = '/api';

  constructor(private http: HttpClient) {}

  getMedicamentos(): Observable<Medicamento[]> {
    return this.http.get<Medicamento[]>(`${this.API_URL}/medicamentos`, { withCredentials: true });
  }

  createSolicitud(data: SolicitudRequest): Observable<SolicitudResponse> {
    return this.http.post<SolicitudResponse>(`${this.API_URL}/solicitudes`, data, { withCredentials: true });
  }

  getMySolicitudes(page: number = 0, size: number = 10): Observable<SolicitudesPageResponse> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<SolicitudesPageResponse>(`${this.API_URL}/solicitudes`, { 
      params, 
      withCredentials: true 
    });
  }
}
