package nl.hva.stb5.backend.models;

import com.fasterxml.jackson.annotation.*;
import com.sun.istack.NotNull;
import nl.hva.stb5.backend.views.QuestionnaireView;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "subquestion")
@JsonIdentityInfo(scope=Subquestion.class, generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Subquestion {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    @Column(name = "subquestion_id")
    @JsonView({QuestionnaireView.getView.class, QuestionnaireView.userView.class})
    private int id;

    @Column(name = "name")
    @JsonView({QuestionnaireView.getView.class, QuestionnaireView.userView.class, QuestionnaireView.postView.class})
    private String name;

    @ManyToOne
    @JoinColumn(name = "goal_id")
    @JsonBackReference(value = "subquestions")
    private Goal goal;

    @ManyToMany(cascade = {
            CascadeType.ALL
    })
    @JoinTable(
            name = "progress_subquestion",
            joinColumns = @JoinColumn(name = "subquestion_id"),
            inverseJoinColumns = @JoinColumn(name = "progress_id"))
    @JsonView({QuestionnaireView.getView.class, QuestionnaireView.userView.class, QuestionnaireView.postView.class})
    private Set<Progress> progresses;

    public Subquestion() {}

    public Subquestion(int id, String name, Goal goal, Set<Progress> progresses) {
        this.id = id;
        this.name = name;
        this.goal = goal;
        this.progresses = progresses;
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

    public Goal getGoal() {
        return goal;
    }

    public void setGoal(Goal goal) {
        this.goal = goal;
    }

    public Set<Progress> getProgresses() {
        return progresses;
    }

    public void setProgresses(Set<Progress> progresses) {
        this.progresses = progresses;
    }
}

