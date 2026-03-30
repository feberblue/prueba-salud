import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { SolicitudService } from './solicitud.service';

describe('SolicitudService', () => {
  let service: SolicitudService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [SolicitudService]
    });

    service = TestBed.inject(SolicitudService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('getMedicamentos', () => {
    it('should retrieve all medicamentos', () => {
      const mockMedicamentos = [
        {
          id: 1,
          nombre: 'Acetaminofén 500mg',
          descripcion: 'Analgésico y antipirético',
          esNoPos: false
        },
        {
          id: 11,
          nombre: 'Adalimumab 40mg',
          descripcion: 'Medicamento biológico',
          esNoPos: true
        }
      ];

      service.getMedicamentos().subscribe(medicamentos => {
        expect(medicamentos.length).toBe(2);
        expect(medicamentos).toEqual(mockMedicamentos);
      });

      const req = httpMock.expectOne('/api/medicamentos');
      expect(req.request.method).toBe('GET');
      req.flush(mockMedicamentos);
    });

    it('should handle error when retrieving medicamentos', () => {
      service.getMedicamentos().subscribe({
        next: () => fail('should have failed'),
        error: (error) => {
          expect(error.status).toBe(500);
        }
      });

      const req = httpMock.expectOne('/api/medicamentos');
      req.flush({ message: 'Error del servidor' }, { status: 500, statusText: 'Internal Server Error' });
    });
  });

  describe('createSolicitud', () => {
    it('should create a POS solicitud successfully', () => {
      const mockSolicitudData = {
        medicamentoId: 1
      };

      const mockResponse = {
        message: 'Solicitud creada exitosamente',
        solicitud: {
          id: 1,
          usuarioId: 1,
          usuarioUsername: 'testuser',
          medicamento: {
            id: 1,
            nombre: 'Acetaminofén 500mg',
            descripcion: 'Analgésico y antipirético',
            esNoPos: false
          },
          estado: 'PENDIENTE',
          createdAt: '2024-03-27T10:00:00'
        }
      };

      service.createSolicitud(mockSolicitudData).subscribe(response => {
        expect(response).toEqual(mockResponse);
      });

      const req = httpMock.expectOne('/api/solicitudes');
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual(mockSolicitudData);
      req.flush(mockResponse);
    });

    it('should create a NO POS solicitud with additional fields', () => {
      const mockSolicitudData = {
        medicamentoId: 11,
        numeroOrden: 'ORD-2024-001',
        direccion: 'Calle 123 #45-67',
        telefono: '3001234567',
        correo: 'test@example.com'
      };

      const mockResponse = {
        message: 'Solicitud creada exitosamente',
        solicitud: {
          id: 2,
          usuarioId: 1,
          usuarioUsername: 'testuser',
          medicamento: {
            id: 11,
            nombre: 'Adalimumab 40mg',
            descripcion: 'Medicamento biológico',
            esNoPos: true
          },
          numeroOrden: 'ORD-2024-001',
          direccion: 'Calle 123 #45-67',
          telefono: '3001234567',
          correo: 'test@example.com',
          estado: 'PENDIENTE',
          createdAt: '2024-03-27T10:00:00'
        }
      };

      service.createSolicitud(mockSolicitudData).subscribe(response => {
        expect(response).toEqual(mockResponse);
      });

      const req = httpMock.expectOne('/api/solicitudes');
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual(mockSolicitudData);
      req.flush(mockResponse);
    });

    it('should handle validation error for NO POS without required fields', () => {
      const mockSolicitudData = {
        medicamentoId: 11
      };

      service.createSolicitud(mockSolicitudData).subscribe({
        next: () => fail('should have failed'),
        error: (error) => {
          expect(error.status).toBe(400);
        }
      });

      const req = httpMock.expectOne('/api/solicitudes');
      req.flush(
        { 
          message: 'Error de validación',
          errors: {
            numeroOrden: 'El número de orden es obligatorio para medicamentos NO POS',
            direccion: 'La dirección es obligatoria para medicamentos NO POS'
          }
        },
        { status: 400, statusText: 'Bad Request' }
      );
    });
  });

  describe('getMySolicitudes', () => {
    it('should retrieve paginated solicitudes', () => {
      const mockResponse = {
        solicitudes: [
          {
            id: 1,
            usuarioId: 1,
            usuarioUsername: 'testuser',
            medicamento: {
              id: 1,
              nombre: 'Acetaminofén 500mg',
              descripcion: 'Analgésico y antipirético',
              esNoPos: false
            },
            estado: 'PENDIENTE',
            createdAt: '2024-03-27T10:00:00'
          }
        ],
        currentPage: 0,
        totalItems: 1,
        totalPages: 1
      };

      service.getMySolicitudes(0, 10).subscribe(response => {
        expect(response).toEqual(mockResponse);
        expect(response.solicitudes.length).toBe(1);
      });

      const req = httpMock.expectOne('/api/solicitudes?page=0&size=10');
      expect(req.request.method).toBe('GET');
      req.flush(mockResponse);
    });

    it('should handle different page sizes', () => {
      const mockResponse = {
        solicitudes: [],
        currentPage: 1,
        totalItems: 25,
        totalPages: 3
      };

      service.getMySolicitudes(1, 10).subscribe(response => {
        expect(response.currentPage).toBe(1);
        expect(response.totalPages).toBe(3);
      });

      const req = httpMock.expectOne('/api/solicitudes?page=1&size=10');
      expect(req.request.method).toBe('GET');
      req.flush(mockResponse);
    });

    it('should handle error when retrieving solicitudes', () => {
      service.getMySolicitudes(0, 10).subscribe({
        next: () => fail('should have failed'),
        error: (error) => {
          expect(error.status).toBe(401);
        }
      });

      const req = httpMock.expectOne('/api/solicitudes?page=0&size=10');
      req.flush({ message: 'No autenticado' }, { status: 401, statusText: 'Unauthorized' });
    });
  });
});
