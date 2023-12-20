import {Component, OnInit} from '@angular/core';
import {QuestionnaireService} from "../../../../services/questionnaire/questionnaire.service";
import {ActivatedRoute, ActivatedRouteSnapshot, Router} from "@angular/router";
import {Questionnaire} from "../../../../models/questionnaire/questionnaire.model";
import {PillarService} from "../../../../services/questionnaire/pillar.service";
import {ClusterService} from "../../../../services/questionnaire/cluster.service";
import {Pillar} from "../../../../models/questionnaire/pillar.model";
import {QuestionService} from "../../../../services/questionnaire/question.service";
import {Cluster} from "../../../../models/questionnaire/cluster.model";
import {Subquestion} from "../../../../models/questionnaire/subquestion.model";
import {Location} from '@angular/common';
import {Goal} from "../../../../models/questionnaire/goal.model";
import {Status} from "../../../../models/questionnaire/status";
import {Progress} from "../../../../models/questionnaire/progress.model";

@Component({
  selector: 'app-edit-questionnaire',
  templateUrl: './edit-questionnaire.component.html',
  styleUrls: ['./edit-questionnaire.component.scss']
})
export class EditQuestionnaireComponent implements OnInit {

  //Booleans for determining which elements should be loaded into DOM
  isNewQuestionnaire: boolean = true;
  pillarSelected: boolean = false;

  //Just for the front end because id's are necessary for the component to work properly
  clusterId: number = 100000;
  goalId: number = 100000;
  subquestionId: number = 100000;

  newClusters: number[] = [];
  newGoals: number[] = [];
  newSubquestions: number[] = [];

  //Meta for post/put (back-end)
  questionnaire: Questionnaire;

  //For adding a pillar
  pillarName: string;

  //For adding or editing a cluster
  clusterName: string;

  //For adding or editing a goal
  goalName: string;

  //For adding or editing a subquestion
  subquestionName: string;
  subDesc1: string;
  subDesc2: string;
  subDesc3: string;
  subDesc4: string;
  subDesc5: string;
  subDesc6: string;

  constructor(
    private questionnaireService: QuestionnaireService,
    private questionService: QuestionService,
    private pillarService: PillarService,
    private clusterService: ClusterService,
    private router: Router,
    private route: ActivatedRoute,
    private _location: Location) {
  }

  ngOnInit(): void {

    //Fallback for when questionnaire doesn't exist
    this.questionnaire = new Questionnaire(null, null, null, null, null, null)

    //If id is not 000 which is the id for unsaved questionnaires; fetch questionnaire
    if (this.route.snapshot.data.questionnaire != null) {

      //If not 000 then pillar must have been selected
      this.pillarSelected = true;

      //Set questionnaire that is being edited and store in local variable
      this.questionnaire = this.route.snapshot.data.questionnaire;

      //If not 000 then it is not a newly created questionnaire
      this.isNewQuestionnaire = false;
    }
  }


  /**
   * Stores previous location of a user and when called routes user
   */
  onBack() {
    this._location.back();
  }

  /**
   * Responsible for setting a pillar for the questionnaire
   * @param name name of pillar
   */
  onSelectPillar(name: string) {
    this.questionnaire.pillar = new Pillar(null, name, []);
    this.pillarSelected = true;
  }

  /**
   * Responsible for adding a cluster to a pillar
   */
  onAddCluster() {

    if(this.clusterName) {
      let cluster = new Cluster(this.getNextClusterId(), this.clusterName, [], null);
      this.questionnaire.pillar.clusters.push(cluster);
      this.newClusters.push(cluster.id);
    } else alert("Please enter a name for this cluster.")
  }

  /**
   * Responsible for adding a goal to a cluster
   * @param clusterId id of cluster goal is in
   */
  onAddGoal(clusterId: number) {

    if (this.goalName) {

      //Create goal
      let goal = new Goal(this.getNextGoalId(), this.goalName, []);
      //Find cluster and add goal
      this.questionnaire.pillar.clusters.find(c => c.id == clusterId).goals.push(goal)
      //Add goal to array that is used to keep track of changes
      this.newGoals.push(goal.id);

    } else alert("Please enter a name for this goal.")
  }

