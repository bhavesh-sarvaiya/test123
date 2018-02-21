import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { First } from './first.model';
import { FirstPopupService } from './first-popup.service';
import { FirstService } from './first.service';

@Component({
    selector: 'jhi-first-delete-dialog',
    templateUrl: './first-delete-dialog.component.html'
})
export class FirstDeleteDialogComponent {

    first: First;

    constructor(
        private firstService: FirstService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.firstService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'firstListModification',
                content: 'Deleted an first'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-first-delete-popup',
    template: ''
})
export class FirstDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private firstPopupService: FirstPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.firstPopupService
                .open(FirstDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
