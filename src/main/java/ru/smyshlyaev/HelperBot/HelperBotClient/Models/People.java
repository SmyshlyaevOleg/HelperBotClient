package ru.smyshlyaev.HelperBot.HelperBotClient.Models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "people")
public class People {

    @Id
    @NotNull
    @Column(name = "telegram_id")
    private long telegramId;

    @NotNull
    @Column(name = "full_name")
    private String fullName;

    @NotNull
    @Column(name = "user_name")
    private String userName;

    @OneToMany(mappedBy = "senderMessage")
    private List<Message> messages;


    public People(long telegramId, String fullName, String userName) {
        this.telegramId = telegramId;
        this.fullName = fullName;
        this.userName = userName;
    }

    public People() {

    }

    public People(long telegramId) {
        this.telegramId = telegramId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public long getTelegramId() {
        return telegramId;
    }

    public void setTelegramId(int telegramId) {
        this.telegramId = telegramId;
    }

    @Override
    public String toString() {
        return "People{" +
                ", fullName='" + fullName + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
