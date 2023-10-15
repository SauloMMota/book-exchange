import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatTableModule } from '@angular/material/table';
import { MatTabsModule } from '@angular/material/tabs';
import { MatTooltipModule } from '@angular/material/tooltip';
import { addTextIcon, ClarityIcons, libraryIcon } from '@cds/core/icon';
import { ClarityModule } from '@clr/angular';

import { ColecaoService } from '../service/colecao.service';
import { GerenciamentoService } from '../service/gerenciamento.service';
import { ColecaoPessoalComponent } from './colecao-pessoal/colecao-pessoal.component';
import { ColecaoRoutingModule } from './colecao-routing.module';
import { ConsultaComponent } from './consulta/consulta.component';
import { ModalInformacaoComponent } from './modal/modal-informacao/modal-informacao.component';
import { SolicitacaoTrocaComponent } from './modal/solicitacao-troca/solicitacao-troca.component';
import { VisualizaPerfilComponent } from './modal/visualiza-perfil/visualiza-perfil.component';

ClarityIcons.addIcons(
  addTextIcon,
  libraryIcon
);

@NgModule({
  declarations: [
    ConsultaComponent,
    ColecaoPessoalComponent,
    ModalInformacaoComponent,
    SolicitacaoTrocaComponent,
    VisualizaPerfilComponent
  ],
  imports: [
    CommonModule,
    ColecaoRoutingModule,
    MatPaginatorModule,
    MatCheckboxModule,
    MatTableModule,
    MatFormFieldModule,
    HttpClientModule,
    MatInputModule,
    ClarityModule,
    FormsModule,
    MatIconModule,
    MatButtonModule,
    MatTooltipModule,
    MatAutocompleteModule,
    MatDialogModule,
    ReactiveFormsModule,
    MatTabsModule,
    MatCardModule
  ],
  providers: [
    ColecaoService,
    GerenciamentoService
  ]
})
export class ColecaoModule { }
