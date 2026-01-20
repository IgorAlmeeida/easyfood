package br.com.imsa.easyfood.application.v1.dto.requests;

import br.com.imsa.easyfood.domain.enums.KichenTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "RestaurantUpdateRequest", description = "Request payload to update a restaurant")
public class RestaurantUpdateRequest {

    private String name;

    @Schema(description = "Address Restaurant")
    @NotNull(message = "{validation.user.address.required}")
    private AddressRequest address;

    private KichenTypeEnum kitchenType;

    private LocalDateTime startOperationTime;

    private LocalDateTime endOperationTime;

    private Long proprietaryId;
}
