import { Component } from '@angular/core';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent {
  constructor() {

  }

  sair() {
    localStorage.removeItem('access_token');
    localStorage.removeItem('usuario');
  }


}
