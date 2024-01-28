package com.coursehub.dto;

/**
 * Projection for {@link com.coursehub.model.User}
 */
public interface UserInfo {
    Long getUserId();

    String getSocialId();

    String getFirstName();

    String getLastName();

    String getEmail();

    String getPassword();
}