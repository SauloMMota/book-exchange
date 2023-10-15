import { Component, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { Subject, takeUntil } from 'rxjs';
import { LivroService } from 'src/app/service/livro.service';

@Component({
  selector: 'app-cadastro-livro',
  templateUrl: './cadastro-livro.component.html',
  styleUrls: ['./cadastro-livro.component.scss'],
})
export class CadastroLivroComponent implements OnDestroy {
  formulario!: FormGroup;
  subscription = new Subject<void>();

  constructor(
    private fb: FormBuilder,
    private toastrService: ToastrService,
    private livroService: LivroService
  ) {
    this.montarFormulario();
  }

  ngOnDestroy(): void {
    this.subscription.next()
    this.subscription.complete();
  }

  montarFormulario() {
    this.formulario = this.fb.group({
      titulo: [null, Validators.required],
      autor: [null, Validators.required],
      genero: [null, Validators.required],
      descricao: [null, Validators.required]
    });
  }

  cadastrar() {
    const formValues = this.formulario.value;
    this.livroService.cadastrar(formValues)
    .pipe(takeUntil(this.subscription))
    .subscribe({
      next: (response: any) => {
        const dados: any = response
        this.toastrService.success(`Livro ${dados.titulo} cadastrado com sucesso.`)
        this.formulario.reset()
        this.formulario.markAsPristine();
      },
      error: (erro) => console.log(erro) });
  }


}
