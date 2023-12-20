import {Injectable} from '@angular/core';
import {
  ActivatedRouteSnapshot,
  Resolve,
  Router,
} from '@angular/router';
import {EMPTY, Observable, of} from 'rxjs';
import {QuestionnaireService} from "../../services/questionnaire/questionnaire.service";
import {catchError} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class editQuestionnaireResolver implements Resolve<any> {

  constructor(private questionnaireService: QuestionnaireService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<any> {

    if (route.paramMap.get('id') != "000") {
      return this.questionnaireService.getById(+route.paramMap.get('id')).pipe(catchError(() =>
      {
        this.router.navigate(['/**']);
        return EMPTY;
      }
      ));
    }
    return undefined;
  }
}
