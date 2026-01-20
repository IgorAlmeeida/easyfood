package br.com.imsa.easyfood.domain.entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Restaurant {

    private Long id;

    private String name;

    private Address address;

    private String kitchenType;

    private LocalDateTime startOperationTime;

    private LocalDateTime endOperationTime;

    private UserSystem proprietary;
}
