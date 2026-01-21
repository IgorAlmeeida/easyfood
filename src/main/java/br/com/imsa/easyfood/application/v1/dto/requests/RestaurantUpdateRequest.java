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

    @Schema(description = "Restaurant name", example = "Sabor & Arte")
    private String name;

    @Schema(description = "Restaurant address")
    @NotNull(message = "{validation.user.address.required}")
    private AddressRequest address;

    @Schema(description = "Kitchen type", example = "ITALIAN")
    private KichenTypeEnum kitchenType;

    @Schema(description = "Start time of operation (HH:mm)", example = "10:00")
    private LocalDateTime startOperationTime;

    @Schema(description = "End time of operation (HH:mm)", example = "22:00")
    private LocalDateTime endOperationTime;

    @Schema(description = "UserSystem (proprietary) identifier", example = "1")
    private Long proprietaryId;
}
