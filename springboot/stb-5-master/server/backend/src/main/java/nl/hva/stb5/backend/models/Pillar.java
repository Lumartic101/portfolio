package nl.hva.stb5.backend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.sun.istack.NotNull;
import nl.hva.stb5.backend.views.PillarView;
import nl.hva.stb5.backend.views.QuestionnaireView;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "Pillar")
public class Pillar {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    @JsonView({PillarView.getView.class, QuestionnaireView.getView.class, QuestionnaireView.postView.class})
    private int id;

    @Column(name = "name")
    @JsonView({PillarView.getView.class, QuestionnaireView.getView.class, QuestionnaireView.adminPreview.class, QuestionnaireView.userView.class, QuestionnaireView.postView.class})
    private String name;

    @OneToMany(mappedBy="pillar", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "pillar_cluster")
    @JsonView({QuestionnaireView.getView.class, QuestionnaireView.userView.class, QuestionnaireView.postView.class, PillarView.getView.class})
    private Set<Cluster> clusters = new HashSet<>();

    @OneToOne(mappedBy = "pillar", fetch = FetchType.LAZY)
    @JsonView({QuestionnaireView.postView.class})
    private Questionnaire questionnaire;

    public Pillar() {
    }

    public Pillar(int id, String name, Set<Cluster> clusters, Questionnaire questionnaire) {
        this.id = id;
        this.name = name;
        this.clusters = clusters;
        this.questionnaire = questionnaire;
    }

    public Pillar(int id, String name) {
        this.id = id;
        this.name = name;
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

    public Set<Cluster> getClusters() {
        return clusters;
    }

    public void setClusters(Set<Cluster> clusters) {
        this.clusters = clusters;
    }


}
