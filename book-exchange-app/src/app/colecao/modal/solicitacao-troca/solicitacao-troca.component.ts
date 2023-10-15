import { ToastrService } from 'ngx-toastr';
import { GerenciamentoService } from './../../../service/gerenciamento.service';
import { Component, Inject, OnInit } from '@angular/core';
import { Observable, Subject, debounceTime, distinctUntilChanged, switchMap, takeUntil } from 'rxjs';
import { ColecaoService } from 'src/app/service/colecao.service';
import { ColecaoLivro } from 'src/app/shared/representation/ColecaoLivro';
import { Livro } from 'src/app/shared/representation/Livro';
import { Util } from 'src/app/shared/util/util';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { FormControl } from '@angular/forms';

@Component({
  selector: 'modal-solicitacao-troca',
  templateUrl: './solicitacao-troca.component.html',
  styleUrls: ['./solicitacao-troca.component.scss'],
})
export class SolicitacaoTrocaComponent implements OnInit {
  modalAberto!: boolean;
  idUsuario!: number;
  subscription = new Subject<void>();
  livros: Livro[] = [];
  keyword = 'titulo';
  idLivroEmissor!: any;
  colecaoReceptor!: ColecaoLivro;
  informacoes!: string;
  searchControl = new FormControl();
  filteredLivros!: Observable<any[]>;

  constructor(
    private colecaoService: ColecaoService,
    private gerenciamentoService: GerenciamentoService,
    private toastrService: ToastrService,
    public dialogRef: MatDialogRef<SolicitacaoTrocaComponent>,
    @Inject(MAT_DIALOG_DATA) public colecaoLivro: ColecaoLivro
  ) {}

  ngOnInit(): void {
    this.idUsuario = Util.obterCodigoUsuario();
    this.colecaoReceptor = this.colecaoLivro;
    this.filtrarLivrosAutoComplete();
  }

  solicitar() {
    const dadosSolicitacao = {
      idUsuarioEmissor: this.idUsuario,
      idUsuarioReceptor: this.colecaoReceptor.idUsuario,
      idLivroEmissor: this.idLivroEmissor,
      idLivroReceptor: this.colecaoReceptor.idLivro

    }

    this.gerenciamentoService.solicitacao(dadosSolicitacao, this.informacoes)
    .pipe(takeUntil(this.subscription))
    .subscribe({
      next: (response) => {
        this.toastrService.success(`${response.mensagem}`)
        this.modalAberto = false;
      },
      error: (erro) => console.log(erro)
    });
  }

  selecionarLivro(event: any): void {
    const livro =  event.option.value
    this.idLivroEmissor = livro.id;
  }

  displayFn(livro: any): string {
    return livro && livro.titulo ? livro.titulo : '';
  }

  filtrarLivrosAutoComplete(): void {
    this.filteredLivros = this.searchControl.valueChanges.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      switchMap(value => this.colecaoService.getLivrosAutoComplete(this.idUsuario,value)),
    );
  }
}
