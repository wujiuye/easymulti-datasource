package com.github.wujiuye.r2dbc;

import com.github.wujiuye.r2dbc.person.PersonService;
import com.github.wujiuye.r2dbc.person.Person;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import reactor.core.publisher.Flux;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
@EnableR2dbcRepositories
public class R2dbcApplication {

    private static void testMono(PersonService personService, Person person) {
        personService.addPerson(person, person)
                .doOnError(Throwable::printStackTrace)
                .subscribe(System.out::println);
    }

    private static void testFlux(PersonService personService, Person person) {
        personService.addPersons(Flux.just(person, person))
                .doOnError(Throwable::printStackTrace)
                .subscribe(System.out::println);
    }

    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext context = SpringApplication.run(R2dbcApplication.class);
        Person person = new Person();
        PersonService personService = context.getBean(PersonService.class);
        person.setId("12348");
        person.setName("wjy");
        person.setAge(25);
        testFlux(personService, person);
        TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
    }

}
