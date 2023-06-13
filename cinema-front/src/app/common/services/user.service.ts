import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { User } from '../model/user';
import { BehaviorSubject, Subject } from 'rxjs';
import { StorageService } from './storage.service';

const USER_ENDPOINT = '/api/users';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  user$: Subject<User | null> = new BehaviorSubject<User | null>(null);

  constructor(private httpClient: HttpClient, private storage: StorageService) {
    if (this.storage.hasUser()) {
      this.user$.next(this.storage.retrieveUser());
    }
  }

  getUser() {
    return this.user$.asObservable();
  }

  login(email: string, password: string) {
    this.httpClient
      .post<User>(USER_ENDPOINT + '/login', { email, password })
      .subscribe((user) => {
        this.user$.next(user);
        this.storage.saveUser(user);
      });
  }

  logout() {
    this.storage.clearUser();
    this.user$.next(null);
  }

  register(name: string, email: string, password: string) {
    this.httpClient.post<User>(USER_ENDPOINT, {
      name,
      email,
      password,
    });
  }

  loginWithSSO(credential: string) {
    this.httpClient
      .post<User>(USER_ENDPOINT + '/login/google', {
        credential,
      })
      .subscribe((user) => {
        this.user$.next(user);
        this.storage.saveUser(user);
      });
  }
}
