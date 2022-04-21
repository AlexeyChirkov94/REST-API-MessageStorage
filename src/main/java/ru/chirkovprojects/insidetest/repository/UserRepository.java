package ru.chirkovprojects.insidetest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.chirkovprojects.insidetest.entity.User;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findAllByUsername(String username);

}
