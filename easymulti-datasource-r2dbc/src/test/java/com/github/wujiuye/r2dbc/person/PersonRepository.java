package com.github.wujiuye.r2dbc.person;

import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface PersonRepository extends R2dbcRepository<Person, String> {

    @Modifying
    @Query("insert into person (id,name,age) values(:id,:name,:age)")
    Mono<Integer> insertPerson(String id, String name, Integer age);

}
