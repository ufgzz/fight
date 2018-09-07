package com.oofgz.fight.repository.primary;

import com.oofgz.fight.domain.primary.LdapPerson;
import org.springframework.data.repository.CrudRepository;

import javax.naming.Name;

public interface LdapPersonRepository extends CrudRepository<LdapPerson, Name> {

}
