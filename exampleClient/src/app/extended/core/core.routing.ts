
import { RouterModule, Routes } from '@angular/router';
import { ModuleWithProviders } from "@angular/core";
import { DashboardExtendedComponent } from './dashboard/dashboard.component';

const routes: Routes = [
	{ path: '', component: DashboardExtendedComponent, pathMatch: 'full' },
	{ path: 'dashboard',  component: DashboardExtendedComponent   },
];

export const CoreRoutingExtendedModule: ModuleWithProviders<any> = RouterModule.forChild(routes);