import { Component, OnInit, Inject } from '@angular/core';
import { PostExtendedService } from '../post.service';

import { ActivatedRoute,Router } from "@angular/router";
import { FormBuilder } from '@angular/forms';
import { PickerDialogService } from 'src/app/common/shared';
import { ErrorService } from 'src/app/core/services/error.service';
import { MatDialogRef, MatDialog, MAT_DIALOG_DATA } from '@angular/material/dialog';

import { WriterExtendedService } from 'src/app/extended/entities/writer/writer.service';

import { PostNewComponent } from 'src/app/entities/post/index';

@Component({
  selector: 'app-post-new',
  templateUrl: './post-new.component.html',
  styleUrls: ['./post-new.component.scss']
})
export class PostNewExtendedComponent extends PostNewComponent implements OnInit {
  
    title:string = "New Post";
	constructor(
		public formBuilder: FormBuilder,
		public router: Router,
		public route: ActivatedRoute,
		public dialog: MatDialog,
		public dialogRef: MatDialogRef<PostNewComponent>,
		@Inject(MAT_DIALOG_DATA) public data: any,
		public pickerDialogService: PickerDialogService,
		public postExtendedService: PostExtendedService,
		public errorService: ErrorService,
		public writerExtendedService: WriterExtendedService,
	) {
		super(formBuilder, router, route, dialog, dialogRef, data, pickerDialogService, postExtendedService, errorService,
		writerExtendedService,
		);
	}
 
	ngOnInit() {
		super.ngOnInit();
  }
     
}
