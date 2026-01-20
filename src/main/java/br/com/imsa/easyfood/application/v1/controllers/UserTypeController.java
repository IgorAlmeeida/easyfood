package br.com.imsa.easyfood.application.v1.controllers;

import br.com.imsa.easyfood.application.v1.dto.PageableDto;
import br.com.imsa.easyfood.application.v1.dto.requests.UserTypeCreateRequest;
import br.com.imsa.easyfood.application.v1.dto.requests.UserTypeUpdateRequest;
import br.com.imsa.easyfood.application.v1.dto.responses.PageResponse;
import br.com.imsa.easyfood.application.v1.dto.responses.UserTypeResponse;
import br.com.imsa.easyfood.application.v1.mappers.UserTypeMapperApp;
import br.com.imsa.easyfood.domain.dto.input.usertype.CreateUserTypeInput;
import br.com.imsa.easyfood.domain.dto.input.usertype.UpdateUserTypeInput;
import br.com.imsa.easyfood.domain.dto.output.usertype.CreateUserTypeOutput;
import br.com.imsa.easyfood.domain.entity.UserType;
import br.com.imsa.easyfood.domain.usercase.usertype.CreateUserTypeUseCase;
import br.com.imsa.easyfood.domain.usercase.usertype.DeleteUserTypeUseCase;
import br.com.imsa.easyfood.domain.usercase.usertype.SearchUserTypeUseCase;
import br.com.imsa.easyfood.domain.usercase.usertype.UpdateUserTypeUseCase;
import br.com.imsa.easyfood.exception.ErrorResponse;
import br.com.imsa.easyfood.infra.adpter.UserTypeEntityRepository;
import br.com.imsa.easyfood.infra.mappers.UserTypeMapper;
import br.com.imsa.easyfood.infra.repository.UserTypeRepository;
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
@RequestMapping(value = "/api/user-type/v1", produces = "application/json; charset=utf-8")
@RequiredArgsConstructor
@Tag(name = "UserType", description = "CRUD operations for user types")
public class UserTypeController {

    private final UserTypeRepository userTypeRepository;
    private final UserTypeMapper userTypeMapper; // infra mapper for gateways
    private final UserTypeMapperApp userTypeMapperApp; // app-level mapper for API

    private UserTypeEntityRepository userTypeGateway() {
        return new UserTypeEntityRepository(userTypeRepository, userTypeMapper);
    }

    @PostMapping
    @Operation(summary = "Create user type", description = "Registers a new user type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User type created successfully",
                    content = @Content(schema = @Schema(implementation = UserTypeResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<UserTypeResponse> createUserType(@Valid @RequestBody UserTypeCreateRequest req) {
        CreateUserTypeInput input = new CreateUserTypeInput(req.getName());
        CreateUserTypeUseCase useCase = new CreateUserTypeUseCase(userTypeGateway());
        return useCase.execute(input)
                .map(userTypeMapperApp::toUserTypeResponse)
                .map(resp -> new ResponseEntity<>(resp, HttpStatus.CREATED))
                .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "Get user type by id", description = "Retrieves a user type by its identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User type found",
                    content = @Content(schema = @Schema(implementation = UserTypeResponse.class))),
            @ApiResponse(responseCode = "404", description = "User type not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<UserTypeResponse> getUserType(@PathVariable @NotNull @Positive Long id) {
        SearchUserTypeUseCase search = new SearchUserTypeUseCase(userTypeGateway());
        return search.findById(id)
                .map(userTypeMapperApp::toUserTypeResponse)
                .map(resp -> new ResponseEntity<>(resp, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    @Operation(summary = "List user types", description = "Returns a paginated list of user types; optional name filter")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of user types returned")
    })
    public ResponseEntity<PageableDto> getUserTypes(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) @Schema(hidden = true) Pageable pageable,
                                                    @RequestParam(value = "name", required = false) String name){
        SearchUserTypeUseCase search = new SearchUserTypeUseCase(userTypeGateway());
        Page<UserType> page = (name != null && !name.isEmpty()) ? search.execute(pageable, name) : search.execute(pageable);
        return new ResponseEntity<>(PageResponse.pageabletoDto(page, userTypeMapperApp::toUserTypeResponse), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user type", description = "Updates an existing user type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User type updated successfully",
                    content = @Content(schema = @Schema(implementation = UserTypeResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "User type not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<UserTypeResponse> updateUserType(@PathVariable Long id,
                                                           @Valid @RequestBody UserTypeUpdateRequest req) {
        UpdateUserTypeInput input = new UpdateUserTypeInput(id, req.getName());
        UpdateUserTypeUseCase useCase = new UpdateUserTypeUseCase(userTypeGateway());
        return useCase.execute(input)
                .map(userTypeMapperApp::toUserTypeResponse)
                .map(resp -> new ResponseEntity<>(resp, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user type", description = "Deletes a user type by its identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User type deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "User type not found", content = @Content)
    })
    public ResponseEntity<Void> deleteUserType(@PathVariable Long id){
        DeleteUserTypeUseCase useCase = new DeleteUserTypeUseCase(userTypeGateway());
        boolean deleted = useCase.execute(id);
        return new ResponseEntity<>(deleted ? HttpStatus.NO_CONTENT : HttpStatus.NOT_FOUND);
    }
}
