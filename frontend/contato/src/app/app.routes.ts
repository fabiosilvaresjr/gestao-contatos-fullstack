import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login';
import { Home } from './pages/home/home';
import { authGuard } from './guards/auth-guard';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },

  { path: 'home', component: Home, canActivate: [authGuard] },

  // localhost:4200 -> redireciona para o login
  { path: '', redirectTo: '/login', pathMatch: 'full' },

  // localhost:4200/ uam que nao existe -> redireciona para o login
  { path: '**', redirectTo: '/login' },
];
