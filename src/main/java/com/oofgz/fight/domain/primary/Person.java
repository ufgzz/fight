package com.oofgz.fight.domain.primary;

import lombok.Data;
import org.springframework.data.domain.Persistable;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;
import org.springframework.ldap.odm.annotations.Transient;

import javax.naming.Name;

/**
 *  LDAP简称对应
 *      o：organization（组织-公司）
 *      ou：organization unit（组织单元-部门）
 *      c：countryName（国家）
 *      dc：domainComponent（域名）
 *      sn：surname（姓氏）
 *      cn：common name（常用名称）
 */
@Entry(base = "dc=maxcrc,dc=com", objectClasses = {"top", "organizationalUnit"})
@Data
public class Person implements Persistable<Name> {

    //不需要序列化的属性
    @Transient
    private boolean isNew = false;

    @Id
    private Name id;

    @Attribute(name = "description")
    private String description;

    @Override
    public boolean isNew() {
        return isNew;
    }
}
