package com.ultikits.ultitools.dao;

import com.ultikits.data.HibernateData;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class UserInfo extends HibernateData {
    @Id
    private String uuid;
    private String username;
    private String password;
    private boolean whitelisted;
    private boolean banned;
}
