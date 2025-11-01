package br.com.imsa.easyfood.domain.listiners;

import br.com.imsa.easyfood.domain.entity.RevisionEntityCustom;
import br.com.imsa.easyfood.domain.entity.UserSystem;
import org.hibernate.envers.RevisionListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuditListener implements RevisionListener {

    @Override
    public void newRevision(Object revisionEntity) {
        RevisionEntityCustom rev = (RevisionEntityCustom) revisionEntity;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getPrincipal() instanceof String) {
            rev.setUserSystem(null);
        } else {
            UserSystem userSystem = (UserSystem) auth.getPrincipal();
            rev.setUserSystem(userSystem.getId());
        }
    }
}