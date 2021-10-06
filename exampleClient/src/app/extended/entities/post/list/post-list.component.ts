import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';

import { Router, ActivatedRoute } from '@angular/router';

import { PostExtendedService } from '../post.service';
import { PostNewExtendedComponent } from '../new/post-new.component';
import { PickerDialogService } from 'src/app/common/shared';
import { ErrorService } from 'src/app/core/services/error.service';
import { Globals } from 'src/app/core/services/globals';

import { WriterExtendedService } from 'src/app/extended/entities/writer/writer.service';
import { PostListComponent } from 'src/app/entities/post/index';

@Component({
  selector: 'app-post-list',
  templateUrl: './post-list.component.html',
  styleUrls: ['./post-list.component.scss']
})
export class PostListExtendedComponent extends PostListComponent implements OnInit {

	title:string = "Post";
	constructor(
		public router: Router,
		public route: ActivatedRoute,
		public global: Globals,
		public dialog: MatDialog,
		public changeDetectorRefs: ChangeDetectorRef,
		public pickerDialogService: PickerDialogService,
		public postService: PostExtendedService,
		public errorService: ErrorService,
		public writerService: WriterExtendedService,
	) { 
		super(router, route, global, dialog, changeDetectorRefs, pickerDialogService, postService, errorService,
		writerService,
)
  }

	ngOnInit() {
		super.ngOnInit();
	}
  
	addNew() {
		super.addNew(PostNewExtendedComponent);
	}
  
}
