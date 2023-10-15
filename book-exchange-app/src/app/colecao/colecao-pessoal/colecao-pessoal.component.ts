import { ToastrService } from 'ngx-toastr';
import { ColecaoService } from 'src/app/service/colecao.service';
import { Component, OnInit, ViewChild, OnDestroy } from '@angular/core';
import { Subject, takeUntil } from 'rxjs';
import { ColecaoLivro } from 'src/app/shared/representation/ColecaoLivro';
import { MatTableDataSource } from '@angular/material/table';
import { SelectionModel } from '@angular/cdk/collections';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { Util } from 'src/app/shared/util/util';
import { ModalInformacaoComponent } from '../modal/modal-informacao/modal-informacao.component';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-colecao-pessoal',
  templateUrl: './colecao-pessoal.component.html',
  styleUrls: ['./colecao-pessoal.component.scss']
})
export class ColecaoPessoalComponent implements OnInit, OnDestroy {

  colecoes: ColecaoLivro[] = [];
  totalElementos = 0;
  pagina = 0;
  tamanho = 10;
  pageSizeOptions: number[] = [10];
  dataSource: MatTableDataSource<ColecaoLivro>;
  selection = new SelectionModel<ColecaoLivro>(true, []);
  displayedColumns: string[] = [
    'select',
    'titulo',
    'autor',
    'genero',
    'descricao',
    'informacao',
    'acoes'
  ];
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  subscription = new Subject<void>();
  idUsuario!: number;
  @ViewChild(ModalInformacaoComponent, {static: false})
    modalInformacao!: ModalInformacaoComponent;

  constructor(private colecaoService: ColecaoService,
              private toastrService: ToastrService,
              public dialog: MatDialog) {
    this.dataSource = new MatTableDataSource();
  }

  ngOnInit(): void {
    this.idUsuario = Util.obterCodigoUsuario();
    if(this.idUsuario) {
      this.listarColecaoPessoal(this.pagina, this.tamanho, this.idUsuario);
    }
  }

  ngOnDestroy(): void {
    this.subscription.next()
    this.subscription.complete();
  }

  listarColecaoPessoal(pagina: number = 0, tamanho: number = 10, idUsuario: number) {

    this.colecaoService.getColecaoPessoal(pagina, tamanho, idUsuario)
    .pipe(takeUntil(this.subscription))
      .subscribe((response) => {
      this.colecoes = response.content;
      this.dataSource = new MatTableDataSource(this.colecoes);
      this.totalElementos = response.totalElements;
      this.pagina = response.number;

    });
  }

  paginar(event: PageEvent) {
    this.pagina = event.pageIndex;
    this.listarColecaoPessoal(this.pagina, this.tamanho, this.idUsuario);
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  /** Whether the number of selected elements matches the total number of rows. */
  isAllSelected() {
    const numSelected = this.selection.selected.length;
    const numRows = this.dataSource.data.length;
    return numSelected === numRows;
  }

  /** Selects all rows if they are not all selected; otherwise clear selection. */
  toggleAllRows() {
    if (this.isAllSelected()) {
      this.selection.clear();
      return;
    }

    this.selection.select(...this.dataSource.data);
  }

  abrirModal(livro: ColecaoLivro) {
    //this.modalInformacao.abrirModal(livro);
    const dialogRef = this.dialog.open(ModalInformacaoComponent, {
      width: '800px',
      data: livro
    });
    dialogRef.afterClosed().subscribe(result => {
      this.incluirInformacao(result)
    });
  }

  incluirInformacao(dados:any) {
    const { idUsuario, idLivro, informacao} = dados

    this.colecaoService
    .incluirInformacao(idUsuario, idLivro, informacao)
    .pipe(takeUntil(this.subscription))
    .subscribe({
       next: (response) => {
        this.toastrService.success(`${response.mensagem}`)
        this.listarColecaoPessoal(this.pagina, this.tamanho, this.idUsuario);
       },
       error: (erro) => console.log(erro)
    });
  }

  removerLivrosColecao() {
    if(this.selection.selected.length != 0) {
      const selecionados: Array<number> = this.selection
              .selected.map(selecionado => selecionado.idLivro);
      this.chamarServicoRemover(selecionados);
    } else {
      this.toastrService.warning('É necessário selecionar pelo menos 1 livro.', `Atenção`)
    }
  }

  chamarServicoRemover(selecionados: Array<number>) {
    this.colecaoService
      .removerLivrosDaColecao(this.idUsuario, selecionados)
      .pipe(takeUntil(this.subscription))
      .subscribe({
        next: (response: any) => {
          this.toastrService.success(`${response.mensagem}`)
          this.listarColecaoPessoal(this.pagina, this.tamanho, this.idUsuario);
        },
        error: (erro) => console.log(erro)
     });
  }

}
