package exceptions;

import java.util.ArrayList;
import java.util.List;

public class MessageError {
    private List<String> messages = new ArrayList<>();

    public void addMessage(String message){
        messages.add(message);
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }
}
