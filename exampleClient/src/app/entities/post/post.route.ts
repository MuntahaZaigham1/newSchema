
import { RouterModule, Routes } from '@angular/router';
import { ModuleWithProviders } from "@angular/core";
// import { CanDeactivateGuard } from 'src/app/core/guards/can-deactivate.guard';

// import { PostDetailsComponent, PostListComponent, PostNewComponent } from './';

const routes: Routes = [
	// { path: '', component: PostListComponent, canDeactivate: [CanDeactivateGuard] },
	// { path: ':id', component: PostDetailsComponent, canDeactivate: [CanDeactivateGuard] },
	// { path: 'new', component: PostNewComponent },
];

export const postRoute: ModuleWithProviders<any> = RouterModule.forChild(routes);