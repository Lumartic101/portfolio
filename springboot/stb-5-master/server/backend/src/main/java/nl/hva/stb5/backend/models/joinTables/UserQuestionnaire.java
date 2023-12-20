package nl.hva.stb5.backend.models.joinTables;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import nl.hva.stb5.backend.models.Questionnaire;
import nl.hva.stb5.backend.models.User;
import nl.hva.stb5.backend.models.compositeKeys.UserQuestionnaireKey;

import javax.persistence.*;

@Entity
public class UserQuestionnaire {
    @EmbeddedId
    UserQuestionnaireKey id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
//    @JsonManagedReference(value = "user_join_table")
    User userFromJoinTable;

    @ManyToOne
    @MapsId("questionnaireId")
    @JoinColumn(name = "questionnaire_id")
//    @JsonManagedReference(value = "questionnaire_join_table")
    Questionnaire questionnaireFromJoinTable;

    public UserQuestionnaire(UserQuestionnaireKey id, User userFromJoinTable, Questionnaire questionnaireFromJoinTable) {
        this.id = id;
        this.userFromJoinTable = userFromJoinTable;
        this.questionnaireFromJoinTable = questionnaireFromJoinTable;
    }

    public UserQuestionnaire() {

    }

    public UserQuestionnaireKey getId() {
        return id;
    }

    public void setId(UserQuestionnaireKey id) {
        this.id = id;
    }

    public User getUserFromJoinTable() {
        return userFromJoinTable;
    }

    public void setUserFromJoinTable(User userFromJoinTable) {
        this.userFromJoinTable = userFromJoinTable;
    }

    public Questionnaire getQuestionnaireFromJoinTable() {
        return questionnaireFromJoinTable;
    }

    public void setQuestionnaireFromJoinTable(Questionnaire questionnaireFromJoinTable) {
        this.questionnaireFromJoinTable = questionnaireFromJoinTable;
    }
}
