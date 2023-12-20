# HVA Dashboard Sustainability

:cloud: HVA Project - HVA Dashboard Sustainability

# Setup

### Dependencies - Before using the project install!

- MySQL 
- NodeJS - https://nodejs.org/en/download/ - Take the LTS version.
- Git
- IntelliJ Ultimate Edition


### Steps to start the project for the first time

1. Clone or download the project to your device.
2. File -> Open the cloned directory in IntelliJ.
3. While the project is open press left under the screen the tab >_Terminal. After you have pressed that type there " cd client ". After that you have selected the client folder there is the packaged json file we need to install. 
4. Write npm install and wait untill it is finished. After that you can restart intelij or refresh the project directory.
If you are getting a error it means probably that you dont have the latest version of nodejs. 
If you find hard to follow these steps you can also find the package.json file and press on it with your right click of the mouse. Then you need to select run "npm install" and it wil do it for you.
5. Now the front end is up and running in order to run the backend you need to go to "server/backend/main/java/nl.hva.stb5.backend/" BackendApplication. Here you need to run this class by pressing the green arrow next to the class after that you can always run it from the above 
tab in IntelliJ. Now the Server is running for backend and frontend.

6. If you get errors with the backend you will need to reload the maven pom.xml in the backend directory.

### Deployment on live server

1. After a commit is made you need to make a merge request in gitlab to staging.
2. When the merge is accepted and merged, you will need to wait a couple of minuts until it is pushed on the live server.
3. After everything is settled it is live and avaibale at : .....

# Work that has been done

### Mortada
I have worked on everything that had to do with the back-end as that was one of my learning goals for this project
Furthermore i also helped some of my team-members with their parts of this project

###### Front-end
- education.ts
- faculty.ts
- usercomponent.ts, specifically creating users in the front end to be sent to the backend
- education-sb.service.ts
- faculty-sb.service.ts

###### Backend
##### Models
- User
- Subquestion
- Progress
- Questionnaire
- Progress
- Pillar
- Goal
- Faculty
- Education
- Answer
- Cluster
- Role enum
- Status enum

##### Controllers
- Answer controller
- Cluster controller
- Education controller
- Faculty controller
- Goal controller
- Pillar controller
- Progress Controller
- Questionnaire Controller
- Subquestion Controller
- WebMvcConfig
##### Repositories
- AnswerRepository
- ClusterRepository
- EducationRepository
- FacultyRepository
- GoalRepository
- PillarRepository
- ProgressRepository
- QuestionnaireRepository
- SubquestionRepository


### Luka

I have worked on everything that is going on with the login page. That includes all the checks session checking and showing the right menu
at the navigation bar. I also worked with Konrad for the JWT tokens that we made for frontend and backend, that right now is checking on every page for a token for extra security.

Here are the classes that I have worked on
##### front end
- question-overview.component all of them some changes on the way where made by teammates
- login.component all of them.
- nav-bar.component I changed a part of the html so that if your are admin or user it wil only then show some particulair navigation menu.
- Everything within the authentication folder in the front end. So , auth.service, auth-guard-admin.service.ts, auth-guard-loggedin.service.ts, auth-interceptor.service.ts this I have made with Konrad as Duo.
- changed routing for authguard

##### Backend
- AuthController.java
- All in Security folder. JWTokeninfo.java, JWTtokenUtils.java, JWTRequestfilter.java, PasswordEncoder.java.
- All in Exceptions folder. AuthenticationException.java, AuthorizationException.java, PreConditionFailedException.java, ResourceNotFoundException.
- Usercontroller.java only added the JWT checks and password encoder.
- JPAUserRepository.java changed functions for the JWTtoken.

### Mika

During this project my main focus has been the component for creating and updating a questionnaire, for this part I have worked a lot on both the front and backend. 
I also worked on the questionnaire service together with Erik and I helped shape the overview component.   

Here are the classes that I have worked on

##### Front-end
- All classes of edit-questionnaire-component 
- Some functionalities in component questionnaire-overview
- questionnaire-service.ts and all models related to it like pillar, cluster etc. together with Erik
- Added a resolver to the routing module
- questionnaire-resolver.ts

##### Back-end
- Configured mapping, view and end-point for pillar but this turned out to be redundant later
- Helped to configure mapping for questionnaire GET, POST, PUT and made some views.
- Anything that had to do with being able to create and update a questionnaire in the back-end.

### Konrad
During this project my main focus has been the chart component for displaying the answers for each faculty and eduction within the HvA.
For this I have worked on both the front-end and the back-end, for the front-end I have created a chart by using chart.js in combination with ng2-charts. 
The data displayed in the chart can be filtered using _**optional**_ query params which are passed through the HTTP GET Request, for this I have created a utils function 
that only adds the passed in parameters to avoid undefined values being passed to the request. In the back-end this request is being resolved by the GET mapping, on which it can 
perform several queries based on the passed query parameters.

I have also worked on the Authorization and Authentication of the application together with Luka, for this I have changed the already created login to use the back-end in order to authenticate the 
user by using JWT tokens. This login is then being stored within the session storage. This token is then being used by an HTTP interceptor in order to add it to the header of every request. In all the needed
back-end mappings I have added a check that checks if the JWT is accoladed to an admin user or not (authorization). In the front-end I have also changed the existing auth guard in order to check if an user is 
an admin (if he can access the admin panel) and if the user is logged in (has access to logged in part of application). 

Furthermore, I have also worked on the creation of users, for this I have created the user overview page where users from the back-end are displayed and can be deleted using the service
that makes HTTP requests to the back-end in order to create, update and delete users. I have also created a simple homepage, and have also created a listview of the available questionnaires for the logged-in user.

Lastly I was also responsible for the CI/CD of the project, I have created a pipeline that is being triggered when something is being deployed from the staging branch, this is triggered by the .gitlab-ci.yml file which I have configured. 

Here are the different parts of the application I have worked on 

##### Front-end
###### Components
- Chart Component
- Users Component
- Forbidden Component
- Home Component
- Navbar Component
- User Questionnaire Overview Component

###### Services
- chart.service.ts
- auth.service.ts
- auth-guard-admin.service.ts
- auth-guard-logged-in.service.ts
- auth-interceptor.service.ts
- users-sb.service.ts

###### Models
- answer.ts
- datapoint.ts
- dataset.ts
- user.ts

###### Utils
- http-params.ts

##### Back-end
###### Models
- User
- Answer
- Role (enum)

###### Controllers
- AnswerController
- AuthController
- UserController

###### Classes
- JWTokenInfo 
- JWTokenUtils 
- JWTRequestFilter 
- PasswordEncoder

###### Repositories
- JPAUserRepository
- AnswerRepository

### Erik
For this project my focus was on answering questionnaires, and viewing available ones as a user. For this i have mostly worked on frontend such as the answer-questionnaire view and questionnaire service. 
On backend i have made several jsonViews and api endpoints to facilitate getting data for the frontend

##### Frontend
###### Components
- Answer questionnaire component and its subcomponents cluster, goal and question component
- User questionnaire overview component (in the latter half of the project when we actually connected it to the database)

###### Services
- submission service
- user parts of questionnaire service

###### Models
- questionnaire
- pillar
- goal
- cluster
- subquestion
- service
- progress


##### Back-end
- user endpoints in questionnaire controller
- json views for user endpoints
