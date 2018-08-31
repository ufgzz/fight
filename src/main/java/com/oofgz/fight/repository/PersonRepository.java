package com.oofgz.fight.repository;

import com.oofgz.fight.bean.Person;
import org.springframework.data.repository.CrudRepository;

import javax.naming.Name;

public interface PersonRepository extends CrudRepository<Person, Name> {

}
