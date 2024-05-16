package com.test.librarymanagement.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuperBuilder
@NoArgsConstructor
public class PageableDTO<T> {
    private int totalPages;
    private long totalElements;
    private boolean first;
    private boolean last;
    private int pageNumber;
    @Builder.Default
    private List<T> content = new ArrayList<>();
    private String message;

    public PageableDTO(Page<T> page) {
        this.content = page.getContent();
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.first = page.isFirst();
        this.last = page.isLast();
        this.pageNumber = page.getNumber();
    }

}