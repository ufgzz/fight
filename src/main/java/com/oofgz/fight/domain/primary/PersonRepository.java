package com.oofgz.fight.domain.primary;

import org.springframework.data.repository.CrudRepository;

import javax.naming.Name;

public interface PersonRepository extends CrudRepository<Person, Name> {

}
