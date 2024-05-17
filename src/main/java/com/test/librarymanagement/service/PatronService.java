package com.test.librarymanagement.service;

import com.test.librarymanagement.domain.data.Patron;
import com.test.librarymanagement.domain.dto.PageableDTO;
import com.test.librarymanagement.domain.dto.PatronDTO;
import com.test.librarymanagement.domain.input.patron.PatronCreateInput;
import com.test.librarymanagement.domain.input.patron.PatronUpdateInput;
import com.test.librarymanagement.mapper.PatronMapper;
import com.test.librarymanagement.repository.PatronRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class PatronService {

    private final PatronRepository patronRepository;
    private final PatronMapper patronMapper;


    public PatronDTO addPatron(PatronCreateInput input){

        Patron patron = patronMapper.mapInputToPatron(input);

        return patronMapper.mapPatronToDTO(patronRepository.save(patron));
    }

    public PatronDTO getPatron(Long patronId){

        Patron patron = patronRepository.findById(patronId).orElseThrow(
                () -> new NoSuchElementException("Patron not found")
        );

        return patronMapper.mapPatronToDTO(patron);
    }

    public PageableDTO<PatronDTO> getPatrons(int page, int size) {
        if (size <= 0) {
            size = 100;
        }

        Pageable pageable = PageRequest.of(page, size);

        Page<Patron> result = patronRepository.findAll(pageable);

        return patronMapper.mapToPageableDTO(result);
    }

    public PatronDTO updatePatron(Long patronId, PatronUpdateInput updateInput){

        Patron existingPatron = patronRepository.findById(patronId).orElseThrow(
                () -> new NoSuchElementException("Patron not found")
        );

        patronMapper.patronUpdateInputToPatron(updateInput, existingPatron);

        return patronMapper.mapPatronToDTO(patronRepository.save(existingPatron));
    }

    public void removePatron(Long id){
        patronRepository.deleteById(id);
    }
}
