import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';


const API_ADD_HOLIDAY = 'http://localhost:8080/api/admins/addHoliday/';
const API_ESCALATE    = 'http://localhost:8080/api/admins/escalate/';
const API_TEST        = 'http://localhost:8080/api/admins/test';
const API_RETRIEVE        = 'http://localhost:8080/api/admins/retrieve/';


@Injectable({
  providedIn: 'root'
})

export class AdminService {
  constructor(private http: HttpClient) { }

  isAdded: boolean=false;

  setHoliday(date: string): Observable<any> {
    const body = "data="+date;
    return this.http.post(API_ADD_HOLIDAY, body,{ responseType: "json" });
  }

  escalatePrivilege(user: string): Observable<any> {
    const body = "user="+user;
    return this.http.patch(API_ESCALATE, body,{ responseType: "json" });
  }

  isAdmin(): Observable<any>{
    return this.http.post(API_TEST, { responseType: 'json'})
  }

  retrieveOnDate(date:String): Observable<any>{
    return this.http.get(API_RETRIEVE+date, { responseType: 'json'})
  }

}
