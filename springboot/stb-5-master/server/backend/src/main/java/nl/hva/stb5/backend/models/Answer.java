package nl.hva.stb5.backend.models;

import com.fasterxml.jackson.annotation.JsonView;
import com.sun.istack.NotNull;
import nl.hva.stb5.backend.views.AnswerView;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@NamedQueries({
        @NamedQuery(name="find_all_answers",
                query = "SELECT a.score, a.submitDate, a.user.education.faculty.name, a.questionnaire.pillar.name FROM Answer a"),
        @NamedQuery(name="find_answers_by_pillar",
                query = "SELECT a.score, a.submitDate, a.user.education.faculty.name, a.questionnaire.pillar.name FROM Answer a WHERE a.questionnaire.pillar.name = ?1"),
        @NamedQuery(name="find_answers_by_faculty",
                query = "SELECT a.score, a.submitDate, a.user.education.name, a.questionnaire.pillar.name FROM Answer a WHERE a.user.education.faculty.name = ?1"),
        @NamedQuery(name="find_answers_by_pillar_and_faculty",
                query = "SELECT a.score, a.submitDate, a.user.education.name, a.questionnaire.pillar.name FROM Answer a WHERE a.questionnaire.pillar.name = ?1 AND a.user.education.faculty.name = ?2")
})
public class Answer {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @JsonView({AnswerView.base.class, AnswerView.submission.class})
    @Column(name = "score")
    private double score;

    @JsonView({AnswerView.base.class})
    @Column(name = "submit_date")
    private Timestamp submitDate;

    @ManyToOne(targetEntity = Questionnaire.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "questionnaire_id", insertable = false, updatable = false)
    private Questionnaire questionnaire;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @JsonView({AnswerView.base.class, AnswerView.submission.class})
    @Column(name = "questionnaire_id")
    private int questionnaireId;

    @JsonView({AnswerView.base.class})
    @Column(name = "user_id")
    private int userId;

    public Answer() {

    }

    public Answer(int id, double score, Timestamp submitDate, Questionnaire questionnaire, User user) {
        this.id = id;
        this.score = score;
        this.submitDate = submitDate;
        this.questionnaire = questionnaire;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public Timestamp getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(Timestamp submitDate) {
        this.submitDate = submitDate;
    }

    public int getQuestionnaireId() {
        return questionnaireId;
    }

    public void setQuestionnaireId(int questionnaireId) {
        this.questionnaireId = questionnaireId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", score=" + score +
                ", submitDate=" + submitDate +
                ", questionnaire=" + questionnaire +
                ", user=" + user +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Answer)) return false;
        Answer answer = (Answer) o;
        return getId() == answer.getId() && Double.compare(answer.getScore(), getScore()) == 0 && Objects.equals(getSubmitDate(), answer.getSubmitDate());
    }

}
