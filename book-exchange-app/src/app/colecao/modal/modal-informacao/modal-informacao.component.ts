import { Component, EventEmitter, Inject, Input, OnDestroy, Output, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'modal-informacao',
  templateUrl: './modal-informacao.component.html',
  styleUrls: ['./modal-informacao.component.scss']
})
export class ModalInformacaoComponent implements OnInit, OnDestroy {

  informacao!: string;
  livro: any;

  constructor(
    public dialogRef: MatDialogRef<ModalInformacaoComponent>,
    @Inject(MAT_DIALOG_DATA) public livroRecebido: any
  ) {}

  ngOnInit(): void {
    this.livro = this.livroRecebido;
  }

  ngOnDestroy(): void {
    this.informacao = '';
  }

  adcionarInformacao() {
    const { idUsuario, idLivro } = this.livro;
    const obj = {
      idUsuario: idUsuario,
      idLivro: idLivro,
      informacao: this.informacao
    }
    this.dialogRef.close(obj)
  }

  cancelar() {
    this.informacao = '';
  }
}

