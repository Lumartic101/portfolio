package nl.hva.stb5.backend.models;

import com.fasterxml.jackson.annotation.*;
import com.sun.istack.NotNull;
import nl.hva.stb5.backend.models.joinTables.UserQuestionnaire;
import nl.hva.stb5.backend.views.QuestionnaireView;

import javax.persistence.*;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Questionnaire")
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
@NamedQueries({
        @NamedQuery(name = "get_available_questionnaires_for_user",
                query = "SELECT q FROM Questionnaire q WHERE q.status = 'OPEN'"),
        @NamedQuery(name = "get_questionnaire_for_user",
                query = "SELECT q FROM Questionnaire q WHERE q.status = 'OPEN' AND q.id = ?1")
})

public class Questionnaire {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView({QuestionnaireView.getView.class, QuestionnaireView.userPreview.class})
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    @JsonView({QuestionnaireView.base.class, QuestionnaireView.getView.class, QuestionnaireView.postView.class, QuestionnaireView.userPreview.class})
    private String name;

    @Column(name = "start_date")
    @JsonView({QuestionnaireView.base.class, QuestionnaireView.getView.class, QuestionnaireView.postView.class, QuestionnaireView.adminPreview.class})
    private Timestamp startDate;

    @Column(name = "finish_date")
    @JsonView({QuestionnaireView.base.class, QuestionnaireView.userPreview.class, QuestionnaireView.getView.class, QuestionnaireView.postView.class})
    private Timestamp finishDate;
    
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    @JsonView({QuestionnaireView.getView.class, QuestionnaireView.postView.class, QuestionnaireView.adminPreview.class})
    private Status status;

    @OneToOne(cascade = CascadeType.ALL, targetEntity = Pillar.class)
    @JoinColumn(name = "pillar_id", referencedColumnName = "id")
//    @JsonManagedReference(value = "questionnaire_pillar")
    @JsonView({QuestionnaireView.getView.class, QuestionnaireView.postView.class, QuestionnaireView.adminPreview.class, QuestionnaireView.userView.class})
    private Pillar pillar;

    @OneToMany(mappedBy="questionnaire")
    @JsonIgnore
    @JsonManagedReference(value = "questionnaire_answer")
    private Set<Answer> answers;

    // TODO remove
    @OneToMany(mappedBy = "questionnaireFromJoinTable")
    @JsonBackReference(value = "questionnaire_join_table")
    @JsonView({QuestionnaireView.getView.class})
    private Set<UserQuestionnaire> users = new HashSet<>();

    public Questionnaire() {
    }

    public Questionnaire(int id, String name, Timestamp startDate, Timestamp finishDate, Status status, Pillar pillar, Set<Answer> answers, Set<UserQuestionnaire> users) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.status = status;
        this.pillar = pillar;
        this.answers = answers;
        this.users = users;
    }

    public Questionnaire(int id, String name, Timestamp startDate, Timestamp finishDate, Status status, Pillar pillar) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.status = status;
        this.pillar = pillar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Pillar getPillar() {
        return pillar;
    }

    public Set<Answer> getAnswers() {
        return answers;
    }

    public Set<UserQuestionnaire> getUsers() {
        return users;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp start_date) {
        this.startDate = start_date;
    }

    public Timestamp getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Timestamp finish_date) {
        this.finishDate = finish_date;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setPillar(Pillar pillar) {
        this.pillar = pillar;
    }

    public void setAnswers(Set<Answer> answers) {
        this.answers = answers;
    }

    public void setUsers(Set<UserQuestionnaire> users) {
        this.users = users;
    }

}
