import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { RegisterComponent } from './register/register.component';
import { LoginComponent } from './login/login.component';
import { HomeComponent } from './home/home.component';
import { ProfileComponent } from './profile/profile.component';
import { BoardUserComponent } from './board-user/board-user.component';
import { BoardManagerComponent } from './board-manager/board-manager.component';
import { BoardAdminComponent } from './board-admin/board-admin.component';
import { AuthGuardAdmin } from './_helpers/auth.guard.admin';
import { NotFoundComponent } from './not-found/not-found.component';
import { UnauthorizedComponent } from './unauthorized/unauthorized.component';
import { ForbiddenComponent } from './forbidden/forbidden.component';
import { AuthGuardManager } from './_helpers/auth.guard.manager';
import { AuthGuardUser } from './_helpers/auth.guard.user';
import { VerifyComponent } from './verify/verify.component';

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'profile', component: ProfileComponent },
  { path: 'user', component: BoardUserComponent , canActivate: [AuthGuardUser]},
  { path: 'manager', component: BoardManagerComponent , canActivate: [AuthGuardManager]},
  { path: 'admin',component: BoardAdminComponent, canActivate: [AuthGuardAdmin]},
  { path: '404', component: NotFoundComponent},
  { path: '401', component: UnauthorizedComponent},
  { path: '403', component: ForbiddenComponent},
  { path: 'verify', component: VerifyComponent},
  { path: '**', redirectTo: '/404' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
