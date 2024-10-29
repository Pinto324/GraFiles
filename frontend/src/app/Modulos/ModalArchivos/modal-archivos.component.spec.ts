import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalArchivosComponent } from '../ModalArchivos/modal-archivos.component';

describe('ModalArchivosComponent', () => {
  let component: ModalArchivosComponent;
  let fixture: ComponentFixture<ModalArchivosComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ModalArchivosComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ModalArchivosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
