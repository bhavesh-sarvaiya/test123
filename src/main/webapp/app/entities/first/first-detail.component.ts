import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { First } from './first.model';
import { FirstService } from './first.service';

@Component({
    selector: 'jhi-first-detail',
    templateUrl: './first-detail.component.html'
})
export class FirstDetailComponent implements OnInit, OnDestroy {

    first: First;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private firstService: FirstService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInFirsts();
    }

    load(id) {
        this.firstService.find(id)
            .subscribe((firstResponse: HttpResponse<First>) => {
                this.first = firstResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInFirsts() {
        this.eventSubscriber = this.eventManager.subscribe(
            'firstListModification',
            (response) => this.load(this.first.id)
        );
    }
}
