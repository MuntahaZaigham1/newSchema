
import { RouterModule, Routes } from '@angular/router';
import { ModuleWithProviders } from "@angular/core";
// import { CanDeactivateGuard } from 'src/app/core/guards/can-deactivate.guard';

// import { WriterDetailsComponent, WriterListComponent, WriterNewComponent } from './';

const routes: Routes = [
	// { path: '', component: WriterListComponent, canDeactivate: [CanDeactivateGuard] },
	// { path: ':id', component: WriterDetailsComponent, canDeactivate: [CanDeactivateGuard] },
	// { path: 'new', component: WriterNewComponent },
];

export const writerRoute: ModuleWithProviders<any> = RouterModule.forChild(routes);