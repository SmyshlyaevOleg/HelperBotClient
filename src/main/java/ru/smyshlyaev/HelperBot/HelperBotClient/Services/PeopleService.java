package ru.smyshlyaev.HelperBot.HelperBotClient.Services;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.smyshlyaev.HelperBot.HelperBotClient.Models.Message;
import ru.smyshlyaev.HelperBot.HelperBotClient.Models.People;
import ru.smyshlyaev.HelperBot.HelperBotClient.Repositories.PeopleRepository;
import ru.smyshlyaev.HelperBot.HelperBotClient.Utils.MessageSenderException;
import ru.smyshlyaev.HelperBot.HelperBotClient.Utils.RegistrationException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class PeopleService {
    private final PeopleRepository peopleRepository;

    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    @Transactional
    public void saveUser(long telegramId,String fullName,String userName) throws RegistrationException {

        Optional<People> person= Optional.ofNullable(peopleRepository.findPeopleByTelegramId(telegramId));

        if(person.isEmpty()) {
            People user = new People(telegramId,fullName,userName);
            peopleRepository.save(user);
        }
        else if(person.isPresent()) {
           throw new RegistrationException("Пользователь уже существует в базе данных");
        }

    }
    @Transactional
    public Optional<People> findOneUser(long telegramId) throws MessageSenderException {
        Optional<People> finderPeople= Optional.of(new People());
        Optional<People> person= Optional.ofNullable(peopleRepository.findPeopleByTelegramId(telegramId));

        if(person.isPresent()) {
            finderPeople=person;
        }
        else if(person.isEmpty()) {
            throw new MessageSenderException("Пользователь не найден");
        }
        Hibernate.initialize(finderPeople.get());
        return finderPeople;
    }

    @Transactional
    public Optional<People> getPeopleByFullName(String fullName) {
        peopleRepository.findPeopleByFullName(fullName);
        return Optional.ofNullable(peopleRepository.findPeopleByFullName(fullName));
    }

@Transactional
    public List<Message> findMessageTelegramId(long telegramId) {
        Optional<People> person= Optional.ofNullable(peopleRepository.findPeopleByTelegramId(telegramId));

        if (person.isPresent()) {
            Hibernate.initialize(person.get().getMessages());
            return person.get().getMessages();
        }

        else {
            return Collections.emptyList();
        }
    }


}

