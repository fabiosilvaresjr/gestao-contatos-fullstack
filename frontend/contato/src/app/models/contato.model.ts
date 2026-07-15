export interface Contato {
  id?: number;
  nome: string;
  celular: string;
  favorito: boolean;
  etiquetas?: Etiqueta[];
}

export interface Etiqueta {
  id?: number;
  nome: string;
}

export interface Grupo {
  id?: number;
  nome: string;
}

