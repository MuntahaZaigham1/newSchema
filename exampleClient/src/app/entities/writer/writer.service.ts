
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { IWriter } from './iwriter';
import { GenericApiService } from 'src/app/common/shared';

@Injectable({
  providedIn: 'root'
})
export class WriterService extends GenericApiService<IWriter> { 
  constructor(protected httpclient: HttpClient) { 
    super(httpclient, "writer");
  }
  
  
  
}
