import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class NotificacaoService {
  urlBase: string = environment.apiBaseUrl;

  constructor(private httpClient: HttpClient) {}

  list(pagina: number, tamanho: number, idUsuario: number): Observable<any> {
    const params = new HttpParams()
      .set('pagina', pagina)
      .set('tamanhoPagina', tamanho)
      .set('idUsuario', idUsuario)

    return this.httpClient.get<any>(
      `${this.urlBase}/api/v1/avaliacao?${params.toString()}`
    );
  }

  cadastrar(
    idUsuarioAvaliador: number,
    idUsuarioAvaliado: number,
    comentario: string
  ): Observable<any> {
    const params = new HttpParams()
      .set('idUsuarioAvaliador', idUsuarioAvaliador)
      .set('idUsuarioAvaliado', idUsuarioAvaliado);
    return this.httpClient.post<any>(
      `${this.urlBase}/api/v1/avaliacao?${params.toString()}`,
      comentario
    );
  }
}
