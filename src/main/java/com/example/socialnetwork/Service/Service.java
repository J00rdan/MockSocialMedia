package com.example.socialnetwork.Service;


import com.example.socialnetwork.Domain.*;
import com.example.socialnetwork.Domain.Validator.DuplicateException;
import com.example.socialnetwork.Domain.Validator.EntityNotFoundException;
import com.example.socialnetwork.Domain.Validator.ValidationException;
import com.example.socialnetwork.Repository.Repository;
import com.example.socialnetwork.Utils.ComparatorByDate;
import com.example.socialnetwork.Utils.Graph;

import java.util.*;

public class Service {
    private Repository<Long, User> userRepository;
    private Repository<Long, Friendship> friendshipRepository;
    private Repository<Long, Message> messageRepository;
    private Repository<Long, FriendRequest> friendRequestRepository;
    private static Long userId = 1L;
    private static Long friendshipId = 1L;
    private static Long messageId = 1L;
    private static Long friendRequestId = 1L;

    public Service(Repository<Long, User> userRepository, Repository<Long, Friendship> friendshipRepository,Repository<Long, Message> messageRepository,
                   Repository<Long, FriendRequest> friendRequestRepository) {
        this.userRepository = userRepository;
        this.friendshipRepository = friendshipRepository;
        this.messageRepository = messageRepository;
        this.friendRequestRepository = friendRequestRepository;
        incrementUserID();
        incrementFriendshipID();
        incrementMessageID();
        incrementFriendRequestID();
    }

    private void incrementUserID(){
        for(User e: userRepository.findAll()){
            if(e.getId() > userId)
                userId = e.getId();
        }
        userId++;
    }

    private void incrementFriendshipID(){
        for(Friendship e: friendshipRepository.findAll()){
            if(e.getId() > friendshipId)
                friendshipId = e.getId();
        }
        friendshipId++;
    }

    private void incrementFriendRequestID() {
        for (FriendRequest e : friendRequestRepository.findAll()) {
            if (e.getId() > friendRequestId)
                friendRequestId = e.getId();
        }
        friendRequestId++;
    }

    private void incrementMessageID(){
        for (Message m: messageRepository.findAll()){
            if (m.getId() > messageId)
                messageId = m.getId();
        }
        messageId++;
    }

    private Long getIDbyUsername(String username){
        for(User e:userRepository.findAll()){
            if(Objects.equals(e.getUsername(), username))
                return e.getId();
        }
        return null;
    }

    public Iterable<User> getAllUsers(){
        return userRepository.findAll();
    }

    public Iterable<Friendship> getAllFriendships(){
        return friendshipRepository.findAll();
    }

    public Iterable<FriendRequest> getAllFriendRequests(){
        return friendRequestRepository.findAll();
    }
    public Iterable<Message> getAllMessages(){
        return messageRepository.findAll();
    }

    public boolean login(String username){
        return getIDbyUsername(username) != null;
    }

    public User getUserByUsername(String username){
        return userRepository.findOne(getIDbyUsername(username));
    }

    public User getUserByID(Long id){ return userRepository.findOne(id); }

    public User addUser(String firstName, String secondName, String username){

        for(User e:userRepository.findAll()){
            if(Objects.equals(e.getUsername(), username))
                throw new DuplicateException("Username already taken!");
        }

        User user = new User(firstName, secondName, username);
        user.setId(userId);
        userId++;

        return userRepository.save(user);
    }

    public String stringReceivedMessage(Message mess)
    {
        return mess.getId() + "| " + mess.getMessage() + "| From: " +  userRepository.findOne(mess.getFrom()).getUsername();
    }

    public List<Message> allReceivedMessages(String username){
        List<Message> receivedMessages = new ArrayList<>();
        Long userId = this.getIDbyUsername(username);
        Iterable<Message> allMessages = messageRepository.findAll();
        for (Message m : allMessages)
        {
            for (Long searchedUser : m.getTo())
            {
                if (userId == searchedUser)
                {
                    receivedMessages.add(m);
                    break;
                }
            }
        }
        return receivedMessages;
    }

