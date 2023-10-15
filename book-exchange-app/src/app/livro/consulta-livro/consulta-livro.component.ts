import { ToastrService } from 'ngx-toastr';
import { SelectionModel } from '@angular/cdk/collections';
import { AfterViewInit, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { Subject, takeUntil } from 'rxjs';
import { ColecaoService } from 'src/app/service/colecao.service';
import { LivroService } from 'src/app/service/livro.service';
import { Livro } from 'src/app/shared/representation/Livro';
import { Util } from 'src/app/shared/util/util';
import { ModalAtualizarLivroComponent } from '../modal/modal-atualizar-livro/modal-atualizar-livro.component';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-consulta-livro',
  templateUrl: './consulta-livro.component.html',
  styleUrls: ['./consulta-livro.component.scss']
})
export class ConsultaLivroComponent  implements OnInit, AfterViewInit, OnDestroy {


  colecoes: Livro[] = [];
  totalElementos = 0;
  pagina = 0;
  tamanho = 10;
  pageSizeOptions: number[] = [10];
  dataSource: MatTableDataSource<Livro>;
  selection = new SelectionModel<Livro>(true, []);
  displayedColumns: string[] = [
    'select',
    'id',
    'titulo',
    'autor',
    'genero',
    'descricao',
    'acoes'
  ];
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  subscription = new Subject<void>();

  constructor(private livroService: LivroService,
    private colecaoService: ColecaoService,
    private toastrService: ToastrService,
    public dialog: MatDialog) {
    this.dataSource = new MatTableDataSource();
  }

  ngOnInit(): void {
    this.listarLivrosColecoes(this.pagina, this.tamanho);
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
  }

  ngOnDestroy(): void {
    this.subscription.next()
    this.subscription.complete();
  }

  listarLivrosColecoes(pagina: number = 0, tamanho: number = 10) {

    this.livroService.list(pagina, tamanho)
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
    this.listarLivrosColecoes(this.pagina, this.tamanho);
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

  adicionarNaColecao() {
    if(this.selection.selected.length != 0) {
      const selecionados: Array<number> = this.selection
              .selected.map(selecionado => selecionado.id);
      const idUsuario: number = Util.obterCodigoUsuario();
      this.adicionarLivrosNaColecaoPessoal(idUsuario, selecionados);
    }

  }

  adicionarLivrosNaColecaoPessoal(idUsuario: number, selecionados: Array<number>) {
    this.colecaoService
      .incluirLivrosNaColecao(idUsuario, selecionados)
      .pipe(takeUntil(this.subscription))
      .subscribe({
        next: (response: any) => {
          this.toastrService.success(`${response.mensagem}`)
        },
        error: (erro) => console.log(erro)
     });
  }

  deletarLivro(livro: Livro): void {
    this.livroService.deletar(livro.id)
      .pipe(takeUntil(this.subscription))
      .subscribe({
        next: (response) => {
          this.listarLivrosColecoes();
          this.toastrService.success('Livro deletado com sucesso!');
        }, error: (erro) => console.log(erro)
      })
  }

  abrirModal(livro:Livro): void {
    const dialogRef = this.dialog.open(ModalAtualizarLivroComponent, {
      width: '800px',
      data: livro
    });
    dialogRef.afterClosed().subscribe(result => {
      this.listarLivrosColecoes();
    });
  }

}
