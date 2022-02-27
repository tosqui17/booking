import { Component } from '@angular/core';
import { TokenStorageService } from './_services/token-storage.service';
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  private roles: string[] = [];
  isLoggedIn = false;
  showAdminBoard = false;
  showModeratorBoard = false;
  username?: string;
  model: any;
  constructor(        private router: Router,
    private tokenStorageService: TokenStorageService) { }

  ngOnInit(): void {
    this.isLoggedIn = !!this.tokenStorageService.getToken();

    if (this.isLoggedIn) {
      const user = this.tokenStorageService.getUser();
      this.roles = user.roles;

      this.showAdminBoard = this.roles.includes('ADMIN');
      if(this.roles.includes('MANAGER') || this.roles.includes('ADMIN'))
      this.showModeratorBoard = true;
      this.username = user.username;
    }
  }

  logout(): void {
    this.tokenStorageService.signOut();
    this.isLoggedIn=false;
    this.showAdminBoard=false;
    this.showModeratorBoard=false;
    this.router.navigateByUrl("/");
  }
}
