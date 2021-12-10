package com.example.socialnetwork.Domain.Validator;

import com.example.socialnetwork.Domain.Message;

public class MessageValidator implements Validator<Message>{
    @Override
    public void validate(Message entity) throws ValidationException {
        String errors = "";
        if (entity.getMessage().equals(""))
            errors+="Message must not be null!";
        if (!errors.equals(""))
            throw new ValidationException(errors);
    }
}
