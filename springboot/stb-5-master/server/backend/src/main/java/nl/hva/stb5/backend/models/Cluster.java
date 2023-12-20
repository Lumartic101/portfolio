package nl.hva.stb5.backend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.sun.istack.NotNull;
import nl.hva.stb5.backend.views.PillarView;
import nl.hva.stb5.backend.views.QuestionnaireView;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "cluster")
public class Cluster {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    @JsonView({PillarView.getView.class, QuestionnaireView.getView.class, QuestionnaireView.userView.class})
    private int id;

    @Column(name = "name")
    @JsonView({PillarView.getView.class, QuestionnaireView.getView.class, QuestionnaireView.userView.class, QuestionnaireView.postView.class})
    private String name;

    @ManyToOne
    @JoinColumn(name = "pillar_id")
    @JsonBackReference(value = "pillar_cluster")
    @JsonView({PillarView.getView.class})
    private Pillar pillar;

    @OneToMany(mappedBy = "cluster", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "cluster")
    @JsonView({QuestionnaireView.getView.class, QuestionnaireView.userView.class, QuestionnaireView.postView.class})
    private Set<Goal> goals;

    public Cluster() {}

    public Cluster(int id, String name, Pillar pillar, Set<Goal> goals) {
        this.id = id;
        this.name = name;
        this.pillar = pillar;
        this.goals = goals;
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

    public void setPillar(Pillar pillar) {
        this.pillar = pillar;
    }

    public Set<Goal> getGoals() {
        return goals;
    }

    public void setGoals(Set<Goal> goals) {
        this.goals = goals;
    }
}
