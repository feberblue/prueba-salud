import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of, throwError } from 'rxjs';
import { RegisterComponent } from './register.component';
import { AuthService } from '../../../core/services/auth.service';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let authService: jasmine.SpyObj<AuthService>;
  let router: jasmine.SpyObj<Router>;

  beforeEach(async () => {
    const authServiceSpy = jasmine.createSpyObj('AuthService', ['register']);
    const routerSpy = jasmine.createSpyObj('Router', ['navigate']);

    await TestBed.configureTestingModule({
      imports: [RegisterComponent, ReactiveFormsModule, HttpClientTestingModule],
      providers: [
        { provide: AuthService, useValue: authServiceSpy },
        { provide: Router, useValue: routerSpy }
      ]
    }).compileComponents();

    authService = TestBed.inject(AuthService) as jasmine.SpyObj<AuthService>;
    router = TestBed.inject(Router) as jasmine.SpyObj<Router>;
    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize form with empty values', () => {
    expect(component.registerForm.value).toEqual({
      username: '',
      email: '',
      password: '',
      confirmPassword: ''
    });
  });

  it('should have invalid form when empty', () => {
    expect(component.registerForm.valid).toBeFalsy();
  });

  it('should validate all required fields', () => {
    const form = component.registerForm;
    expect(form.get('username')?.errors?.['required']).toBeTruthy();
    expect(form.get('email')?.errors?.['required']).toBeTruthy();
    expect(form.get('password')?.errors?.['required']).toBeTruthy();
    expect(form.get('confirmPassword')?.errors?.['required']).toBeTruthy();
  });

  it('should validate email format', () => {
    const email = component.registerForm.get('email');
    email?.setValue('invalid-email');
    expect(email?.errors?.['email']).toBeTruthy();
    
    email?.setValue('valid@email.com');
    expect(email?.errors).toBeNull();
  });

  it('should validate username minimum length', () => {
    const username = component.registerForm.get('username');
    username?.setValue('ab');
    expect(username?.errors?.['minlength']).toBeTruthy();
    
    username?.setValue('abc');
    expect(username?.errors).toBeNull();
  });

  it('should validate password minimum length', () => {
    const password = component.registerForm.get('password');
    password?.setValue('12345');
    expect(password?.errors?.['minlength']).toBeTruthy();
    
    password?.setValue('123456');
    expect(password?.errors).toBeNull();
  });

  it('should validate password mismatch', () => {
    component.registerForm.patchValue({
      password: 'password123',
      confirmPassword: 'different123'
    });
    
    expect(component.registerForm.errors?.['passwordMismatch']).toBeTruthy();
  });

  it('should not have password mismatch error when passwords match', () => {
    component.registerForm.patchValue({
      password: 'password123',
      confirmPassword: 'password123'
    });
    
    expect(component.registerForm.errors?.['passwordMismatch']).toBeFalsy();
  });

  it('should have valid form with correct data', () => {
    component.registerForm.setValue({
      username: 'testuser',
      email: 'test@example.com',
      password: 'password123',
      confirmPassword: 'password123'
    });
    
    expect(component.registerForm.valid).toBeTruthy();
  });

  it('should not submit if form is invalid', () => {
    component.onSubmit();
    expect(authService.register).not.toHaveBeenCalled();
  });

  it('should call authService.register on valid form submission', () => {
    const mockResponse = {
      message: 'Usuario registrado exitosamente',
      user: {
        id: 1,
        username: 'testuser',
        email: 'test@example.com',
        createdAt: '2024-03-27T10:00:00'
      }
    };

    authService.register.and.returnValue(of(mockResponse));

    component.registerForm.setValue({
      username: 'testuser',
      email: 'test@example.com',
      password: 'password123',
      confirmPassword: 'password123'
    });

    component.onSubmit();

    expect(authService.register).toHaveBeenCalledWith({
      username: 'testuser',
      email: 'test@example.com',
      password: 'password123'
    });
  });

  it('should navigate to /login on successful registration', () => {
    const mockResponse = {
      message: 'Usuario registrado exitosamente',
      user: {
        id: 1,
        username: 'testuser',
        email: 'test@example.com',
        createdAt: '2024-03-27T10:00:00'
      }
    };

    authService.register.and.returnValue(of(mockResponse));

    component.registerForm.setValue({
      username: 'testuser',
      email: 'test@example.com',
      password: 'password123',
      confirmPassword: 'password123'
    });

    component.onSubmit();

    expect(router.navigate).toHaveBeenCalledWith(['/login']);
  });

  it('should display error message on registration failure', (done) => {
    const errorResponse = {
      error: { message: 'El usuario ya existe' },
      status: 400
    };

    authService.register.and.returnValue(throwError(() => errorResponse));

    component.registerForm.setValue({
      username: 'existinguser',
      email: 'existing@example.com',
      password: 'password123',
      confirmPassword: 'password123'
    });

    component.onSubmit();

    setTimeout(() => {
      expect(component.error).toBe('El usuario ya existe');
      expect(component.loading).toBe(false);
      done();
    }, 100);
  });

  it('should provide getters for form controls', () => {
    expect(component.username).toBe(component.registerForm.get('username'));
    expect(component.email).toBe(component.registerForm.get('email'));
    expect(component.password).toBe(component.registerForm.get('password'));
    expect(component.confirmPassword).toBe(component.registerForm.get('confirmPassword'));
  });
});