    public String stringSentMessage(Message mess)
    {
        String text = mess.getId() + "| " + mess.getMessage() + "| To: ";
        for (Long sentId : mess.getTo())
        {
            text +=  userRepository.findOne(sentId).getUsername() + "; ";
        }
        return text;
    }

    public List<Message> allSentMessages(String username){
        List<Message> sentMessages = new ArrayList<>();
        Long userId = this.getIDbyUsername(username);
        Iterable<Message> allMessages = messageRepository.findAll();
        for (Message m : allMessages)
        {
            if (m.getFrom() == userId)
            {
                sentMessages.add(m);
            }
        }
        return sentMessages;
    }

    public Message addMessage(Long idFrom, List<Long> to,String message,Long reply){
        Message mess = new Message(idFrom,message);
        mess.setTo(to);
        mess.setId(messageId);
        messageId++;
        if (reply!=null)
        {
            mess.setReply(reply);
        }
        return messageRepository.save(mess);
    }

    public Message deleteMessage(Long id)
    {
        if (messageRepository.findOne(id) == null)
            throw new EntityNotFoundException ("This message doesn't exist");
        Iterable<Message> allMessages = messageRepository.findAll();
        for (Message toUpdate: allMessages)
        {
            if (toUpdate.getReply() == id)
            {
                toUpdate.setReply(null);
                messageRepository.update(toUpdate);
            }
        }
        return messageRepository.delete(id);
    }

    public Message getMessageById (Long id)
    {
        return messageRepository.findOne(id);
    }

    public boolean isReceiver (Message mess, User entity)
    {
        for (Long receiver : mess.getTo())
        {
            if (entity.getId() == receiver)
                return true;
        }
        return false;
    }

    public List<Message> messagesBetween2Users (String username1, String username2)
    {
        Iterable<Message> allMessages = messageRepository.findAll();
        User user1 = this.getUserByUsername(username1);
        User user2 = this.getUserByUsername(username2);
        List<Message> conversations = new ArrayList<>();
        for (Message m: allMessages)
        {
            if ((user1.getId() == m.getFrom() && isReceiver(m,user2)) || (user2.getId() == m.getFrom() && isReceiver(m,user1) ) )
                conversations.add(m);
        }
        return conversations;
    }

    public List<Message> sortConversationsByDate (List<Message> conversations)
    {
        Collections.sort(conversations,new ComparatorByDate());
        return conversations;
    }

    private void removeMessages (String username)
    {
        Long id = getIDbyUsername(username);
        for (Message m : messageRepository.findAll())
        {
            if (m.getFrom() == id)
                this.deleteMessage(m.getId());
            else{
                List<Long> receiversUpdated = new ArrayList<>();
                for (Long receiver : m.getTo())
                {
                    if (receiver != id)
                        receiversUpdated.add(receiver);
                }
                m.setTo(receiversUpdated);
                messageRepository.update(m);
            }
        }
    }

    public User removeUser(String username){
        if(getIDbyUsername(username) != null) {
            removeAllFriends(username);
            removeMessages(username);
            return userRepository.delete(getIDbyUsername(username));
        }

        throw new EntityNotFoundException("Username Nonexistent");
    }

    private void removeAllFriends(String username){
        Long id = getIDbyUsername(username);
        User user = userRepository.findOne(id);
        for(Long friendsID:user.getFriends()){
            User friend = userRepository.findOne(friendsID);
            friend.removeFriend(user);
            userRepository.update(friend);
            removeFromFriendshipRepo(id, friendsID);
        }
    }

    private void removeFromFriendshipRepo(Long id1, Long id2){
        for(Friendship friendship:getAllFriendships()){
            if((Objects.equals(friendship.getID1(), id1) && friendship.getID2().equals(id2)) || (friendship.getID1().equals(id2) && friendship.getID2().equals(id1)))
                friendshipRepository.delete(friendship.getId());
        }
    }

