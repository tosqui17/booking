import { Component, OnInit } from '@angular/core';
import { Reservation } from '../models/Reservation';
import { TokenStorageService } from '../_services/token-storage.service';
import { UserService } from '../_services/user.service';
@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  currentUser: any;
  popupEnabled: boolean = false;
  chose: boolean = false;
  reservations: Reservation[] = [];



  constructor(private token: TokenStorageService,private userService: UserService) { }

  ngOnInit(): void {
    this.currentUser = this.token.getUser();
    this.userService.getReservations().subscribe({
      next: data => {
      this.reservations=data;
    },
      error: err => {
        console.log("error while retriveing reservations")
      }
    });
  }

  areYouSure(): void{
    this.popupEnabled=true;
  }

  sure(): void{
    //OH NO!
  }

  notSure(): void{
    this.popupEnabled=false;
  }


}
