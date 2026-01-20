package br.com.imsa.easyfood.infra.model;


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

    //falta enum type Diponibildiade

    @Column(name = "image_location")
    private String image;
}
