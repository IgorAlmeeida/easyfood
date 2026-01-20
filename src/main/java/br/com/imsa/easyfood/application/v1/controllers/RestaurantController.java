package br.com.imsa.easyfood.application.v1.controllers;

import br.com.imsa.easyfood.application.v1.dto.PageableDto;
import br.com.imsa.easyfood.application.v1.dto.requests.RestaurantCreateRequest;
import br.com.imsa.easyfood.application.v1.dto.requests.RestaurantUpdateRequest;
import br.com.imsa.easyfood.application.v1.dto.responses.PageResponse;
import br.com.imsa.easyfood.application.v1.dto.responses.RestaurantResponse;
import br.com.imsa.easyfood.application.v1.mappers.RestaurantMapperApp;
import br.com.imsa.easyfood.domain.dto.input.restaurant.CreateRestaurantInput;
import br.com.imsa.easyfood.domain.dto.input.restaurant.UpdateRestaurantInput;
import br.com.imsa.easyfood.domain.dto.output.restaurant.CreateRestaurantOutput;
import br.com.imsa.easyfood.domain.entity.Restaurant;
import br.com.imsa.easyfood.domain.usercase.restaurant.CreateRestaurantUseCase;
import br.com.imsa.easyfood.domain.usercase.restaurant.DeleteRestaurantUseCase;
import br.com.imsa.easyfood.domain.usercase.restaurant.SearchRestaurantUseCase;
import br.com.imsa.easyfood.domain.usercase.restaurant.UpdateRestaurantUseCase;
import br.com.imsa.easyfood.exception.ErrorResponse;
import br.com.imsa.easyfood.infra.adpter.AddressEntityRepository;
import br.com.imsa.easyfood.infra.adpter.RestaurantEntityRespository;
import br.com.imsa.easyfood.infra.adpter.UserSystemEntityRepository;
import br.com.imsa.easyfood.infra.mappers.AddressMapper;
import br.com.imsa.easyfood.infra.mappers.RestaurantMapper;
import br.com.imsa.easyfood.infra.mappers.UserSystemMapper;
import br.com.imsa.easyfood.infra.repository.AddressRepository;
import br.com.imsa.easyfood.infra.repository.RestaurantRepository;
import br.com.imsa.easyfood.infra.repository.UserSystemRepository;
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
@RequestMapping(value = "/api/restaurant/v1", produces = "application/json; charset=utf-8")
@RequiredArgsConstructor
@Tag(name = "Restaurant", description = "CRUD operations for restaurants")
public class RestaurantController {

    private final RestaurantRepository restaurantRepository;
    private final AddressRepository addressRepository;
    private final UserSystemRepository userSystemRepository;

    private final RestaurantMapper restaurantMapper; // infra mapper for gateways
    private final AddressMapper addressMapper;       // infra mapper for gateways
    private final UserSystemMapper userSystemMapper; // infra mapper for gateways

    private final RestaurantMapperApp restaurantMapperApp; // app-level mapper for API

    private RestaurantEntityRespository restaurantGateway() {
        return new RestaurantEntityRespository(restaurantRepository, restaurantMapper);
    }

    private AddressEntityRepository addressGateway() {
        return new AddressEntityRepository(addressRepository, addressMapper);
    }

    private UserSystemEntityRepository userSystemGateway() {
        return new UserSystemEntityRepository(userSystemRepository, userSystemMapper);
    }

    @PostMapping
    @Operation(summary = "Create restaurant", description = "Registers a new restaurant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Restaurant created successfully",
                    content = @Content(schema = @Schema(implementation = RestaurantResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<RestaurantResponse> createRestaurant(@Valid @RequestBody RestaurantCreateRequest req) {
        CreateRestaurantInput input = new CreateRestaurantInput(
                req.getName(),
                req.getAddressId(),
                req.getKitchenType(),
                req.getStartOperationTime(),
                req.getEndOperationTime(),
                req.getProprietaryId()
        );
        CreateRestaurantUseCase useCase = new CreateRestaurantUseCase(restaurantGateway(), addressGateway(), userSystemGateway());
        return useCase.execute(input)
                .map(restaurantMapperApp::toRestaurantResponse)
                .map(resp -> new ResponseEntity<>(resp, HttpStatus.CREATED))
                .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "Get restaurant by id", description = "Retrieves a restaurant by its identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Restaurant found",
                    content = @Content(schema = @Schema(implementation = RestaurantResponse.class))),
            @ApiResponse(responseCode = "404", description = "Restaurant not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<RestaurantResponse> getRestaurant(@PathVariable @NotNull @Positive Long id) {
        SearchRestaurantUseCase search = new SearchRestaurantUseCase(restaurantGateway());
        return search.findById(id)
                .map(restaurantMapperApp::toRestaurantResponse)
                .map(resp -> new ResponseEntity<>(resp, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    @Operation(summary = "List restaurants", description = "Returns a paginated list of restaurants; optional name filter")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of restaurants returned")
    })
    public ResponseEntity<PageableDto> getRestaurants(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) @Schema(hidden = true) Pageable pageable,
                                                      @RequestParam(value = "name", required = false) String name){
        SearchRestaurantUseCase search = new SearchRestaurantUseCase(restaurantGateway());
        Page<Restaurant> page = (name != null && !name.isEmpty()) ? search.execute(pageable, name) : search.execute(pageable);
        return new ResponseEntity<>(PageResponse.pageabletoDto(page, restaurantMapperApp::toRestaurantResponse), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update restaurant", description = "Updates an existing restaurant's information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Restaurant updated successfully",
                    content = @Content(schema = @Schema(implementation = RestaurantResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Restaurant not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<RestaurantResponse> updateRestaurant(@PathVariable Long id,
                                                               @Valid @RequestBody RestaurantUpdateRequest req) {
        UpdateRestaurantInput input = new UpdateRestaurantInput(
                id,
                req.getName(),
                req.getAddressId(),
                req.getKitchenType(),
                req.getStartOperationTime(),
                req.getEndOperationTime(),
                req.getProprietaryId()
        );
        UpdateRestaurantUseCase useCase = new UpdateRestaurantUseCase(restaurantGateway(), addressGateway(), userSystemGateway());
        return useCase.execute(input)
                .map(restaurantMapperApp::toRestaurantResponse)
                .map(resp -> new ResponseEntity<>(resp, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete restaurant", description = "Deletes a restaurant by its identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Restaurant deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Restaurant not found", content = @Content)
    })
    public ResponseEntity<Void> deleteRestaurant(@PathVariable Long id){
        DeleteRestaurantUseCase useCase = new DeleteRestaurantUseCase(restaurantGateway());
        boolean deleted = useCase.execute(id);
        return new ResponseEntity<>(deleted ? HttpStatus.NO_CONTENT : HttpStatus.NOT_FOUND);
    }
}
