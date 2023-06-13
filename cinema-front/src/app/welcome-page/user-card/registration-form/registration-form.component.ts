import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import {
  AbstractControl,
  FormControl,
  FormGroup,
  ValidationErrors,
  ValidatorFn,
  Validators,
} from '@angular/forms';
import { UserService } from '../../../common/services/user.service';

@Component({
  selector: 'app-registration-form',
  templateUrl: './registration-form.component.html',
  styleUrls: ['./registration-form.component.scss'],
})
export class RegistrationFormComponent implements OnInit {
  @Output()
  toggleFormEvent = new EventEmitter<boolean>();
  @Output()
  loadingEvent = new EventEmitter<boolean>();

  constructor(private userService: UserService) {}

  registrationData = new FormGroup(
    {
      name: new FormControl('', {
        validators: Validators.required,
        nonNullable: true,
      }),
      email: new FormControl('', {
        validators: [Validators.email, Validators.required],
        nonNullable: true,
      }),
      password: new FormControl('', {
        validators: Validators.required,
        nonNullable: true,
      }),
      passwordConfirmation: new FormControl('', {
        validators: Validators.required,
        nonNullable: true,
      }),
    },
    CustomValidators.matchingPasswords
  );

  registerUser() {
    if (this.registrationData.valid) {
      this.loadingEvent.emit(true);
      this.userService.register(
        this.registrationData.controls.name.value,
        this.registrationData.controls.email.value,
        this.registrationData.controls.password.value
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
    this.toggleForm();
  }
}
class CustomValidators {
  static matchingPasswords: ValidatorFn = (
    group: AbstractControl
  ): ValidationErrors | null => {
    let pass = group.get('password')?.value;
    let confirmPass = group.get('passwordConfirmation')?.value;
    return pass === confirmPass ? null : { notSame: true };
  };
}
