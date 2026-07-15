import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login';
import { Home } from './pages/home/home';
import { authGuard } from './guards/auth-guard';
import { ContatoForm } from './pages/contato-form/contato-form';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },

  { path: 'home', component: Home, canActivate: [authGuard] },

  { path: 'novo-contato', component: ContatoForm, canActivate: [authGuard] },

  // localhost:4200 -> redireciona para o login
  { path: '', redirectTo: '/login', pathMatch: 'full' },

  // localhost:4200/ um que nao existe -> redireciona para o login
  { path: '**', redirectTo: '/login' },
];
