package br.com.imsa.easyfood.api.controller.v1;

import br.com.imsa.easyfood.api.dto.PageableDto;
import br.com.imsa.easyfood.api.dto.requests.UserSystemRequest;
import br.com.imsa.easyfood.api.dto.responses.PageResponse;
import br.com.imsa.easyfood.api.dto.responses.UserSystemResponse;
import br.com.imsa.easyfood.domain.entity.UserSystem;
import br.com.imsa.easyfood.domain.service.UserSystemService;
import br.com.imsa.easyfood.exception.NegocioException;
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
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/user-system/v1", produces = "application/json; charset=utf-8")
@RequiredArgsConstructor
@Tag(name = "UserSystem", description = "CRUD operations for users of the system")
public class UserSystemController {

    private final UserSystemService userSystemService;
    private final ModelMapper modelMapper;

    @PostMapping(value = "/")
    @Operation(summary = "Create user", description = "Registers a new user in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully",
                    content = @Content(schema = @Schema(implementation = UserSystemResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content)
    })
    public ResponseEntity<UserSystemResponse> registerUserSystem(@Valid @RequestBody UserSystemRequest userSystemRequest) {
        UserSystem userSystem = userSystemService.createUserSystem(userSystemRequest);
        return new ResponseEntity<>(modelMapper.map(userSystem, UserSystemResponse.class), HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "Get user by id", description = "Retrieves a user by its identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = @Content(schema = @Schema(implementation = UserSystemResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    public ResponseEntity<UserSystemResponse> getUserSystem(@PathVariable @NotNull @Positive Long id) {
        UserSystem userSystem = userSystemService.getUserSystem(id);
        if (userSystem == null) {
            throw new NegocioException("Usuário não encontrado.");
        }
        return new ResponseEntity<>(modelMapper.map(userSystem, UserSystemResponse.class), HttpStatus.OK);
    }

    @GetMapping("/")
    @Operation(summary = "List users", description = "Returns a paginated list of users; optional name filter")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of users returned")
    })
    public ResponseEntity<PageableDto> getUserSystems(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) @Schema(hidden = true) Pageable pageable,
                                                      @RequestParam(value = "page", required = false) String page,
                                                      @RequestParam(value = "sort", required = false) String sort,
                                                      @RequestParam(value = "size", required = false) Integer size,
                                                      @RequestParam(value = "name", required = false) String name){
        Page<UserSystem> userSystems;
        if (name != null && !name.isEmpty()) {
            userSystems = userSystemService.getAllUserSystems(pageable, name);
        } else {
            userSystems = userSystemService.getAllUserSystems(pageable) ;
        }

        return new ResponseEntity<>(PageResponse.pageabletoDto(userSystems, UserSystemResponse.class), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user", description = "Updates an existing user's information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully",
                    content = @Content(schema = @Schema(implementation = UserSystemResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    public ResponseEntity<UserSystemResponse> updateInfoUserSystem(@PathVariable Long id, @Valid @RequestBody UserSystemRequest userSystemRequest) {
        UserSystem userSystem = userSystemService.updateUserSystem(id, userSystemRequest);
        return new ResponseEntity<>(modelMapper.map(userSystem, UserSystemResponse.class), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user", description = "Deletes a user by its identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    public ResponseEntity<Void> deleteUserSystem(@PathVariable Long id){
        this.userSystemService.deleteUserSystem(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
