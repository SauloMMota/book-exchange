import { ColecaoLivroPage } from 'src/app/shared/representation/ColecaoLivroPage';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ColecaoService {

  urlBase: string = environment.apiBaseUrl;

  constructor(private httpClient: HttpClient) { }

  list(pagina: number, tamanho:number): Observable<any> {
    const params = new HttpParams()
    .set('page', pagina)
    .set('size', tamanho)
    return this.httpClient.get<any>(`${this.urlBase}/api/v1/livros/colecoes?${params.toString()}`);
  }

  incluirLivrosNaColecao(idUsuario: number, idsLivros: Array<number>):  Observable<any> {

    const params = new HttpParams()
    .set('idUsuario', idUsuario)
    return this.httpClient.post<any>(`${this.urlBase}/api/v1/livros/colecao?${params.toString()}`, idsLivros);
  }

  removerLivrosDaColecao(idUsuario: number, idsLivros: Array<number>):  Observable<any> {
    const params = new HttpParams()
    .set('idUsuario', idUsuario)
    return this.httpClient.post<any>(`${this.urlBase}/api/v1/livros/colecao/remocao?${params.toString()}`, idsLivros);
  }

  incluirInformacao(idUsuario: number, idLivro: number, detalhes: string) {
    const params = new HttpParams()
    .set('idUsuario', idUsuario)
    .set('idLivro', idLivro)
    return this.httpClient.post<any>(`${this.urlBase}/api/v1/livros/informacao?${params.toString()}`, detalhes);
  }

  getColecaoPessoal(page: number, size: number, idUsuario: number): Observable<any> {
    const params = new HttpParams()
    .set('page', page)
    .set('size', size)
    .set('idUsuario', idUsuario)

    return this.httpClient.get<any>(`${this.urlBase}/api/v1/livros/colecaoPessoal?${params.toString()}`);
  }

  getLivrosAutoComplete(idUsuario: number, search: string): Observable<any> {
    const params = new HttpParams()
    .set('idUsuario', idUsuario)
    .set('search', search)
    return this.httpClient.get<any>(`${this.urlBase}/api/v1/livros/autocomplete?${params.toString()}`);
  }

}
