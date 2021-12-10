package com.example.socialnetwork.Repository.File;


import com.example.socialnetwork.Domain.Friendship;
import com.example.socialnetwork.Domain.Validator.Validator;

import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

public class FriendshipFileRepository extends AbstractFileRepository<Long, Friendship>{
    public FriendshipFileRepository(String fileName, Validator<Friendship> validator) throws FileNotFoundException {
        super(fileName, validator);
    }

    @Override
    public Friendship extractEntity(List<String> attributes) {
        Friendship friendship = new Friendship(Long.parseLong(attributes.get(1)),Long.parseLong(attributes.get(2)), LocalDateTime.parse(attributes.get(3)));
        friendship.setId(Long.parseLong(attributes.get(0)));
        return friendship;
    }

    @Override
    protected String createEntityAsString(Friendship entity) {
        return entity.getId()+";"+ entity.getID1()+";"+ entity.getID2()+";"+entity.getDate();
    }
}
