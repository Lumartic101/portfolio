package nl.hva.stb5.backend.models;

import com.fasterxml.jackson.annotation.*;
import com.sun.istack.NotNull;
import nl.hva.stb5.backend.views.EducationView;
import nl.hva.stb5.backend.views.UserView;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Education")
public class Education {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView({EducationView.base.class, UserView.UserEducationFaculty.class})
    @Column(name = "id")
    private int id;

    @JsonView({EducationView.base.class, UserView.UserEducationFaculty.class})
    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "faculty_id")
    @JsonView({UserView.UserEducationFaculty.class})
    private Faculty faculty;

    @OneToMany(mappedBy="education", cascade = CascadeType.ALL)
    private Set<User> users;

    public Education() {
    }

    public Education(int id, String name, Faculty faculty, Set<User> users) {
        this.id = id;
        this.name = name;
        this.faculty = faculty;
        this.users = users;
    }

    public Education(int id, String name, Faculty faculty) {
        this.id = id;
        this.name = name;
        this.faculty = faculty;
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

    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }
}
