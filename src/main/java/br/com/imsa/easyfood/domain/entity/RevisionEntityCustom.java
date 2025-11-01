package br.com.imsa.easyfood.domain.entity;

import br.com.imsa.easyfood.domain.listiners.AuditListener;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RevisionEntity(AuditListener.class)
@Entity
@Table(name = "revision_entity", schema = "easyfood_ava")
public class RevisionEntityCustom {

    @Id
    @RevisionNumber
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @RevisionTimestamp
    private Long timestamp;

    private Long userSystem;

}
