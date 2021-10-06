import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';

import { IWriter } from '../iwriter';
import { WriterService } from '../writer.service';
import { Router, ActivatedRoute } from '@angular/router';
import { WriterNewComponent } from '../new/writer-new.component';
import { BaseListComponent, ListColumnType, PickerDialogService } from 'src/app/common/shared';
import { ErrorService } from 'src/app/core/services/error.service';
import { Globals } from 'src/app/core/services/globals';


@Component({
  selector: 'app-writer-list',
  templateUrl: './writer-list.component.html',
  styleUrls: ['./writer-list.component.scss']
})
export class WriterListComponent extends BaseListComponent<IWriter> implements OnInit {

	title = 'Writer';
	constructor(
		public router: Router,
		public route: ActivatedRoute,
		public global: Globals,
		public dialog: MatDialog,
		public changeDetectorRefs: ChangeDetectorRef,
		public pickerDialogService: PickerDialogService,
		public writerService: WriterService,
		public errorService: ErrorService,
	) { 
		super(router, route, dialog, global, changeDetectorRefs, pickerDialogService, writerService, errorService)
  }

	ngOnInit() {
		this.entityName = 'Writer';
		this.setAssociation();
		this.setColumns();
		this.primaryKeys = ['id', ]
		super.ngOnInit();
	}
  
  
	setAssociation(){
  	
		this.associations = [
		];
	}
  
  	setColumns(){
  		this.columns = [
    		{
				column: 'username',
				searchColumn: 'username',
				label: 'username',
				sort: true,
				filter: true,
				type: ListColumnType.String
			},
    		{
				column: 'lastName',
				searchColumn: 'lastName',
				label: 'last Name',
				sort: true,
				filter: true,
				type: ListColumnType.String
			},
    		{
				column: 'id',
				searchColumn: 'id',
				label: 'id',
				sort: true,
				filter: true,
				type: ListColumnType.Number
			},
    		{
				column: 'firstName',
				searchColumn: 'firstName',
				label: 'first Name',
				sort: true,
				filter: true,
				type: ListColumnType.String
			},
    		{
				column: 'email',
				searchColumn: 'email',
				label: 'email',
				sort: true,
				filter: true,
				type: ListColumnType.String
			},
		  	{
				column: 'actions',
				label: 'Actions',
				sort: false,
				filter: false,
				type: ListColumnType.String
			}
		];
		this.selectedColumns = this.columns;
		this.displayedColumns = this.columns.map((obj) => { return obj.column });
  	}
  addNew(comp) {
	if(!comp){
		comp = WriterNewComponent;
	}
	super.addNew(comp);
  }
  
}
