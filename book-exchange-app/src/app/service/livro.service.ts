import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Livro } from '../shared/representation/Livro';

@Injectable({
  providedIn: 'root'
})
export class LivroService {

  urlBase: string = environment.apiBaseUrl;

  constructor(private httpClient: HttpClient) { }

  list(pagina: number, tamanho:number): Observable<any> {
    const params = new HttpParams()
    .set('pagina', pagina)
    .set('tamanhoPagina', tamanho)
    return this.httpClient.get<any>(`${this.urlBase}/api/v1/livros?${params.toString()}`);
  }

  cadastrar(dados: any): Observable<any> {
    return this.httpClient.post<any>(`${this.urlBase}/api/v1/livros`,  dados);
  }

  getLivro(id: number): Observable<any> {
    const params = new HttpParams()
    .set('id', id)

    return this.httpClient.get<any>(`${this.urlBase}/api/v1/livros?${params.toString()}`);
  }

  deletar(id: number): Observable<any> {
    const params = new HttpParams()
    .set('id', id)

    return this.httpClient.delete<any>(`${this.urlBase}/api/v1/livros/deletar?${params.toString()}`);
  }

  atualizar(livro: Livro): Observable<any> {
    const params = new HttpParams()
    .set('id', livro.id)
    return this.httpClient.put<any>(`${this.urlBase}/api/v1/livros/atualizar?${params.toString()}`, livro)
  }

}
