
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { IPost } from './ipost';
import { GenericApiService } from 'src/app/common/shared';

@Injectable({
  providedIn: 'root'
})
export class PostService extends GenericApiService<IPost> { 
  constructor(protected httpclient: HttpClient) { 
    super(httpclient, "post");
  }
  
  
  
}
