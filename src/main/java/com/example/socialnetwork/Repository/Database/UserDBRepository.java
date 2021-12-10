package com.example.socialnetwork.Repository.Database;


import com.example.socialnetwork.Domain.User;
import com.example.socialnetwork.Domain.Validator.ValidationException;
import com.example.socialnetwork.Domain.Validator.Validator;
import com.example.socialnetwork.Repository.Repository;

import java.sql.*;
import java.util.*;

public class UserDBRepository implements Repository<Long, User> {
    private final String url;
    private final String username;
    private final String password;
    private Validator<User> validator;

    private final String tableName;

    public UserDBRepository(String url, String username, String password, Validator<User> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
        this.tableName = "users";
    }

    private User extractEntity(ResultSet resultSet) {
        try {
            Long id = resultSet.getLong("id");
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            String userName = resultSet.getString("username");

            User user = new User(firstName, lastName, userName);

            String friend = resultSet.getString("friends");

            if(friend != null){
                friend = friend.substring(1, friend.length()-1);

                if(!friend.equals("")) {

                    List<String> friends = Arrays.asList(friend.split(", "));
                    ArrayList<Long> newFriends = new ArrayList<>();
                    for (String id_s : friends) {
                        newFriends.add(Long.parseLong(id_s));
                    }
                    user.setFriends(newFriends);
                }
            }

            user.setId(id);

            return user;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String createEntityAsString(User entity) {
        return entity.getId()+";"+entity.getFirstName()+";"+entity.getLastName()+";"+entity.getUsername() + ";" +entity.getFriends();
    }

    @Override
    public User findOne(Long id) {
        if (id==null)
            throw new ValidationException("id must be not null");

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from " + tableName + " where id=?")) {

            statement.setInt(1,Integer.parseInt(String.valueOf(id)));
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                return extractEntity(resultSet);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<User> findAll() {
        Map<Long, User> entities=new HashMap<Long, User>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from " + tableName);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                User entity = extractEntity(resultSet);
                entities.put(entity.getId(), entity);
            }
            return entities.values();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public User save(User entity) {
        if(entity == null)
            throw new ValidationException("entity must not be null");

        validator.validate(entity);

        if(findOne(entity.getId()) != null)
            return entity;

        String sql = "insert into users (id, first_name, last_name, username, friends) values (?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            List<String> attributes = Arrays.asList(createEntityAsString(entity).split(";"));

            ps.setInt(1, Integer.parseInt(attributes.get(0)));
            ps.setString(2, attributes.get(1));
            ps.setString(3, attributes.get(2));
            ps.setString(4, attributes.get(3));
            ps.setString(5, attributes.get(4));

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User delete(Long id) {
        if(id == null)
            throw new ValidationException("id must not be null");
        if(findOne(id) == null)
            return null;

        User ret = findOne(id);

        String sql = "delete from users where id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            List<String> attributes = Arrays.asList(createEntityAsString(findOne(id)).split(";"));

            ps.setInt(1, Integer.parseInt(attributes.get(0)));

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public User update(User entity) {
        if(entity == null)
            throw new IllegalArgumentException("entity must be not null!");
        validator.validate(entity);

        if(findOne(entity.getId()) != null){
            String sql = "update users set first_name=(?),last_name=(?),username=(?),friends=(?) where id = (?)";
            try (Connection connection = DriverManager.getConnection(url, username, password);
                 PreparedStatement ps = connection.prepareStatement(sql)) {

                List<String> attributes = Arrays.asList(createEntityAsString(entity).split(";"));

                ps.setString(1,attributes.get(1));
                ps.setString(2,attributes.get(2));
                ps.setString(3,attributes.get(3));
                ps.setString(4,attributes.get(4));
                ps.setInt(5, Integer.parseInt(attributes.get(0)));

                ps.executeUpdate();
                return null;

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return entity;
    }

}
