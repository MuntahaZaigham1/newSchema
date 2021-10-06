import { NgModule } from '@angular/core';

import { PostExtendedService, PostDetailsExtendedComponent, PostListExtendedComponent, PostNewExtendedComponent } from './';
import { PostService } from 'src/app/entities/post';
import { PostModule } from 'src/app/entities/post/post.module';
import { postRoute } from './post.route';

import { SharedModule  } from 'src/app/common/shared';
import { GeneralComponentsExtendedModule } from 'src/app/extended/common/general-components/general-extended.module';

const entities = [
    PostDetailsExtendedComponent, PostListExtendedComponent, PostNewExtendedComponent 
  ]
@NgModule({
	declarations: entities,
	exports: entities,
  imports: [
    postRoute,
    PostModule,
    SharedModule,
    GeneralComponentsExtendedModule,
  ],
  providers: [{ provide: PostService, useClass: PostExtendedService }],
})
export class PostExtendedModule {
}
