package com.oofgz.fight.bean;

import lombok.Data;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.DnAttribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import javax.naming.Name;
import java.io.Serializable;

/**
 *  LDAP简称对应
 *      o：organization（组织-公司）
 *      ou：organization unit（组织单元-部门）
 *      c：countryName（国家）
 *      dc：domainComponent（域名）
 *      sn：surname（姓氏）
 *      cn：common name（常用名称）
 */
@Entry(base = "ou=person,dc=example,dc=com", objectClasses = {"inetOrgPerson"})
@Data
public class Person implements Serializable {

    @Id
    private Name dn;

    @DnAttribute(value = "uid", index = 3)
    private String uid;

    @Attribute(name = "cn")
    private String commonName;

    @Attribute(name = "su")
    private String surName;

    @Attribute(name = "userPassword")
    private String userPassword;

}
