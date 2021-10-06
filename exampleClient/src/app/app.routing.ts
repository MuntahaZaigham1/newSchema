
import { RouterModule, Routes } from '@angular/router';
import { ModuleWithProviders } from "@angular/core";
import { SwaggerComponent } from './core/swagger/swagger.component';
import { ErrorPageComponent  } from './core/error-page/error-page.component';

const routes: Routes = [
	{
		path: '',
		loadChildren: () => import('./extended/core/core.module').then(m => m.CoreExtendedModule),
	},
  	{ path: "swagger-ui", component: SwaggerComponent },
	{
		path: 'post',
		loadChildren: () => import('./extended/entities/post/post.module').then(m => m.PostExtendedModule),

	},
	{
		path: 'writer',
		loadChildren: () => import('./extended/entities/writer/writer.module').then(m => m.WriterExtendedModule),

	},
	{ path: '**', component:ErrorPageComponent},
	
];

export const routingModule: ModuleWithProviders<any> = RouterModule.forRoot(routes);