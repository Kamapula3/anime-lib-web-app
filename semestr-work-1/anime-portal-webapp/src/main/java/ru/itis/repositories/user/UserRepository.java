package ru.itis.repositories.user;

import ru.itis.models.User;
import ru.itis.repositories.CrudRepository;

public interface UserRepository extends CrudRepository<User> {
    User findByEmail(String email);

    User findByUsername(String username);

    String getUserRole(Integer roleId);

    void saveWithRole(User user);
}
