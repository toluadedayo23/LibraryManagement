package com.test.librarymanagement.service;

import com.test.librarymanagement.domain.data.Patron;
import com.test.librarymanagement.domain.dto.PageableDTO;
import com.test.librarymanagement.domain.dto.PatronDTO;
import com.test.librarymanagement.domain.input.patron.PatronCreateInput;
import com.test.librarymanagement.domain.input.patron.PatronUpdateInput;
import com.test.librarymanagement.mapper.PatronMapper;
import com.test.librarymanagement.repository.PatronRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PatronServiceTest {

    @Mock
    private PatronRepository patronRepository;

    @Mock
    private PatronMapper patronMapper;

    @InjectMocks
    private PatronService patronService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void should_add_patron(){

        PatronCreateInput input = PatronCreateInput.builder()
                .firstName("first")
                .lastName("Last")
                .Address("Lagos 1")
                .phonenumber("08089765376")
                .build();

        Patron patron = Patron.builder()
                .firstName("first")
                .lastName("Last")
                .Address("Lagos 1")
                .phonenumber("08089765376")
                .build();

        PatronDTO dto = PatronDTO.builder()
                .id(2L)
                .firstName("first")
                .lastName("Last")
                .Address("Lagos 1")
                .phonenumber("08089765376")
                .build();

        when(patronMapper.mapInputToPatron(input)).thenReturn(patron);
        when(patronRepository.save(patron)).thenReturn(patron);
        when(patronMapper.mapPatronToDTO(patron)).thenReturn(dto);

        PatronDTO response = patronService.addPatron(input);

        assertEquals(input.getFirstName(), response.getFirstName());
        assertEquals(input.getAddress(), response.getAddress());

        verify(patronMapper, times(1)).mapInputToPatron(input);
        verify(patronRepository, times(1)).save(patron);
        verify(patronMapper, times(1)).mapPatronToDTO(patron);

    }

    @Test
    public void should_throw_NoSuchElementException_if_patron_not_found(){

        when(patronRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> patronService.getPatron(anyLong()));
    }

    @Test
    public void should_get_patron(){
        Patron patron = Patron.builder()
                .firstName("first")
                .lastName("Last")
                .Address("Lagos 1")
                .phonenumber("08089765376")
                .build();

        PatronDTO dto = PatronDTO.builder()
                .id(2L)
                .firstName("first")
                .lastName("Last")
                .Address("Lagos 1")
                .phonenumber("08089765376")
                .build();

        when(patronRepository.findById(anyLong())).thenReturn(Optional.of(patron));
        when(patronMapper.mapPatronToDTO(patron)).thenReturn(dto);

        PatronDTO response = patronService.getPatron(anyLong());

        assertEquals(patron.getFirstName(), response.getFirstName());
        assertEquals(patron.getPhonenumber(), response.getPhonenumber());

        verify(patronRepository, times(1)).findById(anyLong());
        verify(patronMapper, times(1)).mapPatronToDTO(patron);

    }

    @Test
    public void should_get_patrons(){
        int page = 0;
        int size = 2;

        Pageable pageable = PageRequest.of(page, size);

        Patron patron = Patron.builder()
                .id(1L)
                .firstName("first")
                .lastName("Last")
                .Address("Lagos 1")
                .phonenumber("08089765376")
                .build();

        Patron patron2 = Patron.builder()
                .id(2L)
                .firstName("first2")
                .lastName("Last2")
                .Address("Lagos 2")
                .phonenumber("08089765377")
                .build();

        Patron patron3 = Patron.builder()
                .id(3L)
                .firstName("first3")
                .lastName("Last3")
                .Address("Lagos 3")
                .phonenumber("08089765378")
                .build();
        List<Patron> patrons = List.of(patron, patron2, patron3);
        Page<Patron> pageResult = convertListToPage(patrons, pageable);

        PatronDTO dto = PatronDTO.builder()
                .id(1L)
                .firstName("first")
                .lastName("Last")
                .Address("Lagos 1")
                .phonenumber("08089765376")
                .build();

        PatronDTO dto2 = PatronDTO.builder()
                .id(2L)
                .firstName("first2")
                .lastName("Last2")
                .Address("Lagos 2")
                .phonenumber("08089765377")
                .build();

        PatronDTO dto3 = PatronDTO.builder()
                .id(3L)
                .firstName("first3")
                .lastName("Last3")
                .Address("Lagos 3")
                .phonenumber("08089765378")
                .build();
        PageableDTO<PatronDTO> dtoPageResult = new PageableDTO<>(convertListToPage(List.of(dto, dto2, dto3), pageable));

        when(patronRepository.findAll(pageable)).thenReturn(pageResult);
        when(patronMapper.mapToPageableDTO(pageResult)).thenReturn(dtoPageResult);

        PageableDTO<PatronDTO> response = patronService.getPatrons(page, size);

        assertNotNull(response);
        assertEquals(getPageResultSize(page, size, patrons.size()), response.getContent().size());
        assertEquals(patrons.size(), response.getTotalElements());
        assertEquals(page, response.getPageNumber());

        verify(patronRepository, times(1)).findAll(pageable);
        verify(patronMapper, times(1)).mapToPageableDTO(pageResult);

    }

    @Test
    public void should_update_patron(){

        Long patronId = 2L;

        PatronUpdateInput updateInput = PatronUpdateInput.builder()
                .firstName("first")
                .lastName("Last")
                .Address("Lagos 1")
                .phonenumber("08089765370")
                .build();

        Patron existingPatron = Patron.builder()
                .id(2L)
                .firstName("first2")
                .lastName("Last2")
                .Address("Lagos 2")
                .phonenumber("08089765377")
                .build();

        PatronDTO dto = PatronDTO.builder()
                .id(1L)
                .firstName("first")
                .lastName("Last")
                .Address("Lagos 1")
                .phonenumber("08089765370")
                .build();


        when(patronRepository.findById(patronId)).thenReturn(Optional.of(existingPatron));
        doNothing().when(patronMapper).patronUpdateInputToPatron(updateInput, existingPatron);
        when(patronRepository.save(existingPatron)).thenReturn(existingPatron);
        when(patronMapper.mapPatronToDTO(existingPatron)).thenReturn(dto);

        patronService.updatePatron(patronId, updateInput);

        verify(patronRepository, times(1)).findById(patronId);
        verify(patronMapper, times(1)).patronUpdateInputToPatron(updateInput, existingPatron);
        verify(patronRepository, times(1)).save(existingPatron);
        verify(patronMapper, times(1)).mapPatronToDTO(existingPatron);
    }

    private <T> Page<T> convertListToPage(List<T> list, Pageable pageable) {

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), list.size());

        List<T> sublist = list.subList(start, end);

        return new PageImpl<>(sublist, pageable, list.size());
    }

    private int getPageResultSize(int page, int size, int totalElements){
        int startIndex = page * size;
        int endIndex = Math.min(startIndex + size, totalElements);

        return endIndex - startIndex;
    }

}