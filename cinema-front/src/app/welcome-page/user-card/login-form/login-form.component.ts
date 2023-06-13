import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { UserService } from '../../../common/services/user.service';

@Component({
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.scss'],
})
export class LoginFormComponent implements OnInit {
  @Output()
  toggleFormEvent = new EventEmitter<boolean>();
  @Output()
  loadingEvent = new EventEmitter<boolean>();
  constructor(private userService: UserService) {}

  loginData = new FormGroup({
    email: new FormControl('', {
      validators: [Validators.email, Validators.required],
      nonNullable: true,
    }),
    password: new FormControl('', {
      validators: Validators.required,
      nonNullable: true,
    }),
  });

  login() {
    if (this.loginData.valid) {
      this.loadingEvent.emit(true);
      this.userService.login(
        this.loginData.controls.email.value,
        this.loginData.controls.password.value
      );
    }
  }

  toggleForm() {
    this.toggleFormEvent.emit(true);
  }

  ngOnInit() {
    // @ts-ignore
    google.accounts.id.initialize({
      client_id:
        '735910041717-ji8oqfak3s04iu9j5sh9it61g5ene86d.apps.googleusercontent.com',
      callback: this.handleCredentialResponse.bind(this),
      auto_select: false,
      cancel_on_tap_outside: true,
    });
    // @ts-ignore
    google.accounts.id.renderButton(
      // @ts-ignore
      document.getElementById('google-button'),
      { theme: 'outline', size: 'large', width: '100%' }
    );
    // @ts-ignore
    google.accounts.id.prompt((notification: PromptMomentNotification) => {});
  }

  handleCredentialResponse(response: any) {
    this.userService.loginWithSSO(response.credential);
  }
}
