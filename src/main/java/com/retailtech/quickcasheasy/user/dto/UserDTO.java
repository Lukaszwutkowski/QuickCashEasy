package com.retailtech.quickcasheasy.user.dto;

import com.retailtech.quickcasheasy.user.UserRole;

public class UserDTO {

    private final Long id;
    private final String userName;
    private final String password;
    private final UserRole role;

    /**
     * Constructor to initialize all fields.
     *
     * @param id       the unique identifier of the user
     * @param userName the username of the user
     * @param password the password of the user
     * @param role     the role of the user
     */
    public UserDTO(Long id, String userName, String password, UserRole role) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.role = role;
    }

    // Getters

    public Long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public UserRole getRole() {
        return role;
    }
}
