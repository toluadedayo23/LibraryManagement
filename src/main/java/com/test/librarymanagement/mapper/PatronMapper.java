package com.test.librarymanagement.mapper;

import com.test.librarymanagement.domain.data.Patron;
import com.test.librarymanagement.domain.dto.PageableDTO;
import com.test.librarymanagement.domain.dto.PatronDTO;
import com.test.librarymanagement.domain.input.patron.PatronCreateInput;
import com.test.librarymanagement.domain.input.patron.PatronUpdateInput;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface PatronMapper {

    Patron mapInputToPatron(PatronCreateInput input);

    PatronDTO mapPatronToDTO(Patron patron);

    @Mapping(target = "pageNumber", source = "number")
    PageableDTO<PatronDTO> mapToPageableDTO(Page<Patron> result);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patronUpdateInputToPatron(PatronUpdateInput updateInput, @MappingTarget Patron patron);

}
