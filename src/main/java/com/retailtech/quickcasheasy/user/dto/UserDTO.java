package com.retailtech.quickcasheasy.user.dto;

import com.retailtech.quickcasheasy.user.UserRole;

public class UserDTO {

    private Long id;
    private String userName;
    private String password;
    private UserRole role;

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

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
