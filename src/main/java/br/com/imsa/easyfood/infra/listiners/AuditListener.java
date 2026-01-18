package br.com.imsa.easyfood.infra.listiners;

import br.com.imsa.easyfood.infra.model.RevisionEntityCustom;
import br.com.imsa.easyfood.infra.model.UserSystemJpaEntity;
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
            UserSystemJpaEntity userSystemJpaEntity = (UserSystemJpaEntity) auth.getPrincipal();
            rev.setUserSystem(userSystemJpaEntity.getId());
        }
    }
}