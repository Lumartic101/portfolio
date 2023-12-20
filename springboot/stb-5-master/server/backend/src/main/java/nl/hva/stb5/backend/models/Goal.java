package nl.hva.stb5.backend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.sun.istack.NotNull;
import nl.hva.stb5.backend.views.QuestionnaireView;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "goal")
public class Goal {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    @JsonView({QuestionnaireView.getView.class, QuestionnaireView.userView.class})
    private int id;

    @Column(name = "name")
    @JsonView({QuestionnaireView.getView.class, QuestionnaireView.userView.class, QuestionnaireView.postView.class})
    private String name;

    @ManyToOne
    @JoinColumn(name = "cluster_id")
    @JsonBackReference(value = "cluster")
    private Cluster cluster;

    @OneToMany(mappedBy = "goal", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "subquestions")
    @JsonView({QuestionnaireView.getView.class, QuestionnaireView.userView.class, QuestionnaireView.postView.class})
    private Set<Subquestion> subquestions;

    public Goal() {
    }

    public Goal(int id, String name, Cluster cluster, Set<Subquestion> subquestions) {
        this.id = id;
        this.name = name;
        this.cluster = cluster;
        this.subquestions = subquestions;
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

    public Cluster getCluster() {
        return cluster;
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    public Set<Subquestion> getSubquestions() {
        return subquestions;
    }

    public void setSubquestions(Set<Subquestion> subquestions) {
        this.subquestions = subquestions;
    }
}
