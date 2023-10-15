import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ConsultaComponent } from './consulta/consulta.component';
import { ColecaoPessoalComponent } from './colecao-pessoal/colecao-pessoal.component';

const routes: Routes = [
  { path: 'colecoes', component: ConsultaComponent},
  { path: 'colecao-pessoal', component: ColecaoPessoalComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ColecaoRoutingModule { }
