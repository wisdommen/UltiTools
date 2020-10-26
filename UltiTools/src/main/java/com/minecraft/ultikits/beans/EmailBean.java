package com.minecraft.ultikits.beans;

import java.io.Serializable;

public class EmailBean implements Serializable {
    private final String emailAddress;
    private final String title;
    private final String content;

    public EmailBean(String emailAddress, String title, String content){
        this.emailAddress = emailAddress;
        this.title = title;
        this.content = content;
    }


    public String getEmailAddress() {
        return emailAddress;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
