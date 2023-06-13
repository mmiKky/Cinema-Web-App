import { Injectable } from '@angular/core';
import { User } from '../model/user';

@Injectable({
  providedIn: 'root',
})
export class StorageService {
  constructor() {}

  saveUser(value: User) {
    localStorage.setItem('user', JSON.stringify(value));
  }

  retrieveUser() {
    if (this.hasUser()) return JSON.parse(localStorage.getItem('user')!);
  }

  hasUser() {
    return localStorage.getItem('user') !== null;
  }

  clearUser() {
    localStorage.removeItem('user');
  }
}
