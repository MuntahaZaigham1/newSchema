import { NgModule } from '@angular/core';

import { PostDetailsComponent, PostListComponent, PostNewComponent} from './';
import { postRoute } from './post.route';

import { SharedModule } from 'src/app/common/shared';
import { GeneralComponentsModule } from 'src/app/common/general-components/general.module';

const entities = [
    PostDetailsComponent, PostListComponent,PostNewComponent
  ]
@NgModule({
	declarations: entities,
	exports: entities,
  imports: [
    postRoute,
    SharedModule,
    GeneralComponentsModule,
  ]
})
export class PostModule {
}
