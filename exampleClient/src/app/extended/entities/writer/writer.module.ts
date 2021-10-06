import { NgModule } from '@angular/core';

import { WriterExtendedService, WriterDetailsExtendedComponent, WriterListExtendedComponent, WriterNewExtendedComponent } from './';
import { WriterService } from 'src/app/entities/writer';
import { WriterModule } from 'src/app/entities/writer/writer.module';
import { writerRoute } from './writer.route';

import { SharedModule  } from 'src/app/common/shared';
import { GeneralComponentsExtendedModule } from 'src/app/extended/common/general-components/general-extended.module';

const entities = [
    WriterDetailsExtendedComponent, WriterListExtendedComponent, WriterNewExtendedComponent 
  ]
@NgModule({
	declarations: entities,
	exports: entities,
  imports: [
    writerRoute,
    WriterModule,
    SharedModule,
    GeneralComponentsExtendedModule,
  ],
  providers: [{ provide: WriterService, useClass: WriterExtendedService }],
})
export class WriterExtendedModule {
}
