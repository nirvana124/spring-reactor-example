package com.nirvana.springreactorexample.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Profile {
    private String id;
    private String name;
    private String mobile;
    private String email;
}
