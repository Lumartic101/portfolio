package nl.hva.stb5.backend.views;

public class UserView {

    public interface base { }
    public interface id { }

    public interface UserEducationFaculty extends base, EducationView.base, FacultyView.base {}

}
