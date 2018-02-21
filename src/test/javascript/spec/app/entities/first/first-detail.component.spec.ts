/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { BeeTestModule } from '../../../test.module';
import { FirstDetailComponent } from '../../../../../../main/webapp/app/entities/first/first-detail.component';
import { FirstService } from '../../../../../../main/webapp/app/entities/first/first.service';
import { First } from '../../../../../../main/webapp/app/entities/first/first.model';

describe('Component Tests', () => {

    describe('First Management Detail Component', () => {
        let comp: FirstDetailComponent;
        let fixture: ComponentFixture<FirstDetailComponent>;
        let service: FirstService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [BeeTestModule],
                declarations: [FirstDetailComponent],
                providers: [
                    FirstService
                ]
            })
            .overrideTemplate(FirstDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(FirstDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(FirstService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new First(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.first).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
