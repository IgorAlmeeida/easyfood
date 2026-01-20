package br.com.imsa.easyfood.application.v1.controllers;

import br.com.imsa.easyfood.application.v1.dto.PageableDto;
import br.com.imsa.easyfood.application.v1.dto.requests.AddressRequest;
import br.com.imsa.easyfood.application.v1.dto.requests.UserSystemCreateRequest;
import br.com.imsa.easyfood.application.v1.dto.requests.UserSystemUpdateRequest;
import br.com.imsa.easyfood.application.v1.dto.responses.PageResponse;
import br.com.imsa.easyfood.application.v1.dto.responses.UserSystemResponse;
import br.com.imsa.easyfood.application.v1.mappers.AddressMapperApp;
import br.com.imsa.easyfood.application.v1.mappers.UserSystemMapperApp;
import br.com.imsa.easyfood.domain.dto.input.address.CreateAddressInput;
import br.com.imsa.easyfood.domain.dto.input.address.UpdateAddressInput;
import br.com.imsa.easyfood.domain.dto.input.usersystem.CreateUserSystemInput;
import br.com.imsa.easyfood.domain.dto.input.usersystem.UpdateUserSystemInput;
import br.com.imsa.easyfood.domain.dto.output.usersystem.CreateUserSystemOutput;
import br.com.imsa.easyfood.domain.entity.UserSystem;
import br.com.imsa.easyfood.domain.usercase.usersystem.CreateUserSystemUseCase;
import br.com.imsa.easyfood.domain.usercase.usersystem.DeleteUserSystemUseCase;
import br.com.imsa.easyfood.domain.usercase.usersystem.SearchUserSystemUseCase;
import br.com.imsa.easyfood.domain.usercase.usersystem.UpdateUserSystemUseCase;
import br.com.imsa.easyfood.exception.ErrorResponse;
import br.com.imsa.easyfood.infra.adpter.AddressEntityRepository;
import br.com.imsa.easyfood.infra.adpter.UserSystemEntityRepository;
import br.com.imsa.easyfood.infra.mappers.UserSystemMapper;
import br.com.imsa.easyfood.infra.mappers.AddressMapper;
import br.com.imsa.easyfood.infra.repository.AddressRepository;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/user-system/v1", produces = "application/json; charset=utf-8")
@RequiredArgsConstructor
@Tag(name = "UserSystem", description = "CRUD operations for users of the system")
public class UserSystemController {

    private final UserSystemRepository userSystemRepository;
    private final AddressRepository addressRepository;
    private final UserSystemMapper userSystemMapper; // infra mapper for gateways
    private final AddressMapper addressMapper;       // infra mapper for gateways
    private final UserSystemMapperApp userSystemMapperApp; // app-level mapper for API
    private final AddressMapperApp addressMapperApp;        // app-level mapper for API

    private UserSystemEntityRepository userSystemGateway() {
        return new UserSystemEntityRepository(userSystemRepository, userSystemMapper);
    }

    private AddressEntityRepository addressGateway() {
        return new AddressEntityRepository(addressRepository, addressMapper);
    }

    @PostMapping
    @Operation(summary = "Create user", description = "Registers a new user in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully",
                    content = @Content(schema = @Schema(implementation = UserSystemResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<UserSystemResponse> registerUserSystem(@Valid @RequestBody UserSystemCreateRequest req) {
        CreateUserSystemInput input = new CreateUserSystemInput(
                req.getUsername(),
                req.getName(),
                req.getEmail(),
                req.getUserType(),
                toCreateAddressInput(req.getAddress()),
                req.getPassword()
        );
        CreateUserSystemUseCase useCase = new CreateUserSystemUseCase(userSystemGateway(), addressGateway());

        CreateUserSystemOutput output = useCase.execute(input);
        UserSystemResponse response = userSystemMapperApp.toUserSystemResponse(output);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "Get user by id", description = "Retrieves a user by its identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = @Content(schema = @Schema(implementation = UserSystemResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<UserSystemResponse> getUserSystem(@PathVariable @NotNull @Positive Long id) {
        SearchUserSystemUseCase search = new SearchUserSystemUseCase(userSystemGateway());
        return search.findById(id)
                .map(userSystemMapperApp::toUserSystemResponse)
                .map(resp -> new ResponseEntity<>(resp, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    @Operation(summary = "List users", description = "Returns a paginated list of users; optional name filter")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of users returned")
    })
    public ResponseEntity<PageableDto> getUserSystems(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) @Schema(hidden = true) Pageable pageable,
                                                      @RequestParam(value = "name", required = false) String name){
        SearchUserSystemUseCase search = new SearchUserSystemUseCase(userSystemGateway());
        Page<UserSystem> page = (name != null && !name.isEmpty()) ? search.execute(pageable, name) : search.execute(pageable);
        return new ResponseEntity<>(PageResponse.pageabletoDto(page, userSystemMapperApp::toUserSystemResponse), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user", description = "Updates an existing user's information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully",
                    content = @Content(schema = @Schema(implementation = UserSystemResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @Transactional
    public ResponseEntity<UserSystemResponse> updateInfoUserSystem(@PathVariable Long id,
                                                                   @Valid @RequestBody UserSystemUpdateRequest req) {
        UpdateUserSystemInput input = new UpdateUserSystemInput(
                id,
                req.getUsername(),
                req.getName(),
                req.getEmail(),
                req.getUserType(),
                null,
                toUpdateAddressInput(req.getAddress())
        );
        UpdateUserSystemUseCase useCase = new UpdateUserSystemUseCase(userSystemGateway(), addressGateway());
        return useCase.execute(input)
                .map(userSystemMapperApp::toUserSystemResponse)
                .map(resp -> new ResponseEntity<>(resp, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user", description = "Deletes a user by its identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    public ResponseEntity<Void> deleteUserSystem(@PathVariable Long id){
        DeleteUserSystemUseCase useCase = new DeleteUserSystemUseCase(userSystemGateway());
        boolean deleted = useCase.execute(id);
        return new ResponseEntity<>(deleted ? HttpStatus.NO_CONTENT : HttpStatus.NOT_FOUND);
    }

    private CreateAddressInput toCreateAddressInput(AddressRequest req) {
        if (req == null) return null;
        return new CreateAddressInput(req.getStreet(), req.getNeighborhood(), req.getCity(), req.getNumber(), req.getZipCode());
    }

    private UpdateAddressInput toUpdateAddressInput(AddressRequest req) {
        if (req == null) return null;
        return new UpdateAddressInput(null, req.getStreet(), req.getNeighborhood(), req.getCity(), req.getNumber(), req.getZipCode());
    }
}
