package com.test.librarymanagement.controller;

import com.test.librarymanagement.domain.dto.PageableDTO;
import com.test.librarymanagement.domain.dto.PatronDTO;
import com.test.librarymanagement.domain.input.patron.PatronCreateInput;
import com.test.librarymanagement.domain.input.patron.PatronUpdateInput;
import com.test.librarymanagement.response.PageableDataResponse;
import com.test.librarymanagement.service.PatronService;
import com.test.librarymanagement.util.WebSecurityUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@RestController
@AllArgsConstructor
@RequestMapping("api/patrons/")
@PreAuthorize("hasAuthority('LIBRARIAN')")
public class PatronController {
    private final PatronService patronService;
    private final WebSecurityUtil webSecurityUtil;

    @PostMapping
    public ResponseEntity<PatronDTO> addPatron(@Valid @RequestBody PatronCreateInput input){
        log.info("Librarian: {} adding Patron", webSecurityUtil.getUserForLog());

        PatronDTO dto = patronService.addPatron(input);

        log.info("Librarian: {} added Patron: {}", webSecurityUtil.getUserForLog(), dto.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);

    }

    @GetMapping("{id}")
    public ResponseEntity<PatronDTO> getPatronById(
            @PathVariable(value = "id") @Min(value = 1) Long id
    ){
        log.info("Librarian: {} fetching patron: {}", webSecurityUtil.getUserForLog(), id);

        PatronDTO dto = patronService.getPatron(id);

        log.info("Librarian: {} fetched patron: {}", webSecurityUtil.getUserForLog(), id);

        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<PageableDataResponse<PatronDTO>> getPatrons(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "0") int size
    ){
        log.info("Librarian: {} fetching patrons", webSecurityUtil.getUserForLog());

        PageableDTO<PatronDTO> patrons = patronService.getPatrons(page, size);

        log.info("Librarian: {} fetched patrons", webSecurityUtil.getUserForLog());

        return ResponseEntity.ok(new PageableDataResponse<>(patrons));
    }

    @PutMapping("{id}")
    public ResponseEntity<PatronDTO> updatePatron(
            @PathVariable(value = "id") @Min(value = 1) Long id,
            @Valid @RequestBody PatronUpdateInput updateInput
    ){

        log.info("Librarian: {} updating patron: {}", webSecurityUtil.getUserForLog(), id);

        PatronDTO dto = patronService.updatePatron(id, updateInput);

        log.info("Librarian: {} updated book: {}", webSecurityUtil.getUserForLog(), id);

        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> removePatron(@PathVariable(value = "id") @Min(value = 1) Long id){
        log.info("Librarian: {} deleting patron: {}", webSecurityUtil.getUserForLog(), id);

        patronService.removePatron(id);

        log.info("Librarian: {} deleted patron: {}", webSecurityUtil.getUserForLog(), id);

        return ResponseEntity.noContent().build();
    }

}