  /**
   * Responsible for emptying input fields when opening the modal for adding a new question
   */
  emptyFields() {
    this.clusterName = "";
    this.goalName = "";
    this.subquestionName = "";
    this.subDesc1 = "";
    this.subDesc2 = "";
    this.subDesc3 = "";
    this.subDesc4 = "";
    this.subDesc5 = "";
    this.subDesc6 = "";
  }

  /**
   * Responsible for adding a subquestion to a goal
   * @param cluster cluster subquestion is in
   * @param clusterId id of cluster subquestion is in
   * @param goalId id of goal subquestion is in
   */
  onAddSubquestion(cluster: Cluster, clusterId: number, goalId: number) {

    //Empty array with progresses
    let progresses = [];

    //Validate fields
    if (this.subquestionName && this.subDesc1 && this.subDesc2 && this.subDesc3 && this.subDesc4 && this.subDesc5 && this.subDesc6) {
      //Fill array with newly created progresses
      progresses.push(new Progress(null, "(Nog) niet van toepassing (0)", this.subDesc1))
      progresses.push(new Progress(null, "Incidenteel (1)", this.subDesc2))
      progresses.push(new Progress(null, "Samenhangend (2)", this.subDesc3))
      progresses.push(new Progress(null, "Systematisch (3) (dit is een belangrijk element)", this.subDesc4))
      progresses.push(new Progress(null, "Systematisch en partnergericht (4)", this.subDesc5))
      progresses.push(new Progress(null, "Impactvol (5)", this.subDesc6))

      //Create a subquestion which will is filled with progresses
      let subquestion = new Subquestion(this.getNextSubquestionId(), this.subquestionName, null, null, progresses, this.questionnaire, cluster);

      //Create question including progresses and push to corresponding goal
      this.questionnaire.pillar.clusters.find(c => c.id == clusterId).goals.find(g => g.id == goalId).subquestions.push(subquestion);

      //Add to array that keeps track of changes for removing the front-end id's when saving
      this.newSubquestions.push(subquestion.id);

    } else alert("Please fill in all fields.")
  }

  /**
   * Responsible for retrieving subquestion and populating input fields of modal
   * @param clusterId id of cluster goal is in
   * @param goalId id of goal itself

   */
  loadGoal(clusterId: number, goalId: number) {
    //Find goal
    let goal = this.questionnaire.pillar.clusters.find(c => c.id == clusterId).goals.find(g => g.id == goalId);
    //Populate input field
    this.goalName = goal.name;
  }

  /**
   * Responsible for retrieving subquestion and populating input fields of modal
   * @param clusterId id of cluster itself
   */
  loadCluster(clusterId: number) {
    //Find cluster
    let cluster = this.questionnaire.pillar.clusters.find(c => c.id == clusterId);
    //Populate input field
    this.clusterName = cluster.name;
  }

  /**
   * Responsible for editing a cluster, loops through object and finds cluster then alters it
   * @param clusterId id of cluster itself
   */
  onEditCluster(clusterId: number) {

    if(this.clusterName) {
      //Find cluster and change name
      this.questionnaire.pillar.clusters.find(c => c.id == clusterId).name = this.clusterName;
    } else alert("Please fill in a name for this cluster.")
  }

  /**
   * Responsible for editing a goal, loops through object and finds goal then alters it
   * @param clusterId id of cluster subquestion is in
   * @param goalId id of goal itself
   */
  onEditGoal(clusterId: number, goalId: number) {

    if(this.goalName) {
      //Find goal and change name
      this.questionnaire.pillar.clusters.find(c => c.id == clusterId).goals.find(g => g.id == goalId).name = this.goalName;
    } else alert("Please fill in a name for this goal.")
  }

