
import { RouterModule, Routes } from '@angular/router';
import { ModuleWithProviders } from "@angular/core";
import { CanDeactivateGuard } from 'src/app/core/guards/can-deactivate.guard';
import { WriterDetailsExtendedComponent, WriterListExtendedComponent , WriterNewExtendedComponent } from './';

const routes: Routes = [
	{ path: '', component: WriterListExtendedComponent, canDeactivate: [CanDeactivateGuard] },
	{ path: ':id', component: WriterDetailsExtendedComponent, canDeactivate: [CanDeactivateGuard] },
	{ path: 'new', component: WriterNewExtendedComponent },
];

export const writerRoute: ModuleWithProviders<any> = RouterModule.forChild(routes);