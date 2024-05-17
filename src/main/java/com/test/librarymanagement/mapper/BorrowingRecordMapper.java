package com.test.librarymanagement.mapper;

import com.test.librarymanagement.domain.data.BorrowingRecord;
import com.test.librarymanagement.domain.dto.BorrowingRecordDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BorrowingRecordMapper {

    BorrowingRecordDTO mapToDTO(BorrowingRecord borrowingRecord);
}
