import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { elementAt } from 'rxjs';
import { Reservation } from '../models/Reservation';
import { AdminService } from '../_services/admin.service';
import { TokenStorageService } from '../_services/token-storage.service';
import { UserService } from '../_services/user.service';

@Component({
  selector: 'app-board-admin',
  templateUrl: './board-admin.component.html',
  styleUrls: ['./board-admin.component.css']
})
export class BoardAdminComponent implements OnInit {
  date: string= '';
  date2: string= '';
  message: string  = '';
  showAddedBanner: boolean = false;
  showEscalateBanner: boolean = false;
  username: string="";
  reservations: Reservation[]=[];
  showError: boolean=false;


constructor(private adminService: AdminService){}

ngOnInit(): void {}



  onAdd(): void {
    this.adminService.setHoliday(this.date).subscribe({
      next: data => {
        this.showAddedBanner=true;
        this.message=data.message;
      },
      error: err => {
        this.showAddedBanner=true;
        this.message=err.message;
      }
    });
  }



  onEscalate(): void {
    this.adminService.escalatePrivilege(this.username).subscribe({
      next: data => {
        this.showEscalateBanner=true;
        this.message=data.message;
      },
      error: err => {
        this.showEscalateBanner=true;
        this.message=err.message;;
      }
    });
  }

  retrieve(): void {
    this.adminService.retrieveOnDate(this.date2).subscribe({
      next: data => {
        this.reservations=data;
      },
      error: err => {
        this.message=err.message;;
        this.showError=true;
      }
    });
  }




  reset(): void {
    this.showAddedBanner=false;
    this.showEscalateBanner=false;
    this.reservations=[];
    this.showError=false;
    this.message="";
  };

}


