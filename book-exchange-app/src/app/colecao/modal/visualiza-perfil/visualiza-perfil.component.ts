import { Subject, takeUntil } from 'rxjs';
import { NotificacaoService } from 'src/app/service/notificacao.service';
import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { PageEvent } from '@angular/material/paginator';
import { Util } from 'src/app/shared/util/util';

@Component({
  selector: 'modal-visualiza-perfil',
  templateUrl: './visualiza-perfil.component.html',
  styleUrls: ['./visualiza-perfil.component.scss'],
})
export class VisualizaPerfilComponent implements OnInit {
  pagina: number = 0;
  tamanhoPagina: number = 10;
  idUsuario!: number;
  comentarios: any;
  totalElements!: number;
  subscription = new Subject<void>();
  comentario!: string;
  idUsuarioAvaliado!: number;
  constructor(
    public dialogRef: MatDialogRef<VisualizaPerfilComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private notificacaoService: NotificacaoService
  ) {}

  ngOnInit(): void {
    this.idUsuarioAvaliado = this.data.id;
    this.idUsuario = Util.obterCodigoUsuario();
    this.buscarComentarios();
  }

  buscarComentarios(pagina: number = 0, tamanho: number = 10): void {
    this.notificacaoService
      .list(pagina, tamanho, this.idUsuarioAvaliado)
      .subscribe({
        next: (response) => {
          this.comentarios = response.content;
          this.totalElements = response.totalElements;
        },
        error: (erro) => console.log(erro),
      });
  }

  paginar(event: PageEvent) {
    this.pagina = event.pageIndex;
    this.buscarComentarios(this.pagina, this.tamanhoPagina);
  }

  avaliar() {
    this.notificacaoService
      .cadastrar(this.idUsuario, this.idUsuarioAvaliado, this.comentario)
      .pipe(takeUntil(this.subscription))
      .subscribe({
        next: (response) => this.buscarComentarios(),
        error: (erro) => console.log(erro),
      });
  }
}
