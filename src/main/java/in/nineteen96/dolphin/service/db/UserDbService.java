package in.nineteen96.dolphin.service.db;

import in.nineteen96.dolphin.entity.User;

import java.util.Optional;

public interface UserDbService {

    User save(User user);

    Optional<User> findUserByID(String id);

    Optional<User> findUserByEmail(String email);

    User findByEmail(String email);
}
