import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';

export const authGuard: CanActivateFn = (route, state) => {
  // Chama o Roteador do Angular
  const router = inject(Router);

  // Verifica o token
  const token = localStorage.getItem('token');

  if (token) {
    return true;
  } else {
    // Se é tela de ADM volta para se logar
    router.navigate(['/login']);
    return false;
  }
};
