package br.com.imsa.easyfood.application.v1.dto.responses;

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
    private String kitchenType;
    private LocalDateTime startOperationTime;
    private LocalDateTime endOperationTime;
    private UserSystemResponse proprietary;
}