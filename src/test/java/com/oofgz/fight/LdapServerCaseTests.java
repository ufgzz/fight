package com.oofgz.fight;

import com.oofgz.fight.dto.ldap.LdapPerson;
import com.oofgz.fight.repository.primary.LdapPersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.naming.Name;
import java.util.function.Consumer;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class LdapServerCaseTests {

    @Autowired
    private LdapPersonRepository ldapPersonRepository;

    @Before
    public void setUp() {
        log.info("测试LDAP服务的功能");
    }


    @Test
    public void findAllLdap() {
        ldapPersonRepository.findAll().forEach(new Consumer<LdapPerson>() {
            @Override
            public void accept(LdapPerson ldapPerson) {
                System.out.println(ldapPerson);
            }
        });
    }

    @Test
    public void saveLdap() {
        Name dn = LdapNameBuilder.newInstance()
                .add("ou", "Mini")
                .build();
        LdapPerson ldapPerson = new LdapPerson();
        ldapPerson.setId(dn);
        ldapPerson.setDescription("说明情况,,,修改后的结果+++++PPPPP");
        ldapPerson.setNew(!ldapPersonRepository.existsById(dn));
        ldapPersonRepository.save(ldapPerson);
        ldapPersonRepository.findAll().forEach(new Consumer<LdapPerson>() {
            @Override
            public void accept(LdapPerson person) {
                System.out.println(person);
            }
        });
    }



}
