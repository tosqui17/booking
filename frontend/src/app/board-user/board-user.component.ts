import { Component, Input, OnInit } from '@angular/core';
import { delay } from 'rxjs';
import { DH } from '../models/DH';
import { TokenStorageService } from '../_services/token-storage.service';
import { UserService } from '../_services/user.service';

@Component({
  selector: 'app-board-user',
  templateUrl: './board-user.component.html',
  styleUrls: ['./board-user.component.css']
})
export class BoardUserComponent implements OnInit {
  datesHours: DH[] = [];
  content: string = "";
  data: DH = new DH("",[]);
  hour: string="";
  showHour: boolean = false;
  showButton: boolean = false;
  isBooked = false;




  constructor(
    private userService: UserService,
     private tokenStorageService: TokenStorageService) { }

  ngOnInit(): void {
      this.userService.getDatesHours().subscribe({
        next: data => {
        this.datesHours=data;

      },
        error: err => {
          this.content="Can't receive available date"
        }
      });

  }

  onSelectDate(date:DH):void{
    this.isBooked=false;
    this.showHour=true;
    this.content="";
    this.data=date;
  }

  onSelectHour(hour : string):void{
    this.showButton=true;
    this.hour=hour.substring(0,5);
  }

  onReserve(date: string,hour: string): void{
    //console.log(date);
    //console.log(hour);
    this.userService.setReservationRequestForMe(date,hour).subscribe({
      next: data => {
        this.isBooked=true;
        this.content=data.message;
        this.refresh();
      },
      error: err => {
        this.isBooked=false;
        this.content=err.message;
        this.refresh();
      }
    });
    this.showHour=false;
    this.showButton=false;
  }

  refresh(): void{
    this.userService.getDatesHours().subscribe({
      next: data => { this.datesHours=data;},
      error: err => { this.content="noSuch"}
    });
  }


}
