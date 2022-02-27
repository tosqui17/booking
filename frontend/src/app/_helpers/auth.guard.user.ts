import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { TokenStorageService } from '../_services/token-storage.service';
import { UserService } from '../_services/user.service';


@Injectable({ providedIn: 'root' })
export class AuthGuardUser implements CanActivate {

    res:boolean=false;

    constructor(
        private router: Router,
        private tokenStorageService: TokenStorageService,
        private userService: UserService

    ) { }


    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
      this.userService.isUser().subscribe({
        next: data =>{
          this.res=data;
        },
        error: err =>{
          this.router.navigateByUrl('/401');
          return false;
        }

      });
      return true;
  }
}
