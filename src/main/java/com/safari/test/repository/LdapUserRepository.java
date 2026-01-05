//package com.safari.test.repository;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.ldap.core.LdapTemplate;
//import org.springframework.ldap.query.LdapQuery;
//import org.springframework.ldap.query.LdapQueryBuilder;
//import org.springframework.ldap.core.AttributesMapper;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public class LdapUserRepository {
//
//    @Autowired
//    private LdapTemplate ldapTemplate;
//
//    public List<String> getAllUsers() {
//        LdapQuery query = LdapQueryBuilder.query()
//                .where("objectClass").is("user");
//
//        return ldapTemplate.search(query, (AttributesMapper<String>) attrs ->
//                (String) attrs.get("cn").get());
//    }
//}
//
