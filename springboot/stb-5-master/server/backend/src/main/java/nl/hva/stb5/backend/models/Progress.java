package nl.hva.stb5.backend.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.sun.istack.NotNull;
import nl.hva.stb5.backend.views.QuestionnaireView;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "progress")
public class Progress {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "progress_id")
    @JsonView({QuestionnaireView.getView.class, QuestionnaireView.userView.class})
    private int id;

    @Column(name = "title")
    @JsonView({QuestionnaireView.getView.class, QuestionnaireView.userView.class, QuestionnaireView.postView.class})
    private String title;

    @Column(name = "description")
    @JsonView({QuestionnaireView.getView.class, QuestionnaireView.userView.class, QuestionnaireView.postView.class})
    private String description;

    @ManyToMany(mappedBy = "progresses")
//    @JsonBackReference(value = "subquestion_join_table")
    private Set<Subquestion> subquestions;

    public Progress() {
    }

    public Progress(int id, String title, String description, Set<Subquestion> subquestions) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.subquestions = subquestions;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Subquestion> getSubquestions() {
        return subquestions;
    }

    public void setSubquestions(Set<Subquestion> subquestions) {
        this.subquestions = subquestions;
    }

}

