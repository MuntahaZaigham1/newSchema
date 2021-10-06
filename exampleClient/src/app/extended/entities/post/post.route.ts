
import { RouterModule, Routes } from '@angular/router';
import { ModuleWithProviders } from "@angular/core";
import { CanDeactivateGuard } from 'src/app/core/guards/can-deactivate.guard';
import { PostDetailsExtendedComponent, PostListExtendedComponent , PostNewExtendedComponent } from './';

const routes: Routes = [
	{ path: '', component: PostListExtendedComponent, canDeactivate: [CanDeactivateGuard] },
	{ path: ':id', component: PostDetailsExtendedComponent, canDeactivate: [CanDeactivateGuard] },
	{ path: 'new', component: PostNewExtendedComponent },
];

export const postRoute: ModuleWithProviders<any> = RouterModule.forChild(routes);