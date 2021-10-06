import { Component, OnInit, Inject } from '@angular/core';
import { ActivatedRoute,Router} from "@angular/router";
import { FormBuilder, Validators} from '@angular/forms';
import { MatDialogRef, MatDialog, MAT_DIALOG_DATA } from '@angular/material/dialog';

import { WriterService } from '../writer.service';
import { IWriter } from '../iwriter';
import { BaseNewComponent, FieldType, PickerDialogService } from 'src/app/common/shared';
import { ErrorService } from 'src/app/core/services/error.service';


@Component({
  selector: 'app-writer-new',
  templateUrl: './writer-new.component.html',
  styleUrls: ['./writer-new.component.scss']
})
export class WriterNewComponent extends BaseNewComponent<IWriter> implements OnInit {
  
    title:string = "New Writer";
	constructor(
		public formBuilder: FormBuilder,
		public router: Router,
		public route: ActivatedRoute,
		public dialog: MatDialog,
		public dialogRef: MatDialogRef<WriterNewComponent>,
		@Inject(MAT_DIALOG_DATA) public data: any,
		public pickerDialogService: PickerDialogService,
		public writerService: WriterService,
		public errorService: ErrorService,
	) {
		super(formBuilder, router, route, dialog, dialogRef, data, pickerDialogService, writerService, errorService);
	}
 
		ngOnInit() {
		this.entityName = 'Writer';
		this.setAssociations();
		super.ngOnInit();
    	this.setForm();
		this.checkPassedData();
    }
 		
	setForm(){
 		this.itemForm = this.formBuilder.group({
      email: ['', Validators.required],
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      username: [''],
    });
    
    this.fields = [
              {
      		  name: 'username',
      		  label: 'username',
      		  isRequired: false,
      		  isAutoGenerated: false,
      	      type: FieldType.String,
      	    },
              {
      		  name: 'lastName',
      		  label: 'last Name',
      		  isRequired: true,
      		  isAutoGenerated: false,
      	      type: FieldType.String,
      	    },
              {
      		  name: 'firstName',
      		  label: 'first Name',
      		  isRequired: true,
      		  isAutoGenerated: false,
      	      type: FieldType.String,
      	    },
              {
      		  name: 'email',
      		  label: 'email',
      		  isRequired: true,
      		  isAutoGenerated: false,
      	      type: FieldType.String,
      	    },
		];
	}
	 
	setAssociations(){
  	
		this.associations = [
		];
		this.parentAssociations = this.associations.filter(association => {
			return (!association.isParent);
		});

	}
	
	onSubmit() {
		let writer = this.itemForm.getRawValue();
		super.onSubmit(writer);
		
	}
    
}
