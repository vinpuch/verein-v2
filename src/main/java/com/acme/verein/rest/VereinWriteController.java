/*
 * Copyright (C) 2022 - present Juergen Zimmermann, Hochschule Karlsruhe
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.acme.verein.rest;

import com.acme.verein.service.ConstraintViolationsException;
import com.acme.verein.service.EmailExistsException;
import com.acme.verein.service.NotFoundException;
import com.acme.verein.service.VereinWriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;
import java.util.stream.Collectors;
import static com.acme.verein.rest.VereinGetController.ID_PATTERN;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.created;

/**
 * Eine `@RestController`-Klasse bildet die REST-Schnittstelle, wobei die HTTP-Methoden, Pfade und MIME-Typen auf die
 * Funktionen der Klasse abgebildet werden.
 * ![Klassendiagramm](../../../images/KundeWriteController.svg)
 *
 * @author <a href="mailto:Juergen.Zimmermann@h-ka.de">Jürgen Zimmermann</a>
 */
@RestController
@RequestMapping("/rest")
@Tag(name = "Verein API")
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings("ClassFanOutComplexity")
final class VereinWriteController {
    @SuppressWarnings("TrailingComment")
    private static final String PROBLEM_PATH = "/problem/"; //NOSONAR

    private final VereinWriteService service;

    //private final VereinReadService readService;
    //  private final KundePatcher patcher;
    private final UriHelper uriHelper;


    /**
     * Einen neuen Verein-Datensatz anlegen.
     *
     * @param vereinDTO Das Vereinobjekt aus dem eingegangenen Request-Body.
     * @param request   wartet auf ServletRequest.
     * @return URI dem eingegangenen Request-Body.
     */
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(NO_CONTENT)
    @Operation(summary = "Ein neues Verein anlegen", tags = "Neuanlegen")
    @ApiResponse(responseCode = "201", description = "Verein neu angelegt")
    @ApiResponse(responseCode = "400", description = "Syntaktische Fehler im Request-Body")
    @ApiResponse(responseCode = "422", description = "Ungültige Werte")
    @SuppressWarnings("TrailingComment")
    ResponseEntity<Void> create(
        @RequestBody final VereinDTO vereinDTO,
        final HttpServletRequest request
    ) throws URISyntaxException {
        log.debug("create: {}", vereinDTO);

        final var verein = service.create(vereinDTO.toVerein());
        final var baseUri = uriHelper.getBaseUri(request).toString();
        final var location = new URI(baseUri + '/' + verein.getId()); //NOSONAR
        return created(location).build();
    }

    /**
     * Einen vorhandenen Verein-Datensatz überschreiben.
     *
     * @param id        ID des zu aktualisierenden Vereins.
     * @param vereinDTO Das Kundenobjekt aus dem eingegangenen Request-Body.
     */
    @PutMapping(path = "{id:" + ID_PATTERN + "}", consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(NO_CONTENT)
    @Operation(summary = "Ein Verein mit neuen Werten aktualisieren", tags = "Aktualisieren")
    @ApiResponse(responseCode = "204", description = "Aktualisiert")
    @ApiResponse(responseCode = "400", description = "Syntaktische Fehler im Request-Body")
    @ApiResponse(responseCode = "404", description = "Verein nicht vorhanden")
    @ApiResponse(responseCode = "422", description = "Ungültige Werte")
    void update(@PathVariable final UUID id, @RequestBody final VereinDTO vereinDTO
    ) {
        log.debug("update: id={}, {}", id, vereinDTO);
        service.update(vereinDTO.toVerein(), id);
    }


    @ExceptionHandler
    @ResponseStatus(UNPROCESSABLE_ENTITY)
    @SuppressWarnings("unused")
    ProblemDetail handleConstraintViolations(final ConstraintViolationsException ex, final HttpServletRequest request
    ) {
        log.debug("handleConstraintViolations: {}", ex.getMessage());

        final var vereinViolations = ex.getViolations()
            .stream()
            .map(violation -> violation.getPropertyPath() + ": " +
                violation.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName() + " " +
                violation.getMessage())
            .collect(Collectors.toList());
        log.trace("handleConstraintViolations: {}", vereinViolations);
        final String detail;
        if (vereinViolations.isEmpty()) {
            detail = "N/A";
        } else {
            // [ und ] aus dem String der Liste entfernen
            final var violationsStr = vereinViolations.toString();
            detail = violationsStr.substring(1, violationsStr.length() - 2);
        }

        final var problemDetail = ProblemDetail.forStatusAndDetail(UNPROCESSABLE_ENTITY, detail);
        problemDetail.setType(URI.create(PROBLEM_PATH + ProblemType.CONSTRAINTS.getValue()));
        final var uri = uriHelper.getBaseUri(request);
        problemDetail.setInstance(uri);

        return problemDetail;
    }


    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    @SuppressWarnings("unused")
    ProblemDetail handleMessageNotReadable(final HttpMessageNotReadableException ex) {
        log.debug("handleMessageNotReadable: {}", ex.getMessage());
        final var problemDetail = ProblemDetail.forStatusAndDetail(BAD_REQUEST, ex.getMessage());
        problemDetail.setType(URI.create(PROBLEM_PATH + ProblemType.BAD_REQUEST.getValue()));
        return problemDetail;
    }

    @ExceptionHandler
    @SuppressWarnings("unused")
    ResponseEntity<ProblemDetail> onEmailExists(final EmailExistsException ex, final HttpServletRequest request) {
        log.debug("onEmailExists: {}", ex.getMessage());
        final var problemDetail = ProblemDetail.forStatusAndDetail(UNPROCESSABLE_ENTITY, ex.getMessage());
        problemDetail.setType(URI.create(PROBLEM_PATH + ProblemType.CONSTRAINTS.getValue()));
        final var uri = uriHelper.getBaseUri(request);
        problemDetail.setInstance(uri);
        return ResponseEntity.of(problemDetail).build();
    }

    /**
     * ExceptionHandler für eine NotFoundException.
     *
     * @param ex Die Exception
     */
    @ExceptionHandler
    @ResponseStatus(NOT_FOUND)
    void onNotFound(final NotFoundException ex) {
        log.debug("onNotFound: {}", ex.getMessage());
    }
}
