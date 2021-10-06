import { Component, OnInit, Inject } from '@angular/core';
import { WriterExtendedService } from '../writer.service';

import { ActivatedRoute,Router } from "@angular/router";
import { FormBuilder } from '@angular/forms';
import { PickerDialogService } from 'src/app/common/shared';
import { ErrorService } from 'src/app/core/services/error.service';
import { MatDialogRef, MatDialog, MAT_DIALOG_DATA } from '@angular/material/dialog';


import { WriterNewComponent } from 'src/app/entities/writer/index';

@Component({
  selector: 'app-writer-new',
  templateUrl: './writer-new.component.html',
  styleUrls: ['./writer-new.component.scss']
})
export class WriterNewExtendedComponent extends WriterNewComponent implements OnInit {
  
    title:string = "New Writer";
	constructor(
		public formBuilder: FormBuilder,
		public router: Router,
		public route: ActivatedRoute,
		public dialog: MatDialog,
		public dialogRef: MatDialogRef<WriterNewComponent>,
		@Inject(MAT_DIALOG_DATA) public data: any,
		public pickerDialogService: PickerDialogService,
		public writerExtendedService: WriterExtendedService,
		public errorService: ErrorService,
	) {
		super(formBuilder, router, route, dialog, dialogRef, data, pickerDialogService, writerExtendedService, errorService,
		);
	}
 
	ngOnInit() {
		super.ngOnInit();
  }
     
}
