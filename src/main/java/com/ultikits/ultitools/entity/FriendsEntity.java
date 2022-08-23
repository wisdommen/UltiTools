package com.ultikits.ultitools.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
public class FriendsEntity {
    @Id
    private String uuid;
    private String username;
    private
}
