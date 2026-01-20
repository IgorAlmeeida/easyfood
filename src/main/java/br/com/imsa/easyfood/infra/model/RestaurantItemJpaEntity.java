package br.com.imsa.easyfood.infra.model;


import br.com.imsa.easyfood.domain.enums.AvailabilityEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "restaurant_item", schema = "easyfood")
@Audited
@AuditTable(value = "restaurant_item_audit", schema = "audit_easyfood")
public class RestaurantItemJpaEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Double price;

    @Column(name = "availability")
    private AvailabilityEnum availability;

    @Column(name = "image_location")
    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private RestaurantJpaEntity restaurant;
}
