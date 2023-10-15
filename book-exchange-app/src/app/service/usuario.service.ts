import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {

  urlBase: string = environment.apiBaseUrl;

  constructor(private httpClient: HttpClient) { }

  atualizar(id: number, dados: any): Observable<any>{
    const params = new HttpParams()
    .set('id', id)
    return this.httpClient.put<any>(`${this.urlBase}/api/v1/usuarios/alterar?${params.toString()}`,  dados);
  }

  getUsuario(id: number): Observable<any>{
    const params = new HttpParams()
    .set('id', id)
    return this.httpClient.get<any>(`${this.urlBase}/api/v1/usuarios?${params.toString()}`)
  }

}
