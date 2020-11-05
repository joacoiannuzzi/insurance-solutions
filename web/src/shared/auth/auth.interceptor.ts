import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor
} from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor() {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

    const tok = sessionStorage.getItem('token');

    if (tok) {
      const authReq = req.clone({
        headers: req.headers.set('Authorization', tok)
      });
      return next.handle(authReq);
    } else
    return next.handle(req);
  }
}
