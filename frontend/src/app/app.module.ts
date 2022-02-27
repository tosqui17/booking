import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { HomeComponent } from './home/home.component';
import { ProfileComponent } from './profile/profile.component';
import { BoardAdminComponent } from './board-admin/board-admin.component';
import { BoardManagerComponent } from './board-manager/board-manager.component';
import { BoardUserComponent } from './board-user/board-user.component';
import { authInterceptorProviders } from './_helpers/auth.interceptor';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { UnauthorizedComponent } from './unauthorized/unauthorized.component';
import { ForbiddenComponent } from './forbidden/forbidden.component';
import { NotFoundComponent } from './not-found/not-found.component';
import { ErrorInterceptorProviders } from './_helpers/error.interceptor';
import { VerifyComponent } from './verify/verify.component';


@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    HomeComponent,
    ProfileComponent,
    BoardAdminComponent,
    BoardManagerComponent,
    BoardUserComponent,
    UnauthorizedComponent,
    ForbiddenComponent,
    NotFoundComponent,
    VerifyComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    BrowserAnimationsModule,
  ],
  providers: [authInterceptorProviders,ErrorInterceptorProviders],
  bootstrap: [AppComponent],
  exports: []
})
export class AppModule { }
