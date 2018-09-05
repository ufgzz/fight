package com.oofgz.fight.repository.primary;

import com.oofgz.fight.domain.primary.Person;
import org.springframework.data.repository.CrudRepository;

import javax.naming.Name;

public interface PersonRepository extends CrudRepository<Person, Name> {

}
