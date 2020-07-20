import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { IonicModule } from '@ionic/angular';

import { PeldPage } from './peld.page';

describe('PeldPage', () => {
  let component: PeldPage;
  let fixture: ComponentFixture<PeldPage>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PeldPage ],
      imports: [IonicModule.forRoot()]
    }).compileComponents();

    fixture = TestBed.createComponent(PeldPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }));

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
