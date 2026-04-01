package network;

public class Message {
    private MessageType type;
    private String username;
    private String password;
    private String message;

    public Message() {
    }

    public Message(MessageType type, String message) {
        this.type = type;
        this.message = message;
    }

    public Message(MessageType type, String username, String password) {
        this.type = type;
        this.username = username;
        this.password = password;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}