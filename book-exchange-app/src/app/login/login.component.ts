import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';

import { AuthService } from '../service/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent {

  formulario!: FormGroup;
  mensagemErro!: string;

  constructor(private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private toastrService: ToastrService) {
    this.montarFormulario();
  }

  montarFormulario() {
    this.formulario = this.fb.group({
      email: [null, [Validators.required, Validators.email]],
      senha: [null, Validators.required],
    });
  }

  async submit() {

    const formValues = this.formulario.value;

    this.authService.logar(formValues).subscribe({next: (response) => {
        const dados: any = response;
        if(!dados.mensagemErro) {
          this.obterToken();
          const dadosUsuario: any = {
            id: dados.id,
            nome: dados.nome
          }

          localStorage.setItem('usuario', JSON.stringify(dadosUsuario));

        } else {
          this.toastrService.error(dados.mensagemErro, 'Ocorreu um erro: ');
        }
      }, error: (erro) => console.log("Erro: ", erro)
    });
  }

  obterToken() {
    this.authService.obterToken().subscribe({next: (response) => {
        const access_token: string = JSON.stringify(response);
        localStorage.setItem('access_token', access_token);
        this.router.navigate(['home']);
      }, error: (erro) => console.log(erro)})
  }

}
