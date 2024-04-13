package org.auth.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Document(collection = "user")
@Data
public class User {

    @Id
    private String id;
    private String name;
    private String email;
    private String password;
    @DBRef
    private Set<Roles> roles = new HashSet<>();

}
