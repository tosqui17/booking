import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

const AUTH_API = 'http://localhost:8080/api/auth/signin';
const REGISTER_API = 'http://localhost:8080/api/users'

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient) { }

  login(username: string, password: string): Observable<any> {
    return this.http.post(AUTH_API, {
      username,
      password
    }, httpOptions);
  }

  register(username: string, firstName: string, lastName: string ,email: string, password: string): Observable<any> {
    return this.http.post(REGISTER_API, {
      username,
      firstName,
      lastName,
      email,
      password
    }, httpOptions);
  }
}
