import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'modal-confirma-solicitacao-troca',
  templateUrl: './confirma-solicitacao-troca.component.html',
  styleUrls: ['./confirma-solicitacao-troca.component.scss'],
})
export class ConfirmaSolicitacaoTrocaComponent implements OnInit {
  modalAberto!: boolean;
  dados!: any;
  informacoes!: string;
  idSolicitacao!: number;

  constructor(
    public dialogRef: MatDialogRef<ConfirmaSolicitacaoTrocaComponent>,
    @Inject(MAT_DIALOG_DATA) public dadosSolicitacao: any
  ) {}

  ngOnInit(): void {
    this.idSolicitacao = this.dadosSolicitacao.idSolicitacao;
  }

  confirmar(): void {
    this.dialogRef.close(this.idSolicitacao);
  }
}
