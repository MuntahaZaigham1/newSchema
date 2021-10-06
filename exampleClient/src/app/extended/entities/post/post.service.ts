
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { PostService } from 'src/app/entities/post/post.service';
@Injectable({
  providedIn: 'root'
})
export class PostExtendedService extends PostService {
	constructor(protected httpclient: HttpClient) { 
    super(httpclient);
    this.url += '/extended';
  }
}
