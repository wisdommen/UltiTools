package com.ultikits.ultitools.entity;

import com.ultikits.data.HibernateData;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;


@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
public class UserInfoEntity extends HibernateData {
    @Id
    private String uuid;
    private String username;
    private String password;
    private String email;
    @Builder.Default
    private boolean whitelisted = false;
    @Builder.Default
    private boolean banned = false;
}
