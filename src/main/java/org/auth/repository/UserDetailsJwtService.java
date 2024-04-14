package org.auth.repository;

import org.auth.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDetailsJwtService extends MongoRepository<User, String> {

    Optional<User> findUserByName(String username);
}
