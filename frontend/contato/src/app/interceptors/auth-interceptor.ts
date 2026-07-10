import { HttpInterceptorFn } from '@angular/common/http';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  // Verificação do token
  const token = localStorage.getItem('token');

  // Para login e register nao precisa do token
  if (req.url.includes('/auth/login') || req.url.includes('/auth/register')) {
    return next(req);
  }

  // Rotas Adm, tendo token
  if (token) {
    const authReq = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });

    // Token usado no projeto
    return next(authReq);
  }

  // Se não for apra login/register ou nao tiver adm = 403 pelo backend
  return next(req);
};
