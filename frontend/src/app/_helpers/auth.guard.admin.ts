import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { AdminService } from '../_services/admin.service';
import { TokenStorageService } from '../_services/token-storage.service';


@Injectable({ providedIn: 'root' })
export class AuthGuardAdmin implements CanActivate {
    res: boolean=false;
    constructor(
        private router: Router,
        private tokenStorageService: TokenStorageService,
        private adminService: AdminService

    ) { }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
      this.adminService.isAdmin().subscribe({
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
