import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { DH } from '../models/DH';
import { TokenStorageService } from './token-storage.service';
import { Reservation } from '../models/Reservation';

const API_DH = 'http://localhost:8080/api/users/available';
const API_VERIFY = 'http://localhost:8080/api/users/verify/';
const API_BOOK = 'http://localhost:8080/api/users/book/';
const API_TEST = 'http://localhost:8080/api/users/test';
const API_RESERVATIONS = 'http://localhost:8080/api/users/reservations';




@Injectable({
  providedIn: 'root'
})

export class UserService {
  constructor(private http: HttpClient) { }

  getDatesHours(): Observable<DH[]>{
    return this.http.get<DH[]>(API_DH, { responseType: 'json' });
  }

  setReservationRequestForMe(date: string, hour: string): Observable<any> {
    return this.http.get(API_BOOK+date+"/"+hour,{ responseType: 'json' });
  }

  verifyUser(username: string, code: string): Observable<any>{
    return this.http.get(API_VERIFY+username+"/"+code,{ responseType: 'json' });
  }

  isUser(): Observable<boolean>{
    return this.http.post<boolean>(API_TEST, {responseType: "json"} );
  }

  getReservations(): Observable<Reservation[]>{
    return this.http.post<Reservation[]>(API_RESERVATIONS, {responseType: "json"} );
  }


}
