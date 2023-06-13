import { TestBed } from '@angular/core/testing';

import { ActiveOrderGuard } from './active-order.guard';

describe('ActiveOrderGuardGuard', () => {
  let guard: ActiveOrderGuard;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    guard = TestBed.inject(ActiveOrderGuard);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });
});
