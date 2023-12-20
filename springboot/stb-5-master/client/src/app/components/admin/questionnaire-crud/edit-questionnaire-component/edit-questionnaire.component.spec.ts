import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {EditQuestionnaireComponent} from './edit-questionnaire.component';
import {FormsModule} from '@angular/forms';

import {HttpClientModule} from '@angular/common/http';
import {RouterTestingModule} from "@angular/router/testing";
import {QuestionnaireService} from "../../../../services/questionnaire/questionnaire.service";

describe('Edit Questionnaire Component', () => {
  let component: EditQuestionnaireComponent;
  let componentHtml: HTMLElement;
  let fixture: ComponentFixture<EditQuestionnaireComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [EditQuestionnaireComponent],
      imports: [HttpClientModule, FormsModule, RouterTestingModule]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EditQuestionnaireComponent);
    component = fixture.componentInstance;
    componentHtml = fixture.debugElement.nativeElement;
    fixture.detectChanges();
  });

  it('01: Configuration of status should be "OPEN" based on start- and finish-date', () => {

    // Act: setting start date in the past and finish date in the future
    const date: Date = new Date();
    const start: Date = new Date(date.setDate(date.getDate() - 5));
    const finish: Date = new Date(date.setDate(date.getDate() + 10));

    component.questionnaire.startDate = start;
    component.questionnaire.finishDate = finish;

    fixture.detectChanges(); // Angular should be updated

    component.setStatus();
    expect(component.questionnaire.status).toEqual("OPEN");

  });

  it('02: Configuration of status should be "CLOSED" based on start- and finish-date', () => {

    // Act: setting start and end date in the past
    const date: Date = new Date();
    const start: Date = new Date(date.setDate(date.getDate() - 10));
    const finish: Date = new Date(date.setDate(date.getDate() + 5));

    component.questionnaire.startDate = start;
    component.questionnaire.finishDate = finish;

    fixture.detectChanges(); // Angular should be updated

    component.setStatus();
    expect(component.questionnaire.status).toEqual("CLOSED");

  });

  it('03: Configuration of status should be "CONCEPT" based on start- and finish-date', () => {

    // Act: setting start and end date in future
    const date: Date = new Date();
    const start: Date = new Date(date.setDate(date.getDate() + 5));
    const finish: Date = new Date(date.setDate(date.getDate() + 5));

    component.questionnaire.startDate = start;
    component.questionnaire.finishDate = finish;

    fixture.detectChanges(); // Angular should be updated

    component.setStatus();
    expect(component.questionnaire.status).toEqual("CONCEPT");

  });

  it('04: Upon adding pillar, changes should be made to the DOM and to the questionnaire object', () => {

    // Arrange (getting UI components)
    const selectPillarButton: HTMLButtonElement = componentHtml.querySelector('#setPillarBtn')
    const pillarNameInput: HTMLInputElement = componentHtml.querySelector('#pillarName');
    const addPillarButton: HTMLButtonElement = componentHtml.querySelector('#addPillarBtn');

    // Act: open modal and set pillar
    selectPillarButton.click();
    pillarNameInput.value = 'Onderwijs';
    pillarNameInput.dispatchEvent(new Event('input'));
    addPillarButton.click();
    fixture.detectChanges(); // Angular should be updated

    // Assert: checking if both the UI and the questionnaire object were updated
    const resultDiv: HTMLDivElement = componentHtml.querySelector('#pillarDiv');
    expect(resultDiv.innerText).toContain('Onderwijs');
    expect(component.questionnaire.pillar.name).toBe('Onderwijs');

  });

  it('05: Upon adding a cluster, changes should be made to DOM and the questionnaire object', () => {

    // Arrange (getting UI components)
    const selectPillarButton: HTMLButtonElement = componentHtml.querySelector('#setPillarBtn')
    const pillarNameInput: HTMLInputElement = componentHtml.querySelector('#pillarName');
    const addPillarButton: HTMLButtonElement = componentHtml.querySelector('#addPillarBtn');

    // Act: open modal and set pillar
    selectPillarButton.click();
    pillarNameInput.value = 'Onderwijs';
    pillarNameInput.dispatchEvent(new Event('input'));
    addPillarButton.click();
    fixture.detectChanges(); // Angular should be updated

    // Arrange (getting UI components)
    const addClusterButton: HTMLButtonElement = componentHtml.querySelector('#addClusterBtn')
    const clusterNameInput: HTMLInputElement = componentHtml.querySelector('#clusterName');
    const onAddClusterButton: HTMLButtonElement = componentHtml.querySelector('#onAddCluster');

    // Act: open modal and set pillar
    addClusterButton.click();
    clusterNameInput.value = 'Visie en strategie';
    clusterNameInput.dispatchEvent(new Event('input'));
    onAddClusterButton.click();
    fixture.detectChanges(); // Angular should be updated

    // Assert: checking if both the UI and the questionnaire object were updated
    const resultDiv: HTMLDivElement = componentHtml.querySelector('#clusterRow');
    expect(resultDiv.innerText).toContain('Visie en strategie');

    const clusters = component.questionnaire.pillar.clusters;
    const cluster = clusters.find(c => c.name === 'Visie en strategie');
    expect(clusters).toContain(cluster);

  });
})

