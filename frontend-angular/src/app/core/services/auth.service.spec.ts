import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { AuthService } from './auth.service';
import { Router } from '@angular/router';

describe('AuthService', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;
  let routerSpy: jasmine.SpyObj<Router>;

  beforeEach(() => {
    const spy = jasmine.createSpyObj('Router', ['navigate']);

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        AuthService,
        { provide: Router, useValue: spy }
      ]
    });

    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
    routerSpy = TestBed.inject(Router) as jasmine.SpyObj<Router>;
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('register', () => {
    it('should register a new user successfully', () => {
      const mockRegisterData = {
        username: 'testuser',
        email: 'test@example.com',
        password: 'password123'
      };

      const mockResponse = {
        message: 'Usuario registrado exitosamente',
        user: {
          id: 1,
          username: 'testuser',
          email: 'test@example.com',
          createdAt: '2024-03-27T10:00:00'
        }
      };

      service.register(mockRegisterData).subscribe(response => {
        expect(response).toEqual(mockResponse);
      });

      const req = httpMock.expectOne('/auth/register');
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual(mockRegisterData);
      req.flush(mockResponse);
    });

    it('should handle registration error', () => {
      const mockRegisterData = {
        username: 'testuser',
        email: 'test@example.com',
        password: 'password123'
      };

      service.register(mockRegisterData).subscribe({
        next: () => fail('should have failed'),
        error: (error) => {
          expect(error.status).toBe(400);
        }
      });

      const req = httpMock.expectOne('/auth/register');
      req.flush({ message: 'Usuario ya existe' }, { status: 400, statusText: 'Bad Request' });
    });
  });

  describe('login', () => {
    it('should login successfully and set user', () => {
      const mockLoginData = {
        username: 'testuser',
        password: 'password123'
      };

      const mockResponse = {
        message: 'Inicio de sesión exitoso',
        user: {
          id: 1,
          username: 'testuser',
          email: 'test@example.com',
          createdAt: '2024-03-27T10:00:00'
        }
      };

      service.login(mockLoginData).subscribe(response => {
        expect(response).toEqual(mockResponse);
        expect(service.currentUser()).toEqual(mockResponse.user);
        expect(service.isAuthenticated()).toBe(true);
      });

      const req = httpMock.expectOne('/auth/login');
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual(mockLoginData);
      req.flush(mockResponse);
    });

    it('should handle login error', () => {
      const mockLoginData = {
        username: 'testuser',
        password: 'wrongpassword'
      };

      service.login(mockLoginData).subscribe({
        next: () => fail('should have failed'),
        error: (error) => {
          expect(error.status).toBe(401);
        }
      });

      const req = httpMock.expectOne('/auth/login');
      req.flush({ message: 'Credenciales inválidas' }, { status: 401, statusText: 'Unauthorized' });
    });
  });

  describe('logout', () => {
    it('should logout successfully and clear user data', () => {
      // Set initial user
      service.currentUser.set({
        id: 1,
        username: 'testuser',
        email: 'test@example.com',
        createdAt: '2024-03-27T10:00:00'
      });
      service.isAuthenticated.set(true);

      service.logout().subscribe(() => {
        expect(service.currentUser()).toBeNull();
        expect(service.isAuthenticated()).toBe(false);
        expect(routerSpy.navigate).toHaveBeenCalledWith(['/login']);
      });

      const req = httpMock.expectOne('/auth/logout');
      expect(req.request.method).toBe('POST');
      req.flush({ message: 'Sesión cerrada exitosamente' });
    });
  });

  describe('checkAuth', () => {
    it('should check authentication and set user if authenticated', () => {
      const mockUser = {
        id: 1,
        username: 'testuser',
        email: 'test@example.com',
        createdAt: '2024-03-27T10:00:00'
      };

      service.checkAuth();

      const req = httpMock.expectOne('/auth/me');
      expect(req.request.method).toBe('GET');
      req.flush(mockUser);

      expect(service.currentUser()).toEqual(mockUser);
      expect(service.isAuthenticated()).toBe(true);
      expect(service.loading()).toBe(false);
    });

    it('should handle unauthenticated user', () => {
      service.checkAuth();

      const req = httpMock.expectOne('/auth/me');
      req.flush({ message: 'No autenticado' }, { status: 401, statusText: 'Unauthorized' });

      expect(service.currentUser()).toBeNull();
      expect(service.isAuthenticated()).toBe(false);
      expect(service.loading()).toBe(false);
    });
  });

  describe('signals', () => {
    it('should initialize with correct default values', () => {
      expect(service.currentUser()).toBeNull();
      expect(service.isAuthenticated()).toBe(false);
      expect(service.loading()).toBe(true);
    });

    it('should update signals correctly', () => {
      const mockUser = {
        id: 1,
        username: 'testuser',
        email: 'test@example.com',
        createdAt: '2024-03-27T10:00:00'
      };

      service.currentUser.set(mockUser);
      service.isAuthenticated.set(true);
      service.loading.set(false);

      expect(service.currentUser()).toEqual(mockUser);
      expect(service.isAuthenticated()).toBe(true);
      expect(service.loading()).toBe(false);
    });
  });
});
