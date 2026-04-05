package com.disciplineledger.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserProfileResponse {
    private Long userId;
    private String name;
    private String email;
}
