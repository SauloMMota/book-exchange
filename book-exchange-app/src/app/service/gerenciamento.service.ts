import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class GerenciamentoService {

  urlBase: string = environment.apiBaseUrl;

  constructor(private httpClient: HttpClient) { }

  solicitacao(dadosSolicitacao: any, informacoes: string): Observable<any>{
    const params = new HttpParams()
    .set('idUsuarioEmissor', dadosSolicitacao.idUsuarioEmissor)
    .set('idUsuarioReceptor', dadosSolicitacao.idUsuarioReceptor)
    .set('idLivroEmissor', dadosSolicitacao.idLivroEmissor)
    .set('idLivroReceptor', dadosSolicitacao.idLivroReceptor)

    return this.httpClient.post<any>(`${this.urlBase}/api/v1/gerenciamento/solicitacao?${params.toString()}`, informacoes);
  }

  confirmacao(idSolicitacao: number): Observable<any>{
    const params = new HttpParams()
    .set('idSolicitacao', idSolicitacao)

    return this.httpClient.put<any>(`${this.urlBase}/api/v1/gerenciamento/confirmacao?${params.toString()}`, null);
  }

  solicitacoes(idUsuario: number): Observable<any>{
    const params = new HttpParams()
    .set('idUsuario', idUsuario)

    return this.httpClient.get<any>(`${this.urlBase}/api/v1/gerenciamento/solicitacoes?${params.toString()}`);
  }
}
