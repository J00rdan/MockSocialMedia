package com.example.socialnetwork.Repository.Database;

import com.example.socialnetwork.Domain.Message;
import com.example.socialnetwork.Domain.Validator.ValidationException;
import com.example.socialnetwork.Domain.Validator.Validator;
import com.example.socialnetwork.Repository.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class MessageDBRepository implements Repository<Long, Message> {
    private final String url;
    private final String username;
    private final String password;
    private Validator<Message> validator;

    private final String tableName;

    public MessageDBRepository(String url, String username, String password, Validator<Message> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
        this.tableName = "messages";
    }

    private String createEntityAsString(Message entity)
    {
        return entity.getId()+";"+entity.getFrom()+";"+entity.getTo()+";"+entity.getMessage()+";"+entity.getDate()+";"+entity.getReply();
    }

    private Message extractEntity(ResultSet resultSet) {
        try {
            Long id = resultSet.getLong("id");
            Long idFrom = resultSet.getLong("sender");
            String receivers = resultSet.getString("receivers");
            String message = resultSet.getString("message");
            String date = resultSet.getString("date");
            Long reply = resultSet.getLong("reply");

            receivers = receivers.substring(1,receivers.length()-1);
            ArrayList<Long> to = new ArrayList<>();
            if (!receivers.equals("")){
                List<String> allReceivers = Arrays.asList(receivers.split(", "));

                for (String id_r: allReceivers)
                {
                    to.add(Long.parseLong(id_r));
                }
            }

            Message message1 = new Message(idFrom, message, LocalDateTime.parse(date), reply);
            message1.setId(id);
            if (to.size()!=0)
                message1.setTo(to);
            return message1;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Message findOne(Long id) {
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
    public Iterable<Message> findAll() {
        Map<Long, Message> entities=new HashMap<Long, Message>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from " + tableName);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Message entity = extractEntity(resultSet);
                entities.put(entity.getId(), entity);
            }
            return entities.values();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Message save(Message entity) {
        if(entity == null)
            throw new ValidationException("entity must not be null");

        validator.validate(entity);

        if(findOne(entity.getId()) != null)
            return entity;

        String sql = "insert into messages (id, sender, receivers, message, date, reply) values (?, ?, ?, ?, ?, ?)";
        String sql2 = "insert into messages (id, sender, receivers, message, date) values (?, ?, ?, ?, ?)";
        if (entity.getReply() != null)
        {
            try (Connection connection = DriverManager.getConnection(url, username, password);
                 PreparedStatement ps = connection.prepareStatement(sql)) {

                List<String> attributes = Arrays.asList(createEntityAsString(entity).split(";"));

                ps.setInt(1, Integer.parseInt(attributes.get(0)));
                ps.setInt(2, Integer.parseInt(attributes.get(1)));
                ps.setString(3, attributes.get(2));
                ps.setString(4, attributes.get(3));
                ps.setString(5,attributes.get(4));
                ps.setInt(6,Integer.parseInt(attributes.get(5)));

                ps.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else
        {
            try (Connection connection = DriverManager.getConnection(url, username, password);
                 PreparedStatement ps2 = connection.prepareStatement(sql2)) {

                List<String> attributes = Arrays.asList(createEntityAsString(entity).split(";"));

                ps2.setInt(1, Integer.parseInt(attributes.get(0)));
                ps2.setInt(2, Integer.parseInt(attributes.get(1)));
                ps2.setString(3, attributes.get(2));
                ps2.setString(4, attributes.get(3));
                ps2.setString(5,attributes.get(4));

                ps2.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public Message delete(Long id) {
        if(id == null)
            throw new ValidationException("id must not be null");
        if(findOne(id) == null)
            return null;
        Message deleted = findOne(id);

        String sql = "delete from messages where id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            List<String> attributes = Arrays.asList(createEntityAsString(findOne(id)).split(";"));

            ps.setInt(1, Integer.parseInt(attributes.get(0)));

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return deleted;
    }

    @Override
    public Message update(Message entity) {
        if(entity == null)
            throw new IllegalArgumentException("entity must be not null!");
        validator.validate(entity);

        if(findOne(entity.getId()) != null){
            String sql = "update messages set sender=(?),receivers=(?),message=(?),date=(?),reply = NULL where id = (?)";
            String sql2 = "update messages set sender=(?),receivers=(?),message=(?),date=(?)where id = (?)";
            if (entity.getReply() == null)
            {
                try (Connection connection = DriverManager.getConnection(url, username, password);
                     PreparedStatement ps = connection.prepareStatement(sql)) {

                    List<String> attributes = Arrays.asList(createEntityAsString(entity).split(";"));

                    ps.setInt(1, Integer.parseInt(attributes.get(1)));
                    ps.setString(2, attributes.get(2));
                    ps.setString(3, attributes.get(3));
                    ps.setString(4,attributes.get(4));
                   // ps.setInt(5,Integer.parseInt(attributes.get(5)));
                    ps.setInt(5, Integer.parseInt(attributes.get(0)));

                    ps.executeUpdate();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                try (Connection connection = DriverManager.getConnection(url, username, password);
                     PreparedStatement ps2 = connection.prepareStatement(sql2)) {

                    List<String> attributes = Arrays.asList(createEntityAsString(entity).split(";"));

                    ps2.setInt(1, Integer.parseInt(attributes.get(1)));
                    ps2.setString(2, attributes.get(2));
                    ps2.setString(3, attributes.get(3));
                    ps2.setString(4,attributes.get(4));
                    ps2.setInt(5, Integer.parseInt(attributes.get(0)));

                    ps2.executeUpdate();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return entity;
    }
}