    public Friendship addFriendship(String username1, String username2){
        Long id1 = getIDbyUsername(username1);
        Long id2 = getIDbyUsername(username2);

        if(id1 == null || id2 == null)
            throw new EntityNotFoundException("Nonexistent Usernames!");

        if(userRepository.findOne(id1).getFriends().contains(id2))
            throw new DuplicateException("Friendship already exists!");

        User user1 = userRepository.findOne(id1);
        User user2 = userRepository.findOne(id2);

        user1.addFriend(user2);
        user2.addFriend(user1);

        System.out.println(userRepository.findOne(id1));
        System.out.println(userRepository.findOne(id2));
        userRepository.update(user1);
        userRepository.update(user2);


        Friendship friendship = new Friendship(id1, id2);
        friendship.setId(friendshipId);
        friendshipId++;

        return friendshipRepository.save(friendship);
    }

    public Friendship removeFriendship(String username1, String username2){
        Long id1 = getIDbyUsername(username1);
        Long id2 = getIDbyUsername(username2);

        if(id1 == null || id2 == null)
            throw new EntityNotFoundException("Nonexistent Usernames!");

        if(!userRepository.findOne(id1).getFriends().contains(id2))
            throw new EntityNotFoundException("Nonexistent Friendship!");

        User user1 = userRepository.findOne(id1);
        User user2 = userRepository.findOne(id2);

        user1.removeFriend(user2);
        user2.removeFriend(user1);
        userRepository.update(user1);
        userRepository.update(user2);

        for(Friendship friendship:getAllFriendships()){
            if((Objects.equals(friendship.getID1(), id1) && friendship.getID2().equals(id2)) || (friendship.getID1().equals(id2) && friendship.getID2().equals(id1)))
                return friendshipRepository.delete(friendship.getId());
        }

        return null;
    }


    public User getUserById(Long id){
        return userRepository.findOne(id);
    }

    public FriendRequest sendFriendRequest(String sender, String receiver){
        Long id1 = getIDbyUsername(sender);
        Long id2 = getIDbyUsername(receiver);

        if(Objects.equals(id1, id2))
            throw new ValidationException("Invalid Username!");

        if(id1 == null || id2 == null)
            throw new EntityNotFoundException("Nonexistent Username!");

        if(userRepository.findOne(id1).getFriends().contains(id2))
            throw new DuplicateException("Friendship already exists!");

        for(FriendRequest friendRequest:friendRequestRepository.findAll()){
            if(friendRequest.getSenderID().equals(id1) && friendRequest.getReceiverID().equals(id2)
                    && friendRequest.getStatus().equals("Pending"))

                throw new DuplicateException("Friend Request already exists!");
        }

        FriendRequest friendRequest = new FriendRequest(id1, id2, "Pending");
        friendRequest.setId(friendRequestId);
        friendRequestId++;
        return friendRequestRepository.save(friendRequest);
    }

    public Iterable<FriendRequest> getSentFriendRequest(User user){
        Map<Long, FriendRequest> entities=new HashMap<Long, FriendRequest>();
        for(FriendRequest friendRequest:friendRequestRepository.findAll()){
            if(friendRequest.getSenderID().equals(user.getId()))
                entities.put(friendRequest.getId(), friendRequest);
        }
        return entities.values();
    }

    public Iterable<FriendRequest> getReceivedFriendRequest(User user){
        Map<Long, FriendRequest> entities=new HashMap<Long, FriendRequest>();
        for(FriendRequest friendRequest:friendRequestRepository.findAll()){
            if(friendRequest.getReceiverID().equals(user.getId()))
                entities.put(friendRequest.getId(), friendRequest);
        }
        return entities.values();
    }

