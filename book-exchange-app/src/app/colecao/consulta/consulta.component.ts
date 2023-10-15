import { UsuarioService } from 'src/app/service/usuario.service';
import { SelectionModel } from '@angular/cdk/collections';
import { AfterViewInit, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { Subject, takeUntil, lastValueFrom } from 'rxjs';
import { ColecaoService } from 'src/app/service/colecao.service';
import { ColecaoLivro } from 'src/app/shared/representation/ColecaoLivro';
import { SolicitacaoTrocaComponent } from '../modal/solicitacao-troca/solicitacao-troca.component';
import { VisualizaPerfilComponent } from '../modal/visualiza-perfil/visualiza-perfil.component';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-consulta',
  templateUrl: './consulta.component.html',
  styleUrls: ['./consulta.component.scss'],
})
export class ConsultaComponent implements OnInit, AfterViewInit, OnDestroy {

  colecoes: ColecaoLivro[] = [];
  totalElementos = 0;
  pagina = 0;
  tamanho = 10;
  pageSizeOptions: number[] = [10];
  dataSource: MatTableDataSource<ColecaoLivro>;
  selection = new SelectionModel<ColecaoLivro>(true, []);
  displayedColumns: string[] = [
    'select',
    'idLivro',
    'idUsuario',
    'idColecao',
    'titulo',
    'autor',
    'genero',
    'descricao',
    'informacao',
    'acoes'
  ];
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  subscription = new Subject<void>();
  dadosUsuario: any;

  constructor(private colecaoService: ColecaoService,
              public dialog: MatDialog,
              private usuarioService: UsuarioService) {
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

    this.colecaoService.list(pagina, tamanho)
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

  solicitacaoTroca(colecaoLivro: ColecaoLivro) {
    const dialogRef = this.dialog.open(SolicitacaoTrocaComponent, {
      width: '800px',
      data: colecaoLivro
    });
    dialogRef.afterClosed().subscribe(result => {
      //console.log(`Dialog result: ${result}`);
    });
  }

  async visualizarPerfil(idUsuario: number) {
    await this.buscarUsuario(idUsuario);
    const dialogRef = this.dialog.open(VisualizaPerfilComponent, {
      width: '800px',
      data: this.dadosUsuario
    });
    dialogRef.afterClosed().subscribe(result => {
      //console.log(`Dialog result: ${result}`);
    });
  }

  async buscarUsuario(id: number) {
      await lastValueFrom(this.usuarioService.getUsuario(id))
      .then(response =>  this.dadosUsuario = response)
      .catch(error => console.log(error))
  }
}
