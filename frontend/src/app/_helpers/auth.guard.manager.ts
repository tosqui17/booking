import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { ManagerService } from '../_services/manager.service';
import { TokenStorageService } from '../_services/token-storage.service';


@Injectable({ providedIn: 'root' })
export class AuthGuardManager implements CanActivate {

  res:boolean = false;
    constructor(
        private router: Router,
        private tokenStorageService: TokenStorageService,
        private managerService: ManagerService

    ) { }
    /*
    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const user = this.tokenStorageService.getUser();
        if (user.roles != null && (user.roles[0] =='ADMIN' || user.roles[0] =='MANAGER'))
          return true;

        else if(user.roles == null){
          this.router.navigateByUrl('/401');
        }
        else if(user.roles[0] == 'USER'){
          this.tokenStorageService.signOut();
          this.router.navigateByUrl('/403');
        }
        return false;
    }
    */

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
      this.managerService.isManager().subscribe({
        next: data =>{
          this.res=data;
          return this.res;
        },
        error: err =>{
          this.router.navigateByUrl('/403');
          return this.res;
        }
      });
      return this.res;
  }
}
