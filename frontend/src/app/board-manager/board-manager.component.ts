import { Component, Input, OnInit } from '@angular/core';
import { DH } from '../models/DH';
import { ManagerService } from '../_services/manager.service';

@Component({
  selector: 'app-board-manager',
  templateUrl: './board-manager.component.html',
  styleUrls: ['./board-manager.component.css']
})
export class BoardManagerComponent implements OnInit {
  datesHours: DH[] = [];
  reservedDatesHours: DH[] =[];
  message: string = "";
  data: DH = new DH("",[]);
  hour: string="";
  showHour: boolean = false;
  showButton: boolean = false;
  showBookedBanner:boolean = false;
  showUnlockedBanner:boolean = false;
  showDeletedBanner:boolean = false;
  userExist: boolean = false
  username: string="";
  username2: string="";
  TokenError:string =""




  constructor(private managerService: ManagerService) { }

  ngOnInit(): void {
      this.refresh();
  }

  onSelectDate(date:DH):void{
    this.showBookedBanner=false;
    this.showDeletedBanner=false;
    this.showUnlockedBanner=false;
    this.showHour=true;
    this.data=date;
    //console.log(this.username);
  }

  onSelectHour(hour : string):void{
    this.showButton=true;
    this.hour=hour.substring(0,5);
  }

  onReserve(date: string,hour: string): void{
    this.managerService.setReservationRequestFor(this.username,this.data.date,hour).subscribe({
      next: data => {
        this.showBookedBanner=true;
        this.message=data.message;
        this.refresh();
      },
      error: err => {
        this.showBookedBanner=true;
        this.message=err.message;
      }
    });
    this.showHour=false;
    this.showButton=false;
  }

  onSelectDate2(date:DH):void{
    this.showBookedBanner=false;
    this.showDeletedBanner=false;
    this.showUnlockedBanner=false;
    this.showHour=true;
    this.data=date;
  }

  onSelectHour2(hour : string):void{
    //console.log(this.hour);
    this.showButton=true;
    this.hour=hour.substring(0,5);
  }

  onDelete(date: string,hour: string): void{
    this.managerService.deleteReservation(date,hour).subscribe({
      next: data => {
        this.showDeletedBanner=true;
        this.message=data.message;
        console.log(data);
        this.refresh();

      },
      error: err => {
        this.showDeletedBanner=true;
        this.message=err.message;
        console.log(err);
      }
    });
    this.showHour=false;
    this.showButton=false;
  }

  onUnlock(): any{
    this.managerService.unlockUser(this.username2).subscribe({
      next: data =>{
        this.showUnlockedBanner=true;
        this.message=data.message;
      },
      error: err => {
        this.showUnlockedBanner=true;
        this.message=err.message;
      }
    });
}

  refresh():any{
    this.managerService.getDatesHours().subscribe({
      next: data => {
        this.datesHours=data;
      },
      error: err => {
        console.log(err.message)
      }
    });

    this.managerService.getReservationsDate().subscribe({
      next: data => {
        this.reservedDatesHours=data;
      },
      error: err => {
        console.log(err.message)
      }
    });
}
}

