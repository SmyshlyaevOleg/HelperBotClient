package ru.smyshlyaev.HelperBot.HelperBotClient.Models;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private long id;

    @Column(name = "message")
    @NotNull
    private String message;

    @DateTimeFormat
    @Column(name="created_at")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "sender_id",referencedColumnName = "telegram_id")
    private People senderMessage;

    @NotNull
    @Column(name = "sender_name")
    private String senderName;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @NotNull
    @Column(name = "status")
    private String status;

    public Message() {

    }

    public Message(String message, LocalDateTime createdAt, People senderMessage, String status) {
        this.message = message;
        this.createdAt = createdAt;
        this.senderMessage = senderMessage;
        this.status=status;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public People getSenderMessage() {
        return senderMessage;
    }

    public void setSenderMessage(People senderMessage) {
        this.senderMessage = senderMessage;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    @Override
    public String toString() {
        return "Message{" +
                "message='" + message + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message1 = (Message) o;
        return id == message1.id && Objects.equals(message, message1.message) && Objects.equals(createdAt, message1.createdAt) && Objects.equals(senderMessage, message1.senderMessage) && Objects.equals(senderName, message1.senderName) && Objects.equals(status, message1.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, message, createdAt, senderMessage, senderName, status);
    }
}
