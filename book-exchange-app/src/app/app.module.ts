import '@cds/core/icon/register.js';

import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {
  addTextIcon,
  angleIcon,
  boltIcon,
  bookIcon,
  bugIcon,
  certificateIcon,
  ClarityIcons,
  cogIcon,
  flameIcon,
  libraryIcon,
  searchIcon,
  userIcon,
} from '@cds/core/icon';
import { ClarityModule } from '@clr/angular';
import { provideToastr } from 'ngx-toastr';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LayoutComponent } from './layout/layout.component';
import { LoginComponent } from './login/login.component';
import { NavbarComponent } from './template/navbar/navbar.component';
import { SidebarComponent } from './template/sidebar/sidebar.component';
import { TokenInterceptor } from './token.interceptor';
import { MatGridListModule } from '@angular/material/grid-list';
import { CadastroComponent } from './cadastro/cadastro.component';
import { MatButtonModule } from '@angular/material/button';
import { HomeComponent } from './home/home.component';

ClarityIcons.addIcons(
  userIcon,
  cogIcon,
  angleIcon,
  flameIcon,
  boltIcon,
  certificateIcon,
  bugIcon,
  bookIcon,
  searchIcon,
  libraryIcon,
  addTextIcon
);

@NgModule({
  declarations: [
    AppComponent,
    SidebarComponent,
    NavbarComponent,
    LayoutComponent,
    LoginComponent,
    CadastroComponent,
    HomeComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ClarityModule,
    BrowserModule,
    BrowserAnimationsModule,
    ReactiveFormsModule,
    HttpClientModule,
    MatGridListModule,
    MatButtonModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor,
      multi: true
    },
    provideToastr({
      timeOut: 10000,
      positionClass: 'toast-top-right',
      preventDuplicates: true,
    })
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
