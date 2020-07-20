import { Injectable } from '@angular/core';
import { HttpClient} from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { Image } from './models/Images.js';
import { URL } from '../config/variables.config';
import 'rxjs/add/operator/map';
@Injectable({
  providedIn: 'root'
})
export class ProviderService {

  constructor(public http:HttpClient) { }
  list_images() : Observable<Array<Image>>{
      console.log(this.http.get<Image>(`${URL}`));
    return this.http.get<Image>("http://localhost:8081").map(res=>res.json());
  }
}
