import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';

export const routes: Routes = [
  {
    path: 'login',
    loadComponent: () => import('./features/auth/login/login.component').then(m => m.LoginComponent)
  },
  {
    path: 'register',
    loadComponent: () => import('./features/auth/register/register.component').then(m => m.RegisterComponent)
  },
  {
    path: 'solicitudes',
    canActivate: [authGuard],
    loadComponent: () => import('./features/solicitudes/lista-solicitudes/lista-solicitudes.component').then(m => m.ListaSolicitudesComponent)
  },
  {
    path: 'solicitudes/nueva',
    canActivate: [authGuard],
    loadComponent: () => import('./features/solicitudes/nueva-solicitud/nueva-solicitud.component').then(m => m.NuevaSolicitudComponent)
  },
  {
    path: '',
    redirectTo: '/login',
    pathMatch: 'full'
  },
  {
    path: '**',
    redirectTo: '/solicitudes'
  }
];
