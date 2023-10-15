import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { PageEvent } from '@angular/material/paginator';
import { ToastrService } from 'ngx-toastr';
import { Subject, takeUntil } from 'rxjs';
import { GerenciamentoService } from 'src/app/service/gerenciamento.service';
import { NotificacaoService } from 'src/app/service/notificacao.service';
import { UsuarioService } from 'src/app/service/usuario.service';

import { ConfirmaSolicitacaoTrocaComponent } from '../modal/confirma-solicitacao-troca/confirma-solicitacao-troca.component';

@Component({
  selector: 'app-perfil',
  templateUrl: './perfil.component.html',
  styleUrls: ['./perfil.component.scss']
})
export class PerfilComponent implements OnInit, OnDestroy {

  formulario!: FormGroup;
  subscription = new Subject<void>();
  dadosUsuario: any;
  pagina: number = 0;
  tamanhoPagina: number = 10;
  idUsuario!: number;
  comentarios!: any;
  totalElements: any;
  solicitacoes: any;
  displayedColumns = ['nomeEmissor', 'livroEmissor', 'livroReceptor', 'status', 'acoes'];
  hide = true;

  constructor(private fb: FormBuilder,
    private toastrService: ToastrService,
    private usuariService: UsuarioService,
    private notificacaoService: NotificacaoService,
    private gerenciamentoService: GerenciamentoService,
    public dialog: MatDialog
    ) {
    this.montarFormulario(null);
  }

  ngOnInit(): void {
    const dadosStr: any = localStorage.getItem('usuario');
    if(dadosStr) {
      this.dadosUsuario = JSON.parse(dadosStr);
      this.idUsuario = this.dadosUsuario.id
      this.buscarUsuario(this.dadosUsuario.id);
      this.buscarSolicitacoes(this.idUsuario);
      this.buscarComentarios();
    }
  }

  ngOnDestroy(): void {
    this.subscription.next()
    this.subscription.complete();
  }

  montarFormulario(dados: any) {
    this.formulario = this.fb.group({
      nome: [dados?.nome, Validators.required],
      email: [dados?.email, [Validators.required, Validators.email]],
      senha: [dados?.senha, Validators.required]
    });
  }

  alterar() {
    const formValues = this.formulario.value;
    this.usuariService.atualizar(this.dadosUsuario.id ,formValues)
      .pipe(takeUntil(this.subscription))
      .subscribe({
        next: (response)=> {
          this.toastrService.success('Perfil atualizado com sucesso.');
          this.buscarUsuario(response.id);
        },
        error: (erro) => console.log(erro)
      })
  }

  buscarUsuario(id: number) {
    this.usuariService.getUsuario(id).subscribe({
      next: (response) => {
        const dados: any = response
        this.montarFormulario(dados);
      },
      error: (erro) => console.log(erro) });
  }



  buscarComentarios(pagina: number = 0, tamanho: number = 10): void {
    this.notificacaoService
    .list(this.pagina, this.tamanhoPagina, this.idUsuario)
    .subscribe({
      next: (response) => {
        this.comentarios = response.content;
        this.totalElements = response.totalElements;
      },
      error: (erro) => console.log(erro)
    })
  }

  paginar(event: PageEvent) {
    this.pagina = event.pageIndex;
    this.buscarComentarios(this.pagina, this.tamanhoPagina);
  }

  buscarSolicitacoes(idUsuario: number) {
    this.gerenciamentoService
    .solicitacoes(idUsuario)
    .subscribe({
      next: (response) => {
        this.solicitacoes = response;
      },
      error: (erro) => console.log(erro)
    })
  }

  abrirModalConfirmacao(solicitacao: any) {
    const dialogRef = this.dialog.open(ConfirmaSolicitacaoTrocaComponent, {
      width: '800px',
      data: solicitacao
    });
    dialogRef.afterClosed().subscribe(result => {
      this.confirmarSolicitacao(result)
    });
  }

  status(codigoStatus: number) {
    const nomeStatus: any = {
      1: 'PENDENTE',
      2: 'CONFIRMADO'
    }
    return nomeStatus[codigoStatus] || null;
  }

  confirmarSolicitacao(idSolicitacao: number) {
    this.gerenciamentoService
      .confirmacao(idSolicitacao)
      .subscribe({
        next: (response) => {
          this.toastrService.success(`${response.mensagem}`);
          this.buscarSolicitacoes(this.idUsuario);
        },
        error: (erro) => console.log(erro),
      });
  }

}
