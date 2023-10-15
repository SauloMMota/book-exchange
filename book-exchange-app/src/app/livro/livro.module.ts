import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { LivroRoutingModule } from './livro-routing.module';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatTableModule } from '@angular/material/table';
import { MatFormFieldModule } from '@angular/material/form-field';
import { HttpClientModule } from '@angular/common/http';
import { MatInputModule } from '@angular/material/input';
import { ConsultaLivroComponent } from './consulta-livro/consulta-livro.component';
import { LivroService } from '../service/livro.service';
import { CadastroLivroComponent } from './cadastro-livro/cadastro-livro.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ClarityModule } from '@clr/angular';
import { MatIconModule } from '@angular/material/icon';
import { MatTooltipModule } from '@angular/material/tooltip';
import { ModalAtualizarLivroComponent } from './modal/modal-atualizar-livro/modal-atualizar-livro.component';
import { MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';


@NgModule({
  declarations: [
    ConsultaLivroComponent,
    CadastroLivroComponent,
    ModalAtualizarLivroComponent
  ],
  imports: [
    CommonModule,
    LivroRoutingModule,
    MatPaginatorModule,
    MatCheckboxModule,
    MatTableModule,
    MatFormFieldModule,
    HttpClientModule,
    MatInputModule,
    FormsModule,
    ReactiveFormsModule,
    ClarityModule,
    MatIconModule,
    MatTooltipModule,
    MatDialogModule,
    MatButtonModule
  ],
  providers: [
    LivroService
  ]
})
export class LivroModule { }
