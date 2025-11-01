package br.com.imsa.easyfood.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public abstract class Auditable {

    @CreatedDate
    @Column(name = "dh_create_at")
    private LocalDateTime createAt;

    @LastModifiedDate
    @Column(name = "dh_update_at")
    private LocalDateTime updateAt;


}
