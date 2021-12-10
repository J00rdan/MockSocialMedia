package com.example.socialnetwork.Repository.File;


import com.example.socialnetwork.Domain.User;
import com.example.socialnetwork.Domain.Validator.Validator;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserFileRepository extends AbstractFileRepository<Long, User> {
    public UserFileRepository(String fileName, Validator<User> validator) throws FileNotFoundException {
        super(fileName, validator);
    }


    @Override
    public User extractEntity(List<String> attributes) {
        User user = new User(attributes.get(1),attributes.get(2),attributes.get(3));

        String friend = attributes.get(4);
        friend = friend.substring(1, friend.length()-1);

        if(!friend.equals("")) {
            //System.out.println("asd");
            List<String> friends = Arrays.asList(friend.split(", "));
            ArrayList<Long> newFriends = new ArrayList<>();
            for (String id_s : friends) {
                newFriends.add(Long.parseLong(id_s));
            }
            user.setFriends(newFriends);
        }

        user.setId(Long.parseLong(attributes.get(0)));

        return user;
    }

    @Override
    protected String createEntityAsString(User entity) {
        return entity.getId()+";"+entity.getFirstName()+";"+entity.getLastName()+";"+entity.getUsername() + ";" +entity.getFriends();
    }
}