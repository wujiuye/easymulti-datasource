package com.github.wujiuye.r2dbc.person;

import com.github.wujiuye.r2dbc.annotation.R2dbcDataBase;
import com.github.wujiuye.r2dbc.mode.ms.MasterSlaveMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

@Service
public class PersonService {

    @Resource
    private PersonRepository personRepository;

    @R2dbcDataBase(MasterSlaveMode.Master)
    @Transactional(rollbackFor = Throwable.class)
    public Mono<Integer> addPerson(Person... persons) {
        Mono<Integer> txOp = null;
        for (Person person : persons) {
            if (txOp == null) {
                txOp = personRepository.insertPerson(person.getId(), person.getName(), person.getAge());
            } else {
                txOp = txOp.then(personRepository.insertPerson(person.getId(), person.getName(), person.getAge()));
            }
        }
        return txOp;
    }

    @R2dbcDataBase(MasterSlaveMode.Master)
    @Transactional(rollbackFor = Throwable.class)
    public Flux<Integer> addPersons(Flux<Person> persons) {
        return persons.flatMap(person -> personRepository.insertPerson(person.getId(), person.getName(), person.getAge()));
    }

}
