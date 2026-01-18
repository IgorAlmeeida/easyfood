package br.com.imsa.easyfood.application.v1.dto.responses;

import br.com.imsa.easyfood.application.v1.dto.PageableDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

@Getter
@Setter
@RequiredArgsConstructor
@Component
public class PageResponse {

    public static PageableDto pageabletoDto(Page<?> page) {
        PageableDto dto = new PageableDto();
        dto.setFirst(page.isFirst());
        dto.setLast(page.isLast());
        dto.setNumber(page.getNumber());
        dto.setSize(page.getSize());
        dto.setNumberOfElements(page.getNumberOfElements());
        dto.setTotalPages(page.getTotalPages());
        dto.setTotalElements((int) page.getTotalElements());
        dto.getContent().addAll(page.getContent());
        return dto;
    }

    public static <T, U> PageableDto pageabletoDto(Page<T> page, Function<T, U> mapper){
        List<U> content = page.getContent().stream().map(mapper).toList();
        return pageabletoDto(new org.springframework.data.domain.PageImpl<>(content, page.getPageable(), page.getTotalElements()));
    }
}