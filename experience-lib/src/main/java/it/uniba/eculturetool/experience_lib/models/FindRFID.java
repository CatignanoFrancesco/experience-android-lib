package it.uniba.eculturetool.experience_lib.models;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FindRFID extends Experience {
    private UUID rfidId;
    private Map<String, String> messages = new HashMap<>();

    public FindRFID(String id, Difficulty difficulty, int points) {
        super(id, difficulty, points);
    }

    public FindRFID() {
        super();
    }

    public UUID getRfidId() {
        return rfidId;
    }

    public void setRfidId(UUID rfidId) {
        this.rfidId = rfidId;
    }

    public Map<String, String> getMessages() {
        return messages;
    }

    public void setMessages(Map<String, String> messages) {
        this.messages = messages;
    }
}
