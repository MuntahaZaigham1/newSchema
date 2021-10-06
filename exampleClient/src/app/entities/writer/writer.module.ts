import { NgModule } from '@angular/core';

import { WriterDetailsComponent, WriterListComponent, WriterNewComponent} from './';
import { writerRoute } from './writer.route';

import { SharedModule } from 'src/app/common/shared';
import { GeneralComponentsModule } from 'src/app/common/general-components/general.module';

const entities = [
    WriterDetailsComponent, WriterListComponent,WriterNewComponent
  ]
@NgModule({
	declarations: entities,
	exports: entities,
  imports: [
    writerRoute,
    SharedModule,
    GeneralComponentsModule,
  ]
})
export class WriterModule {
}
