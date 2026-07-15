import { Component, Inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-contato-dialog',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatCheckboxModule,
    MatIconModule,
  ],
  templateUrl: './contato-dialog.html',
  styleUrl: './contato-dialog.css',
})
export class ContatoDialogComponent implements OnInit {
  formContato!: FormGroup;

  constructor(
    private fb: FormBuilder,
    public dialogRef: MatDialogRef<ContatoDialogComponent>,
    // MAT_DIALOG_DATA permite que receba dados caso seja uma "Edição"
    @Inject(MAT_DIALOG_DATA) public data: any,
  ) {}

  ngOnInit(): void {
    // Formulário
    this.formContato = this.fb.group({
      nome: ['', Validators.required], // obrigatório
      celular: [''],
      favorito: [false],
      grupos: [''],
    });

    // Se veio dados na variável 'data', significa que estamos editando um contato existente
    if (this.data) {
      this.formContato.patchValue(this.data);
    }
  }

  salvar() {
    if (this.formContato.valid) {
      this.dialogRef.close(this.formContato.value);
    }
  }

  cancelar() {
    this.dialogRef.close();
  }
}
