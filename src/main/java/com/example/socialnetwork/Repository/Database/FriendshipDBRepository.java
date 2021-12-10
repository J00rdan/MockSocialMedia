package com.example.socialnetwork.Repository.Database;

import com.example.socialnetwork.Domain.Friendship;
import com.example.socialnetwork.Domain.Validator.ValidationException;
import com.example.socialnetwork.Domain.Validator.Validator;
import com.example.socialnetwork.Repository.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FriendshipDBRepository  implements Repository<Long, Friendship> {
    private final String url;
    private final String username;
    private final String password;
    private Validator<Friendship> validator;

    private final String tableName;

    public FriendshipDBRepository(String url, String username, String password, Validator<Friendship> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
        this.tableName = "friendships";

    }

    private Friendship extractEntity(ResultSet resultSet) {
        try {
            Long id = resultSet.getLong("id");
            Long id1 = resultSet.getLong("id1");
            Long id2 = resultSet.getLong("id2");
            String date = resultSet.getString("date");

            Friendship friendship = new Friendship(id1,id2, LocalDateTime.parse(date));
            friendship.setId(id);
            return friendship;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String createEntityAsString(Friendship entity) {
        return entity.getId()+";"+ entity.getID1()+";"+ entity.getID2()+";"+entity.getDate();
    }

    @Override
    public Friendship findOne(Long id) {
        if (id==null)
            throw new ValidationException("id must be not null");

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from " + tableName + " where id=?")) {

            statement.setLong(1,id);
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next())
                return extractEntity(resultSet);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<Friendship> findAll() {
        Map<Long, Friendship> entities=new HashMap<Long, Friendship>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from " + tableName);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Friendship entity = extractEntity(resultSet);
                entities.put(entity.getId(), entity);
            }
            return entities.values();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Friendship save(Friendship entity) {
        if(entity == null)
            throw new ValidationException("entity must not be null");

        validator.validate(entity);

        if(findOne(entity.getId()) != null)
            return entity;

        String sql = "insert into friendships (id, id1, id2, date) values (?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            List<String> attributes = Arrays.asList(createEntityAsString(entity).split(";"));

            ps.setInt(1, Integer.parseInt(attributes.get(0)));
            ps.setInt(2, Integer.parseInt(attributes.get(1)));
            ps.setInt(3, Integer.parseInt(attributes.get(2)));
            ps.setString(4, attributes.get(3));

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Friendship delete(Long id) {
        if(id == null)
            throw new ValidationException("id must not be null");
        if(findOne(id) == null)
            return null;

        Friendship ret = findOne(id);

        String sql = "delete from friendships where id = ?";
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
    public Friendship update(Friendship entity) {
        if(entity == null)
            throw new IllegalArgumentException("entity must be not null!");
        validator.validate(entity);

        if(findOne(entity.getId()) != null){
            String sql = "update friendships set id1=(?),id2=(?),date=(?) where id = (?)";
            try (Connection connection = DriverManager.getConnection(url, username, password);
                 PreparedStatement ps = connection.prepareStatement(sql)) {

                List<String> attributes = Arrays.asList(createEntityAsString(entity).split(";"));

                ps.setInt(1,Integer.parseInt(attributes.get(1)));
                ps.setInt(2,Integer.parseInt(attributes.get(2)));
                ps.setString(3,attributes.get(3));
                ps.setInt(4,Integer.parseInt(attributes.get(0)));

                ps.executeUpdate();
                return null;

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return entity;
    }
}
