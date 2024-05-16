package com.test.librarymanagement.mapper;

import com.test.librarymanagement.domain.data.Patron;
import com.test.librarymanagement.domain.dto.PatronDTO;
import com.test.librarymanagement.domain.input.patron.PatronCreateInput;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PatronMapper {

    Patron mapInputToPatron(PatronCreateInput input);

    PatronDTO mapPatronToDTO(Patron patron);

}
