package com.example.socialnetwork.Domain.Validator;


import com.example.socialnetwork.Domain.FriendRequest;

public class FriendRequestValidator implements Validator<FriendRequest> {
    @Override
    public void validate(FriendRequest entity) throws ValidationException {
        if(entity.getSenderID() == null || entity.getReceiverID() == null)
            throw new ValidationException("Friend Request invalid");
    }
}