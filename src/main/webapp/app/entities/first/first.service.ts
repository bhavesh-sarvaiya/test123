import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { First } from './first.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<First>;

@Injectable()
export class FirstService {

    private resourceUrl =  SERVER_API_URL + 'api/firsts';

    constructor(private http: HttpClient) { }

    create(first: First): Observable<EntityResponseType> {
        const copy = this.convert(first);
        return this.http.post<First>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(first: First): Observable<EntityResponseType> {
        const copy = this.convert(first);
        return this.http.put<First>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<First>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<First[]>> {
        const options = createRequestOption(req);
        return this.http.get<First[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<First[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: First = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<First[]>): HttpResponse<First[]> {
        const jsonResponse: First[] = res.body;
        const body: First[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to First.
     */
    private convertItemFromServer(first: First): First {
        const copy: First = Object.assign({}, first);
        return copy;
    }

    /**
     * Convert a First to a JSON which can be sent to the server.
     */
    private convert(first: First): First {
        const copy: First = Object.assign({}, first);
        return copy;
    }
}
