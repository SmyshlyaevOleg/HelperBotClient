package ru.smyshlyaev.HelperBot.HelperBotClient.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.smyshlyaev.HelperBot.HelperBotClient.Models.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message,Long> {


}
