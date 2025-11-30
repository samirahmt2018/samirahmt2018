package com.gundan.terragold.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data                // generates getters, setters, toString, equals, hashCode
@NoArgsConstructor   // generates default constructor
  // generates constructor with all fields
public class UserDto {
    private Long id;
    private String username;
    private String email;

    public UserDto(Long id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public Long getId() {
        return id;
    }
}
