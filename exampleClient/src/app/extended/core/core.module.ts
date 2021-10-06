import { NgModule } from '@angular/core';
import { MainNavExtendedComponent } from './main-nav/main-nav.component';
import { DashboardExtendedComponent } from './dashboard/dashboard.component';
import { SharedModule } from 'src/app/common/shared';
import { CoreRoutingExtendedModule } from './core.routing';
import { CoreModule } from 'src/app/core/core.module';

const components = [
    DashboardExtendedComponent,

    MainNavExtendedComponent
]
@NgModule({
	declarations: components,
	exports: components,
  imports: [
    SharedModule,
    CoreRoutingExtendedModule,
    CoreModule
  ]
})
export class CoreExtendedModule {
}
