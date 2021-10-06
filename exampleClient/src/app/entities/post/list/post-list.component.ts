import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';

import { IPost } from '../ipost';
import { PostService } from '../post.service';
import { Router, ActivatedRoute } from '@angular/router';
import { PostNewComponent } from '../new/post-new.component';
import { BaseListComponent, ListColumnType, PickerDialogService } from 'src/app/common/shared';
import { ErrorService } from 'src/app/core/services/error.service';
import { Globals } from 'src/app/core/services/globals';

import { WriterService } from 'src/app/entities/writer/writer.service';

@Component({
  selector: 'app-post-list',
  templateUrl: './post-list.component.html',
  styleUrls: ['./post-list.component.scss']
})
export class PostListComponent extends BaseListComponent<IPost> implements OnInit {

	title = 'Post';
	constructor(
		public router: Router,
		public route: ActivatedRoute,
		public global: Globals,
		public dialog: MatDialog,
		public changeDetectorRefs: ChangeDetectorRef,
		public pickerDialogService: PickerDialogService,
		public postService: PostService,
		public errorService: ErrorService,
		public writerService: WriterService,
	) { 
		super(router, route, dialog, global, changeDetectorRefs, pickerDialogService, postService, errorService)
  }

	ngOnInit() {
		this.entityName = 'Post';
		this.setAssociation();
		this.setColumns();
		this.primaryKeys = ['id', ]
		super.ngOnInit();
	}
  
  
	setAssociation(){
  	
		this.associations = [
			{
				column: [
            {
					  	key: 'writerId',
					  	value: undefined,
					  	referencedkey: 'id'
					  },
					  
				],
				isParent: false,
				descriptiveField: 'writerDescriptiveField',
				referencedDescriptiveField: 'id',
				service: this.writerService,
				associatedObj: undefined,
				table: 'writer',
				type: 'ManyToOne',
				url: 'posts',
				listColumn: 'writer',
				label: 'writer',

			},
		];
	}
  
  	setColumns(){
  		this.columns = [
    		{
				column: 'title',
				searchColumn: 'title',
				label: 'title',
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
				column: 'body',
				searchColumn: 'body',
				label: 'body',
				sort: true,
				filter: true,
				type: ListColumnType.String
			},
			{
	  			column: 'writerDescriptiveField',
				searchColumn: 'writer',
				label: 'writer',
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
		comp = PostNewComponent;
	}
	super.addNew(comp);
  }
  
}
