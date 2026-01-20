package br.com.imsa.easyfood.application.v1.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
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
@Schema(name = "RestaurantCreateRequest", description = "Request payload to create a restaurant")
public class RestaurantCreateRequest {

    @NotBlank
    private String name;

    @Schema(description = "Address identifier to associate", example = "1")
    private Long addressId;

    @NotBlank
    private String kitchenType;

    @NotNull
    private LocalDateTime startOperationTime;

    @NotNull
    private LocalDateTime endOperationTime;

    @Schema(description = "UserSystem (proprietary) identifier", example = "1")
    private Long proprietaryId;
}
