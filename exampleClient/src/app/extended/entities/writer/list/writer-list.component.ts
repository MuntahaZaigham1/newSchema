import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';

import { Router, ActivatedRoute } from '@angular/router';

import { WriterExtendedService } from '../writer.service';
import { WriterNewExtendedComponent } from '../new/writer-new.component';
import { PickerDialogService } from 'src/app/common/shared';
import { ErrorService } from 'src/app/core/services/error.service';
import { Globals } from 'src/app/core/services/globals';

import { WriterListComponent } from 'src/app/entities/writer/index';

@Component({
  selector: 'app-writer-list',
  templateUrl: './writer-list.component.html',
  styleUrls: ['./writer-list.component.scss']
})
export class WriterListExtendedComponent extends WriterListComponent implements OnInit {

	title:string = "Writer";
	constructor(
		public router: Router,
		public route: ActivatedRoute,
		public global: Globals,
		public dialog: MatDialog,
		public changeDetectorRefs: ChangeDetectorRef,
		public pickerDialogService: PickerDialogService,
		public writerService: WriterExtendedService,
		public errorService: ErrorService,
	) { 
		super(router, route, global, dialog, changeDetectorRefs, pickerDialogService, writerService, errorService,
)
  }

	ngOnInit() {
		super.ngOnInit();
	}
  
	addNew() {
		super.addNew(WriterNewExtendedComponent);
	}
  
}
