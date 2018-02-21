import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { BeeSharedModule } from '../../shared';
import {
    FirstService,
    FirstPopupService,
    FirstComponent,
    FirstDetailComponent,
    FirstDialogComponent,
    FirstPopupComponent,
    FirstDeletePopupComponent,
    FirstDeleteDialogComponent,
    firstRoute,
    firstPopupRoute,
    FirstResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...firstRoute,
    ...firstPopupRoute,
];

@NgModule({
    imports: [
        BeeSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        FirstComponent,
        FirstDetailComponent,
        FirstDialogComponent,
        FirstDeleteDialogComponent,
        FirstPopupComponent,
        FirstDeletePopupComponent,
    ],
    entryComponents: [
        FirstComponent,
        FirstDialogComponent,
        FirstPopupComponent,
        FirstDeleteDialogComponent,
        FirstDeletePopupComponent,
    ],
    providers: [
        FirstService,
        FirstPopupService,
        FirstResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BeeFirstModule {}
