import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { DH } from '../models/DH';

const API_DH       = 'http://localhost:8080/api/managers/available/';
const API_BOOK     = 'http://localhost:8080/api/managers/book/';
const API_RESERVED = 'http://localhost:8080/api/managers/reserved/';
const API_UNLOCK   = 'http://localhost:8080/api/managers/unlock/';
const API_TEST     = 'http://localhost:8080/api/managers/test';




@Injectable({
  providedIn: 'root'
})

export class ManagerService {
  constructor(private http: HttpClient) { }

  getDatesHours(): Observable<DH[]>{
    return this.http.get<DH[]>(API_DH);
  }

  setReservationRequestFor(user: string, date: string, hour: string): Observable<any> {
    return this.http.get(API_BOOK+user+"/"+date+"/"+hour,{ responseType: 'json' });
  }

  getReservationsDate(): Observable<DH[]>{
    return this.http.get<DH[]>(API_RESERVED);
  }

  deleteReservation(date: string, hour: string): Observable<any>{
    return this.http.delete(API_BOOK+date+"/"+hour,{ responseType: 'json' });
  }

  unlockUser(user: string): Observable<any>{
    const params= "user="+user;
    return this.http.patch(API_UNLOCK,params,{ responseType: 'json'});
  }

  isManager(): Observable<any>{
    return this.http.post(API_TEST, { responseType: 'json'})
  }

}
