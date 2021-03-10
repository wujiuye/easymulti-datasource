package com.github.wujiuye.r2dbc.person;

/**
 * CREATE TABLE "person" (
 * "id" varchar(255) NOT NULL,
 * "name" varchar(255) DEFAULT NULL,
 * "age" int(11) DEFAULT NULL,
 * PRIMARY KEY ("id")
 * ) ENGINE=InnoDB DEFAULT CHARSET=utf8
 */
public class Person {
    private String id;
    private String name;
    private int age;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
