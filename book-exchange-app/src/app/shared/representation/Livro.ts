export class Livro {
    id!: number;
    titulo!: string;
    autor!: string;
    genero!: string;
    descricao!: string;

    construtor(id: number,
      titulo: string,
      autor: string,
      genero: string,
      descricao: string) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.genero = genero;
        this.descricao = descricao;
    }
}