  /**
   * Responsible for retrieving subquestion and populating input fields of modal
   * @param clusterId id of cluster subquestion is in
   * @param goalId id of goal subquestion is in
   * @param subquestionId id of subquestion itself
   */
  loadSubquestion(clusterId: number, goalId: number, subquestionId: number) {
    //Find subquestion
    let subquestion = this.questionnaire.pillar.clusters.find(c => c.id == clusterId).goals.find(g => g.id == goalId).subquestions.find(s => s.id == subquestionId);
    //Filter array of subquestions and populate input fields
    this.subquestionName = subquestion.name;
    this.subDesc1 = subquestion.progresses.find(p => p.title === "(Nog) niet van toepassing (0)").description;
    this.subDesc2 = subquestion.progresses.find(p => p.title === "Incidenteel (1)").description;
    this.subDesc3 = subquestion.progresses.find(p => p.title === "Samenhangend (2)").description;
    this.subDesc4 = subquestion.progresses.find(p => p.title === "Systematisch (3) (dit is een belangrijk element)").description;
    this.subDesc5 = subquestion.progresses.find(p => p.title === "Systematisch en partnergericht (4)").description;
    this.subDesc6 = subquestion.progresses.find(p => p.title === "Impactvol (5)").description;
  }

  /**
   * Responsible for editing a subquestion, loops through object and finds subquestion then alters it
   * @param clusterId id of cluster subquestion is in
   * @param goalId id of goal subquestion is in
   * @param subquestionId id of subquestion itself
   */
  onEditSubquestion(clusterId: number, goalId: number, subquestionId: number) {

    //Validate fields
    if (this.subquestionName && this.subDesc1 && this.subDesc2 && this.subDesc3 && this.subDesc4 && this.subDesc5 && this.subDesc6) {
      //Find subquestion
      let subquestion = this.questionnaire.pillar.clusters.find(c => c.id == clusterId).goals.find(g => g.id == goalId).subquestions.find(s => s.id == subquestionId);
      //Apply changes
      subquestion.name = this.subquestionName;
      subquestion.progresses[0].description = this.subDesc1;
      subquestion.progresses[1].description = this.subDesc2;
      subquestion.progresses[2].description = this.subDesc3;
      subquestion.progresses[3].description = this.subDesc4;
      subquestion.progresses[4].description = this.subDesc5;
      subquestion.progresses[5].description = this.subDesc6;

    } else alert("Please fill in all fields.")
  }

  /**
   * Responsible for removing a cluster, loops through object and finds cluster then removes it
   * @param clusterId id of cluster itself
   */
  onRemoveCluster(clusterId: number) {
    //Find index of cluster to be deleted
    let index = this.questionnaire.pillar.clusters.findIndex(c => c.id == clusterId);
    //Remove cluster by index
    this.questionnaire.pillar.clusters.splice(index, 1);

    //Do the same for the array with newly created cluster
    let anotherIndex = this.newClusters.findIndex(cId => cId == clusterId);
    this.newClusters.splice(anotherIndex, 1);
  }

  /**
   * Responsible for removing a goal, loops through object and finds goal then removes it
   * @param clusterId id of cluster goal is in
   * @param goalId id of goal itself
   */
  onRemoveGoal(clusterId: number, goalId: number) {
    //Find index of goal to be deleted
    let index = this.questionnaire.pillar.clusters.find(c => c.id == clusterId).goals.findIndex(g => g.id == goalId);
    //Remove goal by index
    this.questionnaire.pillar.clusters.find(c => c.id == clusterId).goals.splice(index, 1);

    //Do the same for the array with newly created cluster
    let anotherIndex = this.newGoals.findIndex(gId => gId == goalId);
    this.newGoals.splice(anotherIndex, 1);
  }