    public FriendRequest answerRequest(String sender, String receiver, String answer) {
        Long id1 = getIDbyUsername(sender);
        Long id2 = getIDbyUsername(receiver);

        if (id1 == null || id2 == null)
            throw new EntityNotFoundException("Nonexistent Username!");

        for (FriendRequest friendRequest : getReceivedFriendRequest(getUserByUsername(receiver))) {
            if (friendRequest.getSenderID().equals(id1) && friendRequest.getReceiverID().equals(id2)
                    && friendRequest.getStatus().equals("Pending")) {
                if (answer.equals("Y")) {
                    friendRequest.setStatus("Approved");
                    addFriendship(sender, receiver);
                } else friendRequest.setStatus("Rejected");
                friendRequestRepository.update(friendRequest);
                return friendRequest;
            }
        }

        for (FriendRequest friendRequest : friendRequestRepository.findAll()) {
            if (friendRequest.getSenderID().equals(id1) && friendRequest.getReceiverID().equals(id2) &&
                    (friendRequest.getStatus().equals("Approved") || friendRequest.getStatus().equals("Rejected")))
                throw new DuplicateException("Friend Request already answered!");
        }

        throw new EntityNotFoundException("Nonexistent Friend Request!");
    }

    private Long findRequestIdBySenderReceiver(Long sender, Long receiver)
    {
        Iterable<FriendRequest> requests = friendRequestRepository.findAll();
        for (FriendRequest fr : requests)
            if (sender.equals(fr.getSenderID()) && receiver.equals(fr.getReceiverID()) && fr.getStatus().equals("Pending"))
                return fr.getId();
        return null;
    }

    public FriendRequest deleteFriendRequest(String sender, String receiver)
    {
        Long id1 = this.getIDbyUsername(sender);
        Long id2 = this.getIDbyUsername(receiver);
        Long idToDelete = this.findRequestIdBySenderReceiver(id1,id2);
        if (idToDelete != null)
            return friendRequestRepository.delete(idToDelete);
        throw new EntityNotFoundException("Nonexistent FriendRequest!");
    }

    public void getFriendshipsOfUserByMonth(String username, int monthNumber){
        if(getUserByUsername(username) == null)
            throw new EntityNotFoundException("Nonexistent User");
        if(monthNumber == 0)
            throw new IllegalArgumentException("Unknown Month");

        User user = getUserByUsername(username);

        List<Friendship> friendships = new ArrayList<>();
        getAllFriendships().forEach(friendships::add);

        friendships.stream().filter(x->
                        (x.getID1().equals(user.getId()) && x.getDate().getMonth().getValue() == monthNumber))
                .map(x->{
                    User friend = getUserByID(x.getID2());
                    return new FriendDTO(friend.getLastName(), friend.getFirstName(), x.getDate());
                })
                .forEach(x->{
                    System.out.println(x.getLastName() + " " + x.getFirstName() + " " + x.getDate());
                });

        friendships.stream().filter(x->
                        (x.getID2().equals(user.getId()) && x.getDate().getMonth().getValue() == monthNumber))
                .map(x->{
                    User friend = getUserByID(x.getID1());
                    return new FriendDTO(friend.getLastName(), friend.getFirstName(), x.getDate());
                })
                .forEach(x->{
                    System.out.println(x.getLastName() + " " + x.getFirstName() + " " + x.getDate());
                });
    }

    private boolean validID(Long nr){
        for(User user:userRepository.findAll()){
            if(nr.equals(user.getId()))
                return true;
        }
        return false;
    }

    private Graph createGraph(){
        Long maxID = -1L;
        for(User e: userRepository.findAll()){
            if(e.getId() > maxID)
                maxID = e.getId();
        }
        maxID++;
        Graph g = new Graph(Math.toIntExact(maxID));
        for(Friendship friendship:friendshipRepository.findAll()){
            g.addEdge(Math.toIntExact(friendship.getID1()), Math.toIntExact(friendship.getID2()));
        }
        return g;
    }

    public int numberOfCommunities(){

        Long maxID = -1L;
        for(User e: userRepository.findAll()){
            if(e.getId() > maxID)
                maxID = e.getId();
        }
        maxID++;

        Graph g = createGraph();
        int count = g.connectedComponents();

        for(int i = 0; i < maxID; i++){
            if(!validID((long) i))
                count--;
        }
        return count;

    }
    /*
    Eroare in cazul in care avem doua comunitati de o singura persoana
    */
    public int mostSocialCommunity(){
        Graph g = createGraph();
        for(int i:g.maxConnectedComponents()){
            System.out.print(userRepository.findOne((long) i).getUsername() + " ");
        }
        return g.maxConnectedComponents().size();

    }
}

