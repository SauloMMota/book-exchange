import { NgModule, Component } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ConsultaLivroComponent } from './consulta-livro/consulta-livro.component';
import { CadastroLivroComponent } from './cadastro-livro/cadastro-livro.component';

const routes: Routes = [
  { path: 'consulta-livros', component: ConsultaLivroComponent },
  { path: 'cadastro-livro', component: CadastroLivroComponent }
  ];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class LivroRoutingModule { }
