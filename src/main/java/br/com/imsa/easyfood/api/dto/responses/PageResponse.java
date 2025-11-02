package br.com.imsa.easyfood.api.dto.responses;

import br.com.imsa.easyfood.api.dto.PageableDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.List;


@Getter
@Setter
@RequiredArgsConstructor
@Component
public class PageResponse {
    public static PageableDto pageabletoDto(Page<?> page) {
        return new ModelMapper().map(page, PageableDto.class);
    }

    public static <T, U> PageableDto pageabletoDto(Page<T> page, Class<U> clazz){
        return pageabletoDto(toPageResponse(page, clazz));
    }

    private static <T, U> Page<U> toPageResponse(Page<T> page, Class<U> clazz){
        List<U> reponse =
                page.getContent().stream().map(
                        obj -> new ModelMapper().map(obj, clazz)
                ).toList();
        return new PageImpl<>(reponse, page.getPageable(), page.getTotalElements());
    }
}