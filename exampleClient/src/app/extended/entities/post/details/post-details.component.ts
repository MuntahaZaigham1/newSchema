import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from "@angular/router";
import { FormBuilder } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';

import { PostExtendedService } from '../post.service';

import { PickerDialogService } from 'src/app/common/shared';
import { ErrorService } from 'src/app/core/services/error.service';

import { WriterExtendedService } from 'src/app/extended/entities/writer/writer.service';

import { PostDetailsComponent } from 'src/app/entities/post/index';

@Component({
  selector: 'app-post-details',
  templateUrl: './post-details.component.html',
  styleUrls: ['./post-details.component.scss']
})
export class PostDetailsExtendedComponent extends PostDetailsComponent implements OnInit {
	title:string='Post';
	parentUrl:string='post';
	//roles: IRole[];  
	constructor(
		public formBuilder: FormBuilder,
		public router: Router,
		public route: ActivatedRoute,
		public dialog: MatDialog,
		public postExtendedService: PostExtendedService,
		public pickerDialogService: PickerDialogService,
		public errorService: ErrorService,
		public writerExtendedService: WriterExtendedService,
	) {
		super(formBuilder, router, route, dialog, postExtendedService, pickerDialogService, errorService,
		writerExtendedService,
);
  }

	ngOnInit() {
		super.ngOnInit();
  }
  
}
