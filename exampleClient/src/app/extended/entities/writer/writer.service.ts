
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { WriterService } from 'src/app/entities/writer/writer.service';
@Injectable({
  providedIn: 'root'
})
export class WriterExtendedService extends WriterService {
	constructor(protected httpclient: HttpClient) { 
    super(httpclient);
    this.url += '/extended';
  }
}
