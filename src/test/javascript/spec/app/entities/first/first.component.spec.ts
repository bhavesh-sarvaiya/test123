/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { BeeTestModule } from '../../../test.module';
import { FirstComponent } from '../../../../../../main/webapp/app/entities/first/first.component';
import { FirstService } from '../../../../../../main/webapp/app/entities/first/first.service';
import { First } from '../../../../../../main/webapp/app/entities/first/first.model';

describe('Component Tests', () => {

    describe('First Management Component', () => {
        let comp: FirstComponent;
        let fixture: ComponentFixture<FirstComponent>;
        let service: FirstService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [BeeTestModule],
                declarations: [FirstComponent],
                providers: [
                    FirstService
                ]
            })
            .overrideTemplate(FirstComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(FirstComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(FirstService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new First(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.firsts[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
