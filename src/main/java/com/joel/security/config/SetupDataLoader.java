package com.joel.security.config;

import com.joel.security.model.Role;
import com.joel.security.model.RoleType;
import com.joel.security.repository.RoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private final RoleRepository roleRepository;
    private boolean alreadySetup = false;

    @Autowired
    public SetupDataLoader(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if(alreadySetup) return;

        if(this.roleRepository.findByAuthority("ADMINISTRATOR").isPresent() || this.roleRepository.findByAuthority("SHOPPER").isPresent()) return;

        Role adminRole = new Role();
        adminRole.setAuthority(RoleType.ADMINISTRATOR.name());

        Role shopperRole = new Role();
        shopperRole.setAuthority(RoleType.SHOPPER.name());

        this.roleRepository.save(adminRole);
        this.roleRepository.save(shopperRole);

        this.alreadySetup = true;

    }
}
