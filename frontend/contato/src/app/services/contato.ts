import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Contato } from '../models/contato.model';

@Injectable({
  providedIn: 'root'
})
export class ContatoService {
  // URL do java
  private apiUrl = 'http://localhost:8080/contatos';

  constructor(private http: HttpClient) { }

  // GET /contatos
  listarTodos(): Observable<Contato[]> {
    return this.http.get<Contato[]>(this.apiUrl);
  }

  // GET /contatos/{id}
  buscarPorId(id: number): Observable<Contato> {
    return this.http.get<Contato>(`${this.apiUrl}/${id}`);
  }

  // POST /contatos
  criar(contato: Contato): Observable<Contato> {
    return this.http.post<Contato>(this.apiUrl, contato);
  }

  // PATCH /contatos/{id}
  atualizar(id: number, contato: Contato): Observable<void> {
    return this.http.patch<void>(`${this.apiUrl}/${id}`, contato);
  }

  // DELETE /contatos/{id}
  deletar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }


  // PATCH /contatos/{contatoId}/etiquetas/{etiquetaId}
  associarEtiqueta(contatoId: number, etiquetaId: number): Observable<void> {
    return this.http.patch<void>(`${this.apiUrl}/${contatoId}/etiquetas/${etiquetaId}`, {});
  }

  // DELETE /contatos/{contatoId}/etiquetas/{etiquetaId}
  desassociarEtiqueta(contatoId: number, etiquetaId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${contatoId}/etiquetas/${etiquetaId}`);
  }
}
