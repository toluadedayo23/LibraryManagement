package com.test.librarymanagement.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.test.librarymanagement.domain.dto.PageableDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class PageableDataResponse<T extends Serializable> extends PageableDTO<T> implements Response {
    private String message;

    public PageableDataResponse(PageableDTO<T> page) {
        setContent(page.getContent());
        setTotalPages(page.getTotalPages());
        setTotalElements(page.getTotalElements());
        setFirst(page.isFirst());
        setLast(page.isLast());
        setPageNumber(page.getPageNumber());
    }

}
