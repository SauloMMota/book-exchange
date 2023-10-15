import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { firstValueFrom, Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { JwtHelperService} from '@auth0/angular-jwt'

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  tokenUrl: string = environment.obterTokenUrl;
  clientId: string = environment.clientId;
  clientSecret: string = environment.clientSecret;
  urlBase: string = environment.apiBaseUrl;
  jwtHelper: JwtHelperService = new JwtHelperService();

  constructor(private httpClient: HttpClient) { }


  obterToken(): Observable<any> {
    const params = new HttpParams()
                      .set('grant_type', 'client_credentials')
                      .set('client_id', this.clientId)
                      .set('client_secret', this.clientSecret);

    const headers = {
      'Content-Type': 'application/x-www-form-urlencoded'
    }

    return this.httpClient.post<any>(this.tokenUrl, params.toString(), {headers: headers});
  }

  logar(dados: any): Observable<any> {
    return this.httpClient.post<any>(`${this.urlBase}/api/v1/login`,  dados);
  }

  cadastrar(dados: any): Observable<any> {
    return this.httpClient.post<any>(`${this.urlBase}/api/v1/cadastro`,  dados);
  }

  isAuthenticated(): boolean {
    const token = this.getToken();
    if(token) {
      const expired = this.jwtHelper.isTokenExpired(token)
      return !expired
    }
    return false
  }

  getToken() {
    const tokenString = localStorage.getItem('access_token')
    if(tokenString) {
      const token = JSON.parse(tokenString).access_token;
      return token;
    }
    return null;
  }

}
