import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { authGuard } from './auth.guard';
import { CadastroComponent } from './cadastro/cadastro.component';
import { HomeComponent } from './home/home.component';
import { LayoutComponent } from './layout/layout.component';
import { LoginComponent } from './login/login.component';

const routes: Routes = [
  {path: 'login', component: LoginComponent},
  {path: '', redirectTo: '/login', pathMatch: 'full'},
  {path: 'cadastro-usuario', component: CadastroComponent},
  {path: '', component: LayoutComponent, children: [
    {path: 'home', component: HomeComponent, canActivate: [authGuard]},
    {path: '', loadChildren: () => import('./colecao/colecao.module').then(m => m.ColecaoModule), canActivate: [authGuard]},
    {path: '', loadChildren: () => import('./livro/livro.module').then(m => m.LivroModule), canActivate: [authGuard]},
    {path: '', loadChildren: () => import('./usuario/usuario.module').then(m => m.UsuarioModule), canActivate: [authGuard]}
  ]},
  {path: '', redirectTo: '', pathMatch: 'full'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
