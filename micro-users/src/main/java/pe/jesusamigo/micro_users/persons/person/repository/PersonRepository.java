package pe.jesusamigo.micro_users.persons.person.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.jesusamigo.micro_users.persons.person.entity.Person;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    Optional<Person> findByUserUsername(String username);

    Optional<Person> findByDni(String documentNumber);

    Optional<Person> findByUserId(Integer id);
}
