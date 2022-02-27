import { Component, OnInit } from '@angular/core';
import { UserService } from '../_services/user.service';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule } from '@angular/forms';
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  buttonText : string = "Book ASAP"
  badgeText : string = "Wow"
  content?: string;

  constructor(private userService: UserService) { }

  ngOnInit(): void {
  }

  goToLogin(){
    window.location.assign("/login");
  }

  goToSignUp(){
    window.location.assign("/register");
  }

}
