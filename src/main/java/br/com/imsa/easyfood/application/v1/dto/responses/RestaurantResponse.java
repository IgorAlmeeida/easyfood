package br.com.imsa.easyfood.application.v1.dto.responses;

import br.com.imsa.easyfood.domain.enums.KichenTypeEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "RestaurantResponse", description = "Restaurant API response")
public class RestaurantResponse {
    @Schema(description = "Restaurant identifier", example = "5")
    private Long id;

    @Schema(description = "Restaurant name", example = "Sabor & Arte")
    private String name;

    @Schema(description = "Restaurant address")
    private AddressResponse address;

    @Schema(description = "Kitchen type", example = "JAPANESE")
    private KichenTypeEnum kitchenType;

    @Schema(description = "Start time of operation (HH:mm)", example = "11:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "UTC")
    private LocalDateTime startOperationTime;

    @Schema(description = "End time of operation (HH:mm)", example = "23:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "UTC")
    private LocalDateTime endOperationTime;

    @Schema(description = "Restaurant proprietary user")
    private UserSystemResponse proprietary;
}