import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  usuario: any;

  ngOnInit(): void {
    const dados = localStorage.getItem('usuario');
    if(dados) {
      this.usuario = JSON.parse(dados);
    }
  }

}
