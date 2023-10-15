import { ToastrService } from 'ngx-toastr';
import { Subject, takeUntil } from 'rxjs';
import {
  Component,
  EventEmitter,
  Output,
  OnDestroy,
  Inject,
} from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { LivroService } from 'src/app/service/livro.service';
import { Livro } from 'src/app/shared/representation/Livro';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'modal-atualizar-livro',
  templateUrl: './modal-atualizar-livro.component.html',
  styleUrls: ['./modal-atualizar-livro.component.scss'],
})
export class ModalAtualizarLivroComponent implements OnDestroy {
  modalAberto!: boolean;
  formulario!: FormGroup;
  subscription = new Subject<void>();
  @Output() confirmar = new EventEmitter();

  constructor(
    private fb: FormBuilder,
    private livroService: LivroService,
    public dialogRef: MatDialogRef<ModalAtualizarLivroComponent>,
    @Inject(MAT_DIALOG_DATA) public livroRecebido: Livro,
    private toastrService: ToastrService
  ) {
    this.montarFormulario(livroRecebido);
  }

  ngOnDestroy(): void {
    this.subscription.next();
    this.subscription.complete();
  }

  montarFormulario(livro: any) {
    this.formulario = this.fb.group({
      idHidden: [{ value: livro?.id, disabled: true }],
      id: [livro?.id, Validators.required],
      titulo: [livro?.titulo, Validators.required],
      autor: [livro?.autor, Validators.required],
      genero: [livro?.genero, Validators.required],
      descricao: [livro?.descricao, Validators.required],
    });
  }

  cadastrar() {
    this.modalAberto = false;
    const formValues = this.formulario.value;
    this.atualizarLivro(formValues);
  }

  atualizarLivro(livro: Livro): void {
    this.livroService
      .atualizar(livro)
      .pipe(takeUntil(this.subscription))
      .subscribe({
        next: (response) => {
          this.toastrService.success('Livro atualizado com sucesso.');
        },
        error: (erro) => console.log(erro),
      });
  }
}
