import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { catchError, throwError } from 'rxjs';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const snackBar = inject(MatSnackBar);

  return next(req).pipe(
    catchError((err) => {
      const message =
        err.status === 0 ? 'Cannot reach the server' :
        err.status === 401 ? 'Session expired — please log in again' :
        err.status === 404 ? 'Resource not found' :
        err.error?.message ?? `Error ${err.status}`;

      snackBar.open(message, 'Dismiss', { duration: 4000, panelClass: 'snack-error' });
      return throwError(() => err);
    }),
  );
};
