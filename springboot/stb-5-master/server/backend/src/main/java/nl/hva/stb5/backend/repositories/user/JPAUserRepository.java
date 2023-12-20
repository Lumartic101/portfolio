package nl.hva.stb5.backend.repositories.user;

import nl.hva.stb5.backend.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class JPAUserRepository implements UserRepository {

    @Autowired
    private EntityManager em;

    @Override
    public User save(User user) {
        return em.merge(user);
    }

    @Override
    public User deleteById(int id) {
        User userToBeRemoved = em.find(User.class, id);

        em.remove(userToBeRemoved);
        return userToBeRemoved;
    }

    @Override
    public List<User> findAll() {
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u", User.class);

        return query.getResultList();
    }

    @Override
    public User findById(int id) {
        return em.find(User.class, id);
    }

    @Override
    public User findByEmail(String email) {
        // user emails are always unique, so there always will only be one user in the result of the query
        return em.createNamedQuery("findUserByEmail", User.class)
                .setParameter(1, email) // add the email to the JPQL query as the first parameter
                .getResultList()
                .stream()
                .findFirst()
                .orElse(null); // get the user from the result of query, or if there is no result it will be null
    }
}
