package org.auth.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Document(collection = "user")
@Data
public class User {

    @Id
    private String id;
    @Field(value = "name")
    private String name;
    @Field(value = "email")
    private String email;
    private String password;

    private Boolean isDisabled;

    private Boolean isAccountExpired;

    private Boolean isAccountLocked;

    private Collection<? extends GrantedAuthority> authorities;

    private Boolean credientialsExpired;

    @DBRef
    private Set<Roles> roles = new HashSet<>();

}
