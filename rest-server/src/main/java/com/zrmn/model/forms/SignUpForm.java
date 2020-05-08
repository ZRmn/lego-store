package com.zrmn.model.forms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SignUpForm
{
    private String login;
    private String password;
    private String fullName;
    private String phoneNumber;
}
