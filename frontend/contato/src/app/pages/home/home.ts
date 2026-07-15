import { Component, OnInit, ViewChild, AfterViewInit } from '@angular/core';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatListModule } from '@angular/material/list';
import { MatExpansionModule } from '@angular/material/expansion';
import { Router } from '@angular/router';
import { ContatoService } from '../../services/contato';
import { CommonModule } from '@angular/common';

import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { ContatoDialogComponent } from './contato-dialog/contato-dialog';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
    MatTableModule,
    MatSidenavModule,
    MatListModule,
    MatExpansionModule,
    CommonModule,
    MatDialogModule,
  ],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
// MatSidenavModule (barra fixa na esquerda), MatListModule, MatExpansionModule (paineis de ver mais)
export class Home implements OnInit {
  colunas: string[] = ['nome', 'celular', 'favorito', 'etiquetas', 'acoes'];

  contatos: any[] = [];

  constructor(
    private router: Router,
    private contatoService: ContatoService,
    private dialog: MatDialog,
  ) {}

  ngOnInit() {
    this.carregarContatos();
  }

  carregarContatos() {
    this.contatoService.listarTodos().subscribe({
      next: (dados: any[]) => {
        this.contatos = dados;
      },
      error: (erro: any) => {
        console.error('Erro ao buscar contatos no banco', erro);
      },
    });
  }

  abrirModalNovo() {
    const dialogRef = this.dialog.open(ContatoDialogComponent, {
      width: '400px',
    });

    dialogRef.afterClosed().subscribe((resultado) => {
      // Se o usuário preencheu e clicou em Salvar
      if (resultado) {

        const contatoParaSalvar = {
          nome: resultado.nome,
          celular: resultado.celular,
          favorito: resultado.favorito,
        };

        this.contatoService.criar(contatoParaSalvar).subscribe({
          next: () => {
            console.log('Contato salvo com sucesso!');
            this.carregarContatos(); // Puxa os dados novos do banco pra tabela
          },
          error: (erro: any) => {
            console.error('Erro ao salvar o contato', erro);
          },
        });
      }
    });
  }

  sair() {
    localStorage.removeItem('token');
    this.router.navigate(['/login']);
  }
}
