package nl.hva.stb5.backend.models.compositeKeys;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class UserQuestionnaireKey implements Serializable {

    @Column(name = "user_id")
    int userId;
    @Column(name = "questionnaire_id")
    int questionnaireId;

    public UserQuestionnaireKey(int userId, int questionnaireId) {
        this.userId = userId;
        this.questionnaireId = questionnaireId;
    }

    public UserQuestionnaireKey() {

    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getQuestionnaireId() {
        return questionnaireId;
    }

    public void setQuestionnaireId(int questionnaireId) {
        this.questionnaireId = questionnaireId;
    }
}
