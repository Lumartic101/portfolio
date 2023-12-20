package nl.hva.stb5.backend.views;

public class QuestionnaireView {
    public interface base { }
    public interface postView {}
    public interface getView {}
    public interface userPreview { }
    public interface userView extends userPreview{ }
    public interface adminPreview extends userPreview { }
}
