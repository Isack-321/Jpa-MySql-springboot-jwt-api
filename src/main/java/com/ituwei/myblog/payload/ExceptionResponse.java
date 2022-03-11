package com.ituwei.myblog.payload;

import lombok.Data;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
public class ExceptionResponse {
    private String error;
    private Integer status;
    private List<String> messages;
    private Instant timestamp;

    public ExceptionResponse(String error, Integer status, List<String> messages, Instant timestamp) {
        this.error = error;
        this.status = status;
        this.messages = messages;
        this.timestamp = timestamp;
    }

    public List<String> getMessages() {
        return messages == null?null:new ArrayList<>(messages);
    }

    public void setMessages(List<String> messages) {
        if (messages==null){
        this.messages = null;}
        else{
            this.messages = Collections.unmodifiableList(messages);
        }
    }
}