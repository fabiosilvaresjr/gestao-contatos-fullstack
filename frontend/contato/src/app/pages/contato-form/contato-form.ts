import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ContatoService } from '../../services/contato';

// Angular Material para Dashboard
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatExpansionModule } from '@angular/material/expansion';

@Component({
  selector: 'app-contato-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatCheckboxModule,
    MatIconModule,
    MatCardModule,
    MatExpansionModule,
  ],
  templateUrl: './contato-form.html',
  styleUrl: './contato-form.css',
})
export class ContatoForm implements OnInit {
  formContato!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private contatoService: ContatoService,
  ) {}

  ngOnInit(): void {
    this.formContato = this.fb.group({
      nome: ['', Validators.required],
      celular: [''],
      favorito: [false],
      grupos: [''],
    });
  }

  salvar() {
    if (this.formContato.valid) {
      const contatoParaSalvar = {
        nome: this.formContato.value.nome,
        celular: this.formContato.value.celular,
        favorito: this.formContato.value.favorito,
      };

      this.contatoService.criar(contatoParaSalvar).subscribe({
        next: () => {
          console.log('Contato salvo com sucesso!');
          this.router.navigate(['/home']);
        },
        error: (erro: any) => {
          console.error('Erro ao salvar o contato', erro);
        },
      });
    }
  }

  cancelar() {
    this.router.navigate(['/home']);
  }
}
