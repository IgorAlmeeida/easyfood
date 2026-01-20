package br.com.imsa.easyfood.infra.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "restaurant", schema = "easyfood")
@Audited
@AuditTable(value = "restaurant_audit", schema = "audit_easyfood")
public class RestaurantJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private AddressJpaEntity address;

    @Column(name = "kitchen_type")
    private String kitchenType;

    @Column(name = "start_operation_time")
    private LocalDateTime startOperationTime;

    @Column(name = "end_operation_time")
    private LocalDateTime endOperationTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "proprietary_id")
    private UserSystemJpaEntity proprietary;
}
