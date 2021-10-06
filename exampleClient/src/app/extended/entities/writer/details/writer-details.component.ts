import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from "@angular/router";
import { FormBuilder } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';

import { WriterExtendedService } from '../writer.service';

import { PickerDialogService } from 'src/app/common/shared';
import { ErrorService } from 'src/app/core/services/error.service';


import { WriterDetailsComponent } from 'src/app/entities/writer/index';

@Component({
  selector: 'app-writer-details',
  templateUrl: './writer-details.component.html',
  styleUrls: ['./writer-details.component.scss']
})
export class WriterDetailsExtendedComponent extends WriterDetailsComponent implements OnInit {
	title:string='Writer';
	parentUrl:string='writer';
	//roles: IRole[];  
	constructor(
		public formBuilder: FormBuilder,
		public router: Router,
		public route: ActivatedRoute,
		public dialog: MatDialog,
		public writerExtendedService: WriterExtendedService,
		public pickerDialogService: PickerDialogService,
		public errorService: ErrorService,
	) {
		super(formBuilder, router, route, dialog, writerExtendedService, pickerDialogService, errorService,
);
  }

	ngOnInit() {
		super.ngOnInit();
  }
  
}
