import { Component, OnInit} from '@angular/core';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatListModule } from '@angular/material/list';
import { MatExpansionModule } from '@angular/material/expansion';
import { Router, RouterModule } from '@angular/router';
import { ContatoService } from '../../services/contato';
import { CommonModule } from '@angular/common';

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
    RouterModule,
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

  excluir(id: number) {
    // Confirm já é da web
    if (confirm('Tem certeza que deseja excluir este contato?')) {
      this.contatoService.deletar(id).subscribe({
        next: () => {
          console.log('Contato excluído com sucesso!');
          this.carregarContatos();
        },
        error: (erro: any) => {
          console.error('Erro ao excluir o contato', erro);
        },
      });
    }
  }

  sair() {
    localStorage.removeItem('token');
    this.router.navigate(['/login']);
  }
}
