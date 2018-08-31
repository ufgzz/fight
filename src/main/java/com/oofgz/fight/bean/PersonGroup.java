package com.oofgz.fight.bean;

import lombok.Data;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.DnAttribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import javax.naming.Name;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entry(base = "ou=Groups", objectClasses = {"top", "groupOfUniqueNames"})
@Data
public class PersonGroup implements Serializable {

    public static final String BASE = "ou=Groups";

    @Id
    private Name dn;

    @Attribute(name = "cn")
    @DnAttribute(value = "cn", index = 1)
    private String name;

    @Attribute(name = "uniqueMember")
    private Set<Name> members = new HashSet<>();

    @Attribute(name="description")
    private String description;
}
