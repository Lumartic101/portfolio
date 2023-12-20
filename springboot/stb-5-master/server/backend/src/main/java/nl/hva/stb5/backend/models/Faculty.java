package nl.hva.stb5.backend.models;

import com.fasterxml.jackson.annotation.*;
import com.sun.istack.NotNull;
import nl.hva.stb5.backend.views.EducationView;
import nl.hva.stb5.backend.views.FacultyView;
import nl.hva.stb5.backend.views.UserView;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "faculty")
public class Faculty {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @JsonView({FacultyView.base.class, UserView.UserEducationFaculty.class})
    private int id;

    @Column(name = "name")
    @JsonView({FacultyView.base.class, UserView.UserEducationFaculty.class})
    private String name;

    @OneToMany(mappedBy="faculty")
    @JsonBackReference(value = "faculty_education")
    private Set<Education> educations = new HashSet<>();

    public Faculty() {
    }

    public Faculty(int id, String name, Set<Education> educations) {
        this.id = id;
        this.name = name;
        this.educations = educations;
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


    public Set<Education> getEducations() {
        return educations;
    }

    public void setEducations(Set<Education> educations) {
        this.educations = educations;
    }


}
