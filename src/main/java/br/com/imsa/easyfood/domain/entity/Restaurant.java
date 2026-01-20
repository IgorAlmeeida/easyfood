package br.com.imsa.easyfood.domain.entity;

import br.com.imsa.easyfood.domain.enums.KichenTypeEnum;
import br.com.imsa.easyfood.domain.exception.NegocioException;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Restaurant {

    private Long id;
    private String name;
    private Address address;
    private KichenTypeEnum kitchenType;
    private LocalDateTime startOperationTime;
    private LocalDateTime endOperationTime;
    private UserSystem proprietary;

    public Restaurant(Long id,
                      String name,
                      Address address,
                      KichenTypeEnum kitchenType,
                      LocalDateTime startOperationTime,
                      LocalDateTime endOperationTime,
                      UserSystem proprietary) {
        validate(name, kitchenType, startOperationTime, endOperationTime);
        this.id = id;
        this.name = name;
        this.address = address; // address can be null here; relationship optional
        this.kitchenType = kitchenType;
        this.startOperationTime = startOperationTime;
        this.endOperationTime = endOperationTime;
        this.proprietary = proprietary; // optional
    }

    public Restaurant(String name,
                      Address address,
                      KichenTypeEnum kitchenType,
                      LocalDateTime startOperationTime,
                      LocalDateTime endOperationTime,
                      UserSystem proprietary) {
        validate(name, kitchenType, startOperationTime, endOperationTime);
        this.name = name;
        this.address = address;
        this.kitchenType = kitchenType;
        this.startOperationTime = startOperationTime;
        this.endOperationTime = endOperationTime;
        this.proprietary = proprietary;
    }

    private void validate(String name,
                          KichenTypeEnum kitchenType,
                          LocalDateTime startOperationTime,
                          LocalDateTime endOperationTime) {
        if (name == null || name.isBlank()) {
            throw new NegocioException("O nome do restaurante é obrigatório.");
        }
        if (kitchenType == null) {
            throw new NegocioException("O tipo de cozinha é obrigatório.");
        }
        if (startOperationTime == null) {
            throw new NegocioException("O horário de abertura é obrigatório.");
        }
        if (endOperationTime == null) {
            throw new NegocioException("O horário de fechamento é obrigatório.");
        }
        if (endOperationTime.isBefore(startOperationTime)) {
            throw new NegocioException("O horário de fechamento deve ser após o horário de abertura.");
        }
    }
}
