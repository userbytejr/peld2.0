import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { IonicModule } from '@ionic/angular';

import { BytePage } from './byte.page';

describe('BytePage', () => {
  let component: BytePage;
  let fixture: ComponentFixture<BytePage>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BytePage ],
      imports: [IonicModule.forRoot()]
    }).compileComponents();

    fixture = TestBed.createComponent(BytePage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
