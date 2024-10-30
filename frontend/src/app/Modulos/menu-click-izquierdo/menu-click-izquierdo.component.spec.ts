import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MenuClickIzquierdoComponent } from './menu-click-izquierdo.component';

describe('MenuClickIzquierdoComponent', () => {
  let component: MenuClickIzquierdoComponent;
  let fixture: ComponentFixture<MenuClickIzquierdoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MenuClickIzquierdoComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MenuClickIzquierdoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
