import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UserService } from '../_services/user.service';

@Component({
  selector: 'app-verify',
  templateUrl: './verify.component.html'
})
export class VerifyComponent implements OnInit {

  constructor(private route: ActivatedRoute,private userService: UserService) { }

  isVerified = false;
  isNotVerified= false;
  message: string = "";

  ngOnInit(): void {
      this.route.queryParams.subscribe({
        next: data => {
          if(data['username'] !== null && data['verificationToken'] !== null){
          this.userService.verifyUser(data['username'],data['verificationToken']).subscribe({
            next: data => {
              this.message = data.message;
              this.isVerified = true;
            },
            error: err => {
              this.isNotVerified=false;
              this.message = err.message;
            },
          });
        }},
        error:err => {
          this.message=err.error.message;
        }
      });
    }



}
