import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { LoginRequest, LoginResponse } from '../models/auth.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  // API Java rodando localmente
  private apiUrl = 'http://localhost:8080/auth';

  // HttpClient no construtor para fazer requisições HTTP
  constructor(private http: HttpClient) { }

  // Envia o login/senha para o backend
  login(credentials: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, credentials).pipe(
      tap(response => {
        // 'tap' intercepta a resposta de sucesso.
        // Token é salvo no LocalStorage do navegador
        if (response && response.token) {
          localStorage.setItem('token', response.token);
        }
      })
    );
  }

  // Forma de usar o token guardado
  getToken(): string | null {
    return localStorage.getItem('token');
  }

  // Metodo para fazer o logout, limpando o token
  logout(): void {
    localStorage.removeItem('token');
  }
}
