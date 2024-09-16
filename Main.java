import java.util.*;
import java.io.*;
import java.net.*;

abstract class CommunicationAdapter {
    public abstract void sendMessage(String message) throws IOException;
    public abstract String receiveMessage() throws IOException;
}

class HTTPAdapter extends CommunicationAdapter {
    private String url;

    public HTTPAdapter(String url) {
        this.url = url;
    }

    @Override
    public void sendMessage(String message) throws IOException {
        URL urlObj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        out.writeBytes("message=" + message);
        out.flush();
        out.close();
        connection.getResponseCode();
    }

    @Override
    public String receiveMessage() throws IOException {
        URL urlObj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
        connection.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        return content.toString();
    }
}

class ChatroomMain {
    private String roomId;
    private List<User> data;
    private List<String> messages;

    public ChatroomMain(String roomId) {
        this.roomId = roomId;
        this.data = new ArrayList<>();
        this.messages = new ArrayList<>();
    }

    public void addUser(User user) {
        data.add(user);
        user.joinRoom(this);
        notifyUsers(user.getUsername() + " has joined the room.");
    }

    public void removeUser(User user) {
        data.remove(user);
        user.leaveRoom(this);
        notifyUsers(user.getUsername() + " has left the room.");
    }

    public void notifyUsers(String message) {
        for (User user : data) {
            user.update(message);
        }
    }

    public void addMessage(String message) {
        messages.add(message);
        notifyUsers(message);
    }

    public List<String> listActiveUsers() {
        List<String> activeUsers = new ArrayList<>();
        for (User user : data) {
            activeUsers.add(user.getUsername());
        }
        return activeUsers;
    }

    public void displayMessages() {
        for (String message : messages) {
            System.out.println(message);
        }
    }

    public String getRoomId() {
        return roomId;
    }
}

class User {
    private String username;
    private List<ChatroomMain> rooms;

    public User(String username) {
        this.username = username;
        this.rooms = new ArrayList<>();
    }

    public void joinRoom(ChatroomMain room) {
        rooms.add(room);
        System.out.println(username + " joined " + room.getRoomId());
    }

    public void leaveRoom(ChatroomMain room) {
        rooms.remove(room);
        System.out.println(username + " left " + room.getRoomId());
    }

    public void update(String message) {
        System.out.println(username + " received message: " + message);
    }

    public String getUsername() {
        return username;
    }
}

class ChatRoom {
    private static ChatRoom instance = null;
    private Map<String, ChatroomMain> rooms;

    private ChatRoom() {
        rooms = new HashMap<>();
    }

    public static ChatRoom getInstance() {
        if (instance == null) {
            instance = new ChatRoom();
        }
        return instance;
    }

    public void createRoom(String roomId) {
        if (!rooms.containsKey(roomId)) {
            rooms.put(roomId, new ChatroomMain(roomId));
            System.out.println("Chatroom " + roomId + " has been created.");
        } else {
            System.out.println("Chatroom " + roomId + " already exists.");
        }
    }

    public ChatroomMain getRoom(String roomId) {
        return rooms.get(roomId);
    }
}

public class Main {
    public static void main(String[] args) throws IOException {
        ChatRoom manager = ChatRoom.getInstance();
        HTTPAdapter httpAdapter = new HTTPAdapter("http://example.com/api");

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n1. create room\n2. join room\n3. send message\n4. list active users\n5. display messages\n6. exit");
            System.out.print("enter your choice: ");
            String choice = scanner.nextLine();

            if (choice.equals("1")) {
                System.out.print("enter room id: ");
                String roomId = scanner.nextLine();
                manager.createRoom(roomId);

            } else if (choice.equals("2")) {
                System.out.print("enter room id: ");
                String roomId = scanner.nextLine();
                System.out.print("enter your username: ");
                String username = scanner.nextLine();
                ChatroomMain room = manager.getRoom(roomId);
                if (room != null) {
                    User user = new User(username);
                    room.addUser(user);
                } else {
                    System.out.println("room not found.");
                }

            } else if (choice.equals("3")) {
                System.out.print("enter room id: ");
                String roomId = scanner.nextLine();
                System.out.print("enter message: ");
                String message = scanner.nextLine();
                ChatroomMain room = manager.getRoom(roomId);
                if (room != null) {
                    room.addMessage(message);
                } else {
                    System.out.println("room not found.");
                }

            } else if (choice.equals("4")) {
                System.out.print("enter room id: ");
                String roomId = scanner.nextLine();
                ChatroomMain room = manager.getRoom(roomId);
                if (room != null) {
                    List<String> activeUsers = room.listActiveUsers();
                    System.out.println("active users: " + String.join(", ", activeUsers));
                } else {
                    System.out.println("room not found.");
                }

            } else if (choice.equals("5")) {
                System.out.print("enter room id: ");
                String roomId = scanner.nextLine();
                ChatroomMain room = manager.getRoom(roomId);
                if (room != null) {
                    room.displayMessages();
                } else {
                    System.out.println("room not found.");
                }

            } else if (choice.equals("6")) {
                break;

            } else {
                System.out.println("invalid choice.");
            }
        }

        scanner.close();
    }
}