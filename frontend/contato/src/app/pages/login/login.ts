import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { AuthService } from '../../services/auth';
import { LoginRequest } from '../../models/auth.model';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    FormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSnackBarModule,
  ],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class LoginComponent {
  // VGuarda o que o usuário digitar
  credentials: LoginRequest = { login: '', password: '' };

  constructor(
    private authService: AuthService,
    private router: Router,
    private snackBar: MatSnackBar, // Ferramenta do Material para mostrar alertas (pop-ups)
  ) {}

  onSubmit() {
    // Chama o nosso service que bate no http://localhost:8080/auth/login
    this.authService.login(this.credentials).subscribe({
      next: () => {
        // Se o Spring Boot devolver o Token (sucesso), o roteador joga o usuário pra Home!
        this.router.navigate(['/home']);
      },
      error: () => {
        // Se o Spring Boot der erro (ex: 403), mostra um alerta vermelho na tela
        this.snackBar.open('Login ou senha incorretos!', 'Fechar', { duration: 3000 });
      },
    });
  }
}