  /**
   * Responsible for removing a subquestion, loops through object and finds subquestion then removes it
   * @param clusterId id of cluster subquestion is in
   * @param goalId id of goal subquestion is in
   * @param subquestionId id of subquestion itself
   */
  onRemoveSubquestion(clusterId: number, goalId: number, subquestionId: number) {
    //Find index of question to be deleted
    let index = this.questionnaire.pillar.clusters.find(c => c.id == clusterId).goals.find(g => g.id == goalId).subquestions.findIndex(s => s.id == subquestionId);
    //Remove question by index
    this.questionnaire.pillar.clusters.find(c => c.id == clusterId).goals.find(g => g.id == goalId).subquestions.splice(index, 1);

    //Do the same for the array with newly created cluster
    let anotherIndex = this.newSubquestions.findIndex(sId => sId == subquestionId);
    this.newSubquestions.splice(anotherIndex, 1);
  }


  /**
   * Responsible for validating input fields which can never be null
   * Makes sure start date comes after finish date
   * @return boolean false along with error message if any faulty fields are present, true if not.
   */
  validateFields(): boolean {

    if (this.questionnaire.startDate != null && this.questionnaire.finishDate != null) {

      let startDate = this.questionnaire.startDate.valueOf();
      let finishDate = this.questionnaire.finishDate.valueOf();

      if (this.questionnaire.name == "") {
        alert("Please enter a name.");
        return false;
      } else if (this.questionnaire.startDate == null || this.questionnaire.finishDate == null) {
        alert("Please select a start and finish date.");
        return false;
      } else if (this.questionnaire.finishDate < this.questionnaire.startDate) {
        alert("End date can't be before start date.");
        return false;
      } else if (startDate > finishDate) {
        alert("Start date must always be later than finish date.")
        return false;
      } else return true;
    }
    alert("Please fill in a start and finish date.")
    return false;
  }

  /**
   * Responsible for configuring the status based upon start and end date
   * @return boolean false if configuring failed, true if it succeeded
   */
  setStatus(): boolean {

    if (this.questionnaire.startDate != null && this.questionnaire.finishDate != null) {
      let today = new Date().valueOf();
      let startDate = new Date(this.questionnaire.startDate).valueOf();
      let finishDate = new Date(this.questionnaire.finishDate).valueOf();

      //Set correct status based on dates user entered
      if (finishDate <= today) {
        this.questionnaire.status = Status.CLOSED;
        return true;
      } else if (startDate > today) {
        this.questionnaire.status = Status.CONCEPT;
        return true;
      } else if (startDate < today && finishDate > today) {
        this.questionnaire.status = Status.OPEN;
        return true;
      }
    }
    return false;
  }

  /**
   * Responsible for making sure object is ready for submittal to the back-end
   * Removes any unnecessary front-end fields and removes front-end ids
   */
  onSave() {

    if (this.validateFields() && this.setStatus()) {

      //For each newly created cluster, set front-end id to null
      for (let cluster of this.questionnaire.pillar.clusters) {
        for (let i = 0; i < this.newClusters.length; i++) {
          if (this.newClusters[i] == cluster.id) {
            cluster.id = null
          }
        }
      }

      //For each newly created goal, set front-end id to null
      for (let cluster of this.questionnaire.pillar.clusters) {
        for (let goal of cluster.goals) {
          for (let i = 0; i < this.newGoals.length; i++) {
            if (this.newGoals[i] == goal.id) {
              goal.id = null
            }
          }
        }
      }

      //For each newly created subquestion, set front-end id to null
      for (let cluster of this.questionnaire.pillar.clusters) {
        for (let goal of cluster.goals) {
          for (let sub of goal.subquestions) {
            for (let i = 0; i < this.newSubquestions.length; i++) {
              if (this.newSubquestions[i] == sub.id) {
                sub.id = null
              }
            }
          }
        }
      }

      //Save questionnaire and return status based on promise
      let promise = this.questionnaireService.save(this.questionnaire);
      promise.then(
        () => {
          alert("Questionnaire has been submitted with status " + this.questionnaire.status + "!");
          this.router.navigate(["/dashboard/overview"]);
        },
        () => {
          alert("An error occurred while submitting, please try again!")
        });
    }
  }

  //Helper methods
  getNextClusterId(): number {
    return this.clusterId++;
  }

  getNextGoalId(): number {
    return this.goalId++;
  }

  getNextSubquestionId(): number {
    return this.subquestionId++;
  }

}
