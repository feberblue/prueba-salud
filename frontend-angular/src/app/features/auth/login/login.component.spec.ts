import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of, throwError } from 'rxjs';
import { LoginComponent } from './login.component';
import { AuthService } from '../../../core/services/auth.service';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authService: jasmine.SpyObj<AuthService>;
  let router: jasmine.SpyObj<Router>;

  beforeEach(async () => {
    const authServiceSpy = jasmine.createSpyObj('AuthService', ['login']);
    const routerSpy = jasmine.createSpyObj('Router', ['navigate']);

    await TestBed.configureTestingModule({
      imports: [LoginComponent, ReactiveFormsModule, HttpClientTestingModule],
      providers: [
        { provide: AuthService, useValue: authServiceSpy },
        { provide: Router, useValue: routerSpy }
      ]
    }).compileComponents();

    authService = TestBed.inject(AuthService) as jasmine.SpyObj<AuthService>;
    router = TestBed.inject(Router) as jasmine.SpyObj<Router>;
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize form with empty values', () => {
    expect(component.loginForm.value).toEqual({
      username: '',
      password: ''
    });
  });

  it('should have invalid form when empty', () => {
    expect(component.loginForm.valid).toBeFalsy();
  });

  it('should validate username as required', () => {
    const username = component.loginForm.get('username');
    expect(username?.valid).toBeFalsy();
    expect(username?.errors?.['required']).toBeTruthy();
  });

  it('should validate password as required', () => {
    const password = component.loginForm.get('password');
    expect(password?.valid).toBeFalsy();
    expect(password?.errors?.['required']).toBeTruthy();
  });

  it('should validate username minimum length', () => {
    const username = component.loginForm.get('username');
    username?.setValue('ab');
    expect(username?.errors?.['minlength']).toBeTruthy();
  });

  it('should validate password minimum length', () => {
    const password = component.loginForm.get('password');
    password?.setValue('12345');
    expect(password?.errors?.['minlength']).toBeTruthy();
  });

  it('should have valid form with correct data', () => {
    component.loginForm.setValue({
      username: 'testuser',
      password: 'password123'
    });
    expect(component.loginForm.valid).toBeTruthy();
  });

  it('should not submit if form is invalid', () => {
    component.onSubmit();
    expect(authService.login).not.toHaveBeenCalled();
  });

  it('should call authService.login on valid form submission', () => {
    const mockResponse = {
      message: 'Inicio de sesión exitoso',
      user: {
        id: 1,
        username: 'testuser',
        email: 'test@example.com',
        createdAt: '2024-03-27T10:00:00'
      }
    };

    authService.login.and.returnValue(of(mockResponse));

    component.loginForm.setValue({
      username: 'testuser',
      password: 'password123'
    });

    component.onSubmit();

    expect(authService.login).toHaveBeenCalledWith({
      username: 'testuser',
      password: 'password123'
    });
  });

  it('should navigate to /solicitudes on successful login', () => {
    const mockResponse = {
      message: 'Inicio de sesión exitoso',
      user: {
        id: 1,
        username: 'testuser',
        email: 'test@example.com',
        createdAt: '2024-03-27T10:00:00'
      }
    };

    authService.login.and.returnValue(of(mockResponse));

    component.loginForm.setValue({
      username: 'testuser',
      password: 'password123'
    });

    component.onSubmit();

    expect(router.navigate).toHaveBeenCalledWith(['/solicitudes']);
  });

  it('should set loading to true during login', () => {
    authService.login.and.returnValue(of({
      message: 'Inicio de sesión exitoso',
      user: {
        id: 1,
        username: 'testuser',
        email: 'test@example.com',
        createdAt: '2024-03-27T10:00:00'
      }
    }));

    component.loginForm.setValue({
      username: 'testuser',
      password: 'password123'
    });

    component.onSubmit();
    expect(component.loading).toBe(true);
  });

  it('should set loading to false after successful login', (done) => {
    authService.login.and.returnValue(of({
      message: 'Inicio de sesión exitoso',
      user: {
        id: 1,
        username: 'testuser',
        email: 'test@example.com',
        createdAt: '2024-03-27T10:00:00'
      }
    }));

    component.loginForm.setValue({
      username: 'testuser',
      password: 'password123'
    });

    component.onSubmit();

    setTimeout(() => {
      expect(component.loading).toBe(false);
      done();
    }, 100);
  });

  it('should display error message on login failure', (done) => {
    const errorResponse = {
      error: { message: 'Credenciales inválidas' },
      status: 401
    };

    authService.login.and.returnValue(throwError(() => errorResponse));

    component.loginForm.setValue({
      username: 'testuser',
      password: 'wrongpassword'
    });

    component.onSubmit();

    setTimeout(() => {
      expect(component.error).toBe('Credenciales inválidas');
      expect(component.loading).toBe(false);
      done();
    }, 100);
  });

  it('should display generic error message when no message in error', (done) => {
    const errorResponse = {
      error: {},
      status: 500
    };

    authService.login.and.returnValue(throwError(() => errorResponse));

    component.loginForm.setValue({
      username: 'testuser',
      password: 'password123'
    });

    component.onSubmit();

    setTimeout(() => {
      expect(component.error).toBe('Error al iniciar sesión');
      expect(component.loading).toBe(false);
      done();
    }, 100);
  });

  it('should provide getter for username control', () => {
    expect(component.username).toBe(component.loginForm.get('username'));
  });

  it('should provide getter for password control', () => {
    expect(component.password).toBe(component.loginForm.get('password'));
  });
});
