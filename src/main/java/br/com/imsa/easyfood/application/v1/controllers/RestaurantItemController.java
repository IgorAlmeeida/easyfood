package br.com.imsa.easyfood.application.v1.controllers;

import br.com.imsa.easyfood.application.v1.dto.PageableDto;
import br.com.imsa.easyfood.application.v1.dto.requests.RestaurantItemCreateRequest;
import br.com.imsa.easyfood.application.v1.dto.requests.RestaurantItemUpdateRequest;
import br.com.imsa.easyfood.application.v1.dto.responses.PageResponse;
import br.com.imsa.easyfood.application.v1.dto.responses.RestaurantItemResponse;
import br.com.imsa.easyfood.application.v1.mappers.RestaurantItemMapperApp;
import br.com.imsa.easyfood.domain.dto.input.restaurantitem.CreateRestaurantItemInput;
import br.com.imsa.easyfood.domain.dto.input.restaurantitem.UpdateRestaurantItemInput;
import br.com.imsa.easyfood.domain.entity.RestaurantItem;
import br.com.imsa.easyfood.domain.usercase.restauranteitem.CreateRestaurantItemUseCase;
import br.com.imsa.easyfood.domain.usercase.restauranteitem.DeleteRestaurantItemUseCase;
import br.com.imsa.easyfood.domain.usercase.restauranteitem.SearchRestaurantItemUseCase;
import br.com.imsa.easyfood.domain.usercase.restauranteitem.UpdateRestaurantItemUseCase;
import br.com.imsa.easyfood.infra.adpter.RestaurantEntityRespository;
import br.com.imsa.easyfood.infra.exception.ErrorResponse;
import br.com.imsa.easyfood.infra.adpter.RestaurantItemEntityRepository;
import br.com.imsa.easyfood.infra.mappers.RestaurantItemMapper;
import br.com.imsa.easyfood.infra.mappers.RestaurantMapper;
import br.com.imsa.easyfood.infra.repository.RestaurantItemRepository;
import br.com.imsa.easyfood.infra.repository.RestaurantRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/restaurant-item/v1", produces = "application/json; charset=utf-8")
@RequiredArgsConstructor
@Tag(name = "Restaurant Item", description = "CRUD operations for restaurant items")
public class RestaurantItemController {

    private final RestaurantItemRepository restaurantItemRepository;
    private final RestaurantItemMapper restaurantItemMapper; // infra mapper for gateways
    private final RestaurantItemMapperApp restaurantItemMapperApp; // app-level mapper for API

    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper  restaurantMapper;

    private RestaurantItemEntityRepository restaurantItemGateway() {
        return new RestaurantItemEntityRepository(restaurantItemRepository, restaurantItemMapper);
    }

    private RestaurantEntityRespository  restaurantGateway() {
        return new RestaurantEntityRespository(restaurantRepository, restaurantMapper);
    }

    @PostMapping
    @Operation(summary = "Create restaurant item", description = "Registers a new restaurant item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Restaurant item created successfully",
                    content = @Content(schema = @Schema(implementation = RestaurantItemResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<RestaurantItemResponse> createRestaurantItem(@Valid @RequestBody RestaurantItemCreateRequest req) {
        CreateRestaurantItemInput input = new CreateRestaurantItemInput(
                req.getDescription(),
                req.getPrice(),
                req.getImage(),
                req.getAvailability(),
                req.getRestaurantId()

        );

        CreateRestaurantItemUseCase useCase = new CreateRestaurantItemUseCase(restaurantItemGateway(), restaurantGateway());
        return useCase.execute(input)
                .map(restaurantItemMapperApp::toRestaurantItemResponse)
                .map(resp -> new ResponseEntity<>(resp, HttpStatus.CREATED))
                .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "Get restaurant item by id", description = "Retrieves a restaurant item by its identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Restaurant item found",
                    content = @Content(schema = @Schema(implementation = RestaurantItemResponse.class))),
            @ApiResponse(responseCode = "404", description = "Restaurant item not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<RestaurantItemResponse> getRestaurantItem(@PathVariable @NotNull @Positive Long id) {
        SearchRestaurantItemUseCase search = new SearchRestaurantItemUseCase(restaurantItemGateway());
        return search.findById(id)
                .map(restaurantItemMapperApp::toRestaurantItemResponse)
                .map(resp -> new ResponseEntity<>(resp, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    @Operation(summary = "List restaurant items", description = "Returns a paginated list of items; optional filters: description and restaurantId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of restaurant items returned")
    })
    public ResponseEntity<PageableDto> getRestaurantItems(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) @Schema(hidden = true) Pageable pageable,
                                                          @RequestParam(value = "description", required = false) String description,
                                                          @RequestParam(value = "restaurantId", required = false) Long restaurantId){
        SearchRestaurantItemUseCase search = new SearchRestaurantItemUseCase(restaurantItemGateway());
        Page<RestaurantItem> page = search.execute(pageable, restaurantId, description);
        return new ResponseEntity<>(PageResponse.pageabletoDto(page, restaurantItemMapperApp::toRestaurantItemResponse), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update restaurant item", description = "Updates an existing restaurant item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Restaurant item updated successfully",
                    content = @Content(schema = @Schema(implementation = RestaurantItemResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Restaurant item not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<RestaurantItemResponse> updateRestaurantItem(@PathVariable Long id,
                                                                       @Valid @RequestBody RestaurantItemUpdateRequest req) {
        UpdateRestaurantItemInput input = new UpdateRestaurantItemInput(
                id,
                req.getDescription(),
                req.getPrice(),
                req.getImage(),
                req.getAvailability(),
                req.getRestaurantId()
        );

        UpdateRestaurantItemUseCase useCase = new UpdateRestaurantItemUseCase(restaurantItemGateway(), restaurantGateway());
        return useCase.execute(input)
                .map(restaurantItemMapperApp::toRestaurantItemResponse)
                .map(resp -> new ResponseEntity<>(resp, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete restaurant item", description = "Deletes a restaurant item by its identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Restaurant item deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Restaurant item not found", content = @Content)
    })
    public ResponseEntity<Void> deleteRestaurantItem(@PathVariable Long id){
        DeleteRestaurantItemUseCase useCase = new DeleteRestaurantItemUseCase(restaurantItemGateway());
        boolean deleted = useCase.execute(id);
        return new ResponseEntity<>(deleted ? HttpStatus.NO_CONTENT : HttpStatus.NOT_FOUND);
    }
}
