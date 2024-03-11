package ru.smyshlyaev.HelperBot.HelperBotClient.Repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.smyshlyaev.HelperBot.HelperBotClient.Models.People;

@Repository
public interface PeopleRepository extends JpaRepository<People,Long> {

     People findPeopleByTelegramId(long userId);

     People findPeopleByFullName(String fullName);
}

