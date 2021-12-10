package com.example.socialnetwork.Domain.Validator;

import com.example.socialnetwork.Domain.User;

public class UserValidator implements Validator<User> {
    @Override
    public void validate(User entity) throws ValidationException {
        String errors = "";
        if (entity.getFirstName().equals(""))
            errors = errors+"FirstName is null!\n";
        if (entity.getLastName().equals(""))
            errors = errors+"LastName is null!\n";
        if (entity.getUsername().equals(""))
            errors = errors+"UserName is null!\n";
        if(!entity.getFirstName().matches("[a-zA-z]+"))
            errors = errors+"FirstName is invalid!\n";
        if(!entity.getLastName().matches("[a-zA-z]+"))
            errors = errors+"LastName is invalid!\n";

        if (!errors.equals(""))
            throw new ValidationException(errors);
    }
}