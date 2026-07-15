import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ContatoDialog } from './contato-dialog';

describe('ContatoDialog', () => {
  let component: ContatoDialog;
  let fixture: ComponentFixture<ContatoDialog>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ContatoDialog],
    }).compileComponents();

    fixture = TestBed.createComponent(ContatoDialog);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
