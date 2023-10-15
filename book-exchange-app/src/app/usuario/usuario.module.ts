import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { bookIcon, ClarityIcons } from '@cds/core/icon';
import { ClarityModule } from '@clr/angular';

import { PerfilComponent } from './perfil/perfil.component';
import { UsuarioRoutingModule } from './usuario-routing.module';
import { NotificacaoService } from '../service/notificacao.service';
import { ScrollingModule } from '@angular/cdk/scrolling';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';
import { MatDividerModule } from '@angular/material/divider';
import { MatCardModule } from '@angular/material/card';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatTableModule } from '@angular/material/table';
import { MatTooltipModule } from '@angular/material/tooltip';
import { ConfirmaSolicitacaoTrocaComponent } from './modal/confirma-solicitacao-troca/confirma-solicitacao-troca.component';
import { MatDialogModule } from '@angular/material/dialog';
import { MatTabsModule } from '@angular/material/tabs';

ClarityIcons.addIcons(
  bookIcon
);

@NgModule({
  declarations: [
    PerfilComponent,
    ConfirmaSolicitacaoTrocaComponent
  ],
  imports: [
    CommonModule,
    UsuarioRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    ClarityModule,
    HttpClientModule,
    MatPaginatorModule,
    ScrollingModule,
    MatListModule,
    MatIconModule,
    MatDividerModule,
    MatCardModule,
    MatInputModule,
    MatButtonModule,
    MatTableModule,
    MatTooltipModule,
    MatDialogModule,
    MatTabsModule
  ],
  providers: [
    NotificacaoService
  ]
})
export class UsuarioModule { }
