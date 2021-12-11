package com.example.socialnetwork.Repository.Database;


import com.example.socialnetwork.Domain.FriendRequest;
import com.example.socialnetwork.Domain.Validator.ValidationException;
import com.example.socialnetwork.Domain.Validator.Validator;
import com.example.socialnetwork.Repository.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FriendRequestDBRepository implements Repository<Long, FriendRequest> {
    private final String url;
    private final String username;
    private final String password;
    private Validator<FriendRequest> validator;

    private final String tableName;

    public FriendRequestDBRepository(String url, String username, String password, Validator<FriendRequest> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
        this.tableName = "friend_requests";
    }

    private FriendRequest extractEntity(ResultSet resultSet) {
        try {
            Long id = resultSet.getLong("id");
            Long senderID = resultSet.getLong("sender_id");
            Long receiverID = resultSet.getLong("receiver_id");
            String status = resultSet.getString("status");
            String date = resultSet.getString("date");

            FriendRequest friendRequest = new FriendRequest(senderID, receiverID, status, LocalDateTime.parse(date));
            friendRequest.setId(id);

            return friendRequest;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String createEntityAsString(FriendRequest entity) {
        return entity.getId()+";"+entity.getSenderID()+";"+entity.getReceiverID()+";"+entity.getStatus()+";"+entity.getDate();
    }

    @Override
    public FriendRequest findOne(Long id) {
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
    public Iterable<FriendRequest> findAll() {
        Map<Long, FriendRequest> entities=new HashMap<Long, FriendRequest>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from " + tableName);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                FriendRequest entity = extractEntity(resultSet);
                entities.put(entity.getId(), entity);
            }
            return entities.values();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public FriendRequest save(FriendRequest entity) {
        if(entity == null)
            throw new ValidationException("entity must not be null");

        validator.validate(entity);

        if(findOne(entity.getId()) != null)
            return entity;

        String sql = "insert into friend_requests (id, sender_id, receiver_id, status, date) values (?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            List<String> attributes = Arrays.asList(createEntityAsString(entity).split(";"));

            ps.setInt(1, Integer.parseInt(attributes.get(0)));
            ps.setInt(2, Integer.parseInt(attributes.get(1)));
            ps.setInt(3, Integer.parseInt(attributes.get(2)));
            ps.setString(4, attributes.get(3));
            ps.setString(5, attributes.get(4));

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public FriendRequest delete(Long id) {
        if(id == null)
            throw new ValidationException("id must not be null");
        if(findOne(id) == null)
            return null;

        FriendRequest ret = findOne(id);

        String sql = "delete from friend_requests where id = ?";
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
    public FriendRequest update(FriendRequest entity) {
        if(entity == null)
            throw new IllegalArgumentException("entity must be not null!");
        validator.validate(entity);

        if(findOne(entity.getId()) != null){
            String sql = "update friend_requests set sender_id=(?),receiver_id=(?),status=(?),date=(?) where id = (?)";
            try (Connection connection = DriverManager.getConnection(url, username, password);
                 PreparedStatement ps = connection.prepareStatement(sql)) {

                List<String> attributes = Arrays.asList(createEntityAsString(entity).split(";"));

                ps.setInt(1,Integer.parseInt(attributes.get(1)));
                ps.setInt(2,Integer.parseInt(attributes.get(2)));
                ps.setString(3,attributes.get(3));
                ps.setString(4,attributes.get(4));
                ps.setInt(5,Integer.parseInt(attributes.get(0)));

                ps.executeUpdate();
                return null;

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return entity;
    }

}