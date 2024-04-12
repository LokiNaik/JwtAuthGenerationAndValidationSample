package org.auth.model;

import java.io.Serializable;

public class JwtResponse implements Serializable {

    private String jwtToken;

    public JwtResponse() {
    }

    @Override
    public String toString() {
        return "JwtResponse{" +
                "jwtToken='" + jwtToken + '\'' +
                '}';
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}
