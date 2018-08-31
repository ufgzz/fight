package com.oofgz.fight;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.ldap.repository.config.EnableLdapRepositories;

@EnableLdapRepositories
@SpringBootApplication
public class FightApplication {

	public static void main(String[] args) {
		SpringApplication.run(FightApplication.class, args);
	}
}
