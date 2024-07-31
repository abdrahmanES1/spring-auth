package com.esab.springauth.config;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.esab.springauth.SpringAuthApplication;
import com.esab.springauth.annotations.SecuredEntity;
import com.esab.springauth.entities.Privilege;
import com.esab.springauth.repositories.PrivilegeRepository;

import jakarta.annotation.PostConstruct;

@Component
public class PrivilegeGenerator {

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @PostConstruct
    public void generatePrivileges() {

        String base = SpringAuthApplication.class.getPackageName();
        Reflections reflections = new Reflections(base);
        Set<Class<?>> annotatedClasses = reflections
                .getTypesAnnotatedWith((Class<? extends Annotation>) SecuredEntity.class);

        for (Class<?> annotatedClass : annotatedClasses) {
            SecuredEntity securedEntity = annotatedClass.getAnnotation(SecuredEntity.class);
            String entityName = securedEntity.name();
            generateEntityPrivileges(entityName);
        }
    }

    private void generateEntityPrivileges(String entityName) {
        List<String> actions = List.of("view", "create", "update", "delete");

        for (String action : actions) {
            String privilegeName = action + ":" + entityName;
            if (!privilegeRepository.existsByAuthority(privilegeName)) {
                Privilege privilege = new Privilege();
                privilege.setAuthority(privilegeName);
                privilegeRepository.save(privilege);
            }
        }
    }
}
