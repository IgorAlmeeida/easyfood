package br.com.imsa.easyfood.application.v1.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Schema(name = "PageableDto", description = "Standard pagination wrapper for list results")
public class PageableDto {

    @Schema(description = "Page content list")
    private List<Object> content = new ArrayList<>();

    @Schema(description = "Indicates if this is the first page", example = "true")
    private boolean first;

    @Schema(description = "Indicates if this is the last page", example = "false")
    private boolean last;

    @Schema(description = "Current page number (0-based)", example = "0")
    @JsonProperty("page")
    private int number;

    @Schema(description = "Page size (elements per page)", example = "10")
    private int size;

    @Schema(description = "Number of elements in the current page", example = "10")
    @JsonProperty("pageElements")
    private int numberOfElements;

    @Schema(description = "Total number of pages", example = "5")
    private int totalPages;

    @Schema(description = "Total number of elements across all pages", example = "42")
    private int totalElements;
}
