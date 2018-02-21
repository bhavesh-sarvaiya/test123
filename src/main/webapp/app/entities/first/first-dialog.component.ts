import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { First } from './first.model';
import { FirstPopupService } from './first-popup.service';
import { FirstService } from './first.service';

@Component({
    selector: 'jhi-first-dialog',
    templateUrl: './first-dialog.component.html'
})
export class FirstDialogComponent implements OnInit {

    first: First;
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private firstService: FirstService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.first.id !== undefined) {
            this.subscribeToSaveResponse(
                this.firstService.update(this.first));
        } else {
            this.subscribeToSaveResponse(
                this.firstService.create(this.first));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<First>>) {
        result.subscribe((res: HttpResponse<First>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: First) {
        this.eventManager.broadcast({ name: 'firstListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

@Component({
    selector: 'jhi-first-popup',
    template: ''
})
export class FirstPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private firstPopupService: FirstPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.firstPopupService
                    .open(FirstDialogComponent as Component, params['id']);
            } else {
                this.firstPopupService
                    .open(FirstDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
