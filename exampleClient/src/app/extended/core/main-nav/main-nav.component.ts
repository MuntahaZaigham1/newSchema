import { Component } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { Router } from '@angular/router';


import { Globals } from 'src/app/core/services/globals';
import { MainNavComponent } from 'src/app/core/main-nav/main-nav.component';

@Component({
	selector: 'app-main-nav',
	templateUrl: './main-nav.component.html',
	styleUrls: ['./main-nav.component.scss', './main-nav-mixin.component.scss']
})
export class MainNavExtendedComponent extends MainNavComponent {	
	
	constructor(
		public router: Router,
		public translate: TranslateService,
		public globals: Globals,

	) {
		super(router, translate, globals,

)
	}
}