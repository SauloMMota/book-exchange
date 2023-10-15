import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Subject, takeUntil } from 'rxjs';
import { AuthService } from 'src/app/service/auth.service';

@Component({
  selector: 'app-cadastro',
  templateUrl: './cadastro.component.html',
  styleUrls: ['./cadastro.component.scss']
})
export class CadastroComponent {
  formulario!: FormGroup;
  subscription = new Subject<void>();

  constructor(private fb: FormBuilder,
              private toastrService: ToastrService,
              private authService: AuthService,
              private router: Router
  ) {
    this.montarFormulario();
  }

  montarFormulario() {
    this.formulario = this.fb.group({
      nome: [null, Validators.required],
      email: [null, [Validators.required, Validators.email]],
      senha: [null, Validators.required]
    });
  }

  cadastrar() {
    const formValues = this.formulario.value;
    this.authService.cadastrar(formValues)
      .pipe(takeUntil(this.subscription))
      .subscribe({
      next: (response) => {
        const dados: any = response
        this.router.navigate(['/login'])
        this.toastrService.success(`Seja bem vindo(a) ${dados.nome}`, "Cadastro realizado com sucesso")
      },
      error: (erro) => console.log(erro) });

  }
}
