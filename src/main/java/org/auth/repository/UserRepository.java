package org.auth.repository;

import org.auth.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findUserByName(String username);

    @Query(value = "{ 'name' : ?0 }", exists = true)
    Boolean name(String name);

    @Query(value = "{ 'email' : ?0 }", exists = true)
    Boolean email(String email);
}
