import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { FirstComponent } from './first.component';
import { FirstDetailComponent } from './first-detail.component';
import { FirstPopupComponent } from './first-dialog.component';
import { FirstDeletePopupComponent } from './first-delete-dialog.component';

@Injectable()
export class FirstResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const firstRoute: Routes = [
    {
        path: 'first',
        component: FirstComponent,
        resolve: {
            'pagingParams': FirstResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'beeApp.first.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'first/:id',
        component: FirstDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'beeApp.first.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const firstPopupRoute: Routes = [
    {
        path: 'first-new',
        component: FirstPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'beeApp.first.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'first/:id/edit',
        component: FirstPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'beeApp.first.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'first/:id/delete',
        component: FirstDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'beeApp.first.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
