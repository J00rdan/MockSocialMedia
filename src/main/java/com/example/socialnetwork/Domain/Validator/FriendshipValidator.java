package com.example.socialnetwork.Domain.Validator;

import com.example.socialnetwork.Domain.Friendship;

public class FriendshipValidator implements Validator<Friendship> {
    @Override
    public void validate(Friendship entity) throws ValidationException {
        if(entity.getID1() == null || entity.getID2() == null)
            throw new FriendshipException("Usernames invalid");
    }
}