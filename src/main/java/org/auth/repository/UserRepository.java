package org.auth.repository;

import org.auth.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Repository;

@EnableMongoRepositories
@Repository
public interface UserRepository extends MongoRepository<User, String> {

    @Query(value = "{ 'name' : ?0 }", exists = true)
    Boolean name(String name);

    @Query(value = "{ 'email' : ?0 }", exists = true)
    Boolean email(String email);

}
