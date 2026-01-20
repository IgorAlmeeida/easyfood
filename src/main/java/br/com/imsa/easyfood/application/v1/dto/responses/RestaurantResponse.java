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
    private Long id;

    private String name;

    private AddressResponse address;

    private KichenTypeEnum kitchenType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "UTC")
    private LocalDateTime startOperationTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "UTC")
    private LocalDateTime endOperationTime;

    private UserSystemResponse proprietary;
}