//package nl.hva.stb5.backend.models.user;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//
//import java.util.Objects;
//
//public class User {
//    public enum UserRole {
//        ADMIN_USER, GENERAL_USER
//    }
//
//    private Integer id;
//    private String email;
//
//    @JsonIgnore
//    private String password;    //ignore the password for safety reasons
//
//    private String firstName;
//    private String lastName;
//    private String faculty;
//    private String department;
//    private UserRole userRole;
//
//    public User() {
//
//    }
//
//    // constructor for users that are admin
//    public User(Integer id, String email, String password, String firstName, String lastName, UserRole userRole) {
//        this();
//        this.id = id;
//        this.email = email;
//        this.password = password;
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.userRole = userRole;
//    }
//
//    public User(Integer id, String email, String password, String firstName, String lastName, UserRole userRole, String faculty, String department) {
//        this(id, email, password, firstName, lastName, userRole);
//        this.faculty = faculty;
//        this.department = department;
//    }
//
//    public Integer getId() {
//        return id;
//    }
//
//    public void setId(Integer id) {
//        this.id = id;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public String getFirstName() {
//        return firstName;
//    }
//
//    public void setFirstName(String firstName) {
//        this.firstName = firstName;
//    }
//
//    public String getLastName() {
//        return lastName;
//    }
//
//    public void setLastName(String lastName) {
//        this.lastName = lastName;
//    }
//
//    public String getFaculty() {
//        return faculty;
//    }
//
//    public void setFaculty(String faculty) {
//        this.faculty = faculty;
//    }
//
//    public String getDepartment() {
//        return department;
//    }
//
//    public void setDepartment(String department) {
//        this.department = department;
//    }
//
//    public UserRole getUserRole() {
//        return userRole;
//    }
//
//    public void setUserRole(UserRole userRole) {
//        this.userRole = userRole;
//    }
//
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof User user)) return false;
//        return getId().equals(user.getId()) && getEmail().equals(user.getEmail()) && getPassword().equals(user.getPassword()) && getFirstName().equals(user.getFirstName()) && getLastName().equals(user.getLastName()) && Objects.equals(getFaculty(), user.getFaculty()) && Objects.equals(getDepartment(), user.getDepartment()) && getUserRole() == user.getUserRole();
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(getId(), getEmail(), getPassword(), getFirstName(), getLastName(), getFaculty(), getDepartment(), getUserRole());
//    }
//
//    @Override
//    public String toString() {
//        return "User{" +
//                "id=" + id +
//                ", email='" + email + '\'' +
//                ", password='" + password + '\'' +
//                ", firstName='" + firstName + '\'' +
//                ", lastName='" + lastName + '\'' +
//                ", faculty='" + faculty + '\'' +
//                ", department='" + department + '\'' +
//                ", userRole=" + userRole +
//                '}';
//    }
//
//}
