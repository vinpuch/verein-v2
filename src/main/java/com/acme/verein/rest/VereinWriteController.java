
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
//import com.acme.verein.service.VereinReadService;
import com.acme.verein.service.VereinWriteService;
import com.acme.verein.service.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
//import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.acme.verein.rest.VereinGetController.ID_PATTERN;
import static com.acme.verein.rest.UriHelper.getBaseUri;
import static com.acme.verein.rest.UriHelper.getRequestUri;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.notFound;

/**
 * Eine `@RestController`-Klasse bildet die REST-Schnittstelle, wobei die HTTP-Methoden, Pfade und MIME-Typen auf die
 * Methoden der Klasse abgebildet werden.
 * <img src="../../../../../asciidoc/VereinWriteController.svg" alt="Klassendiagramm">
 *
 * @author <a href="mailto:Juergen.Zimmermann@h-ka.de">Jürgen Zimmermann</a>
 */
@RestController
@RequestMapping("/")
@Tag(name = "Verein API")
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings("ClassFanOutComplexity")
final class VereinWriteController {
    @SuppressWarnings("TrailingComment")
    private static final String PROBLEM_PATH = "/problem/"; //NOSONAR

    private final VereinWriteService service;
    //private final VereinReadService readService;

    /**
     * Einen neuen Verein-Datensatz anlegen.
     *
     * @param vereinDTO Das Vereinobjekt aus dem eingegangenen Request-Body.
     * @param request Das Request-Objekt, um `Location` im Response-Header zu erstellen.
     * @return Response mit Statuscode 201 einschließlich Location-Header oder Statuscode 422 falls Constraints verletzt
     *      sind oder die Emailadresse bereits existiert oder Statuscode 400 falls syntaktische Fehler im Request-Body
     *      vorliegen.
     * @throws URISyntaxException falls die URI im Request-Objekt nicht korrekt wäre
     */
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    @Operation(summary = "Einen neuen Verein anlegen", tags = "Neuanlegen")
    @ApiResponse(responseCode = "201", description = "Verein neu angelegt")
    @ApiResponse(responseCode = "400", description = "Syntaktische Fehler im Request-Body")
    @ApiResponse(responseCode = "422", description = "Ungültige Werte oder Email vorhanden")
    @SuppressWarnings("TrailingComment")
    ResponseEntity<Void> create(
        @RequestBody final VereinDTO vereinDTO,
        final HttpServletRequest request
    ) throws URISyntaxException {
        log.debug("create: {}", vereinDTO);

        final var verein = service.create(vereinDTO.toVerein());
        final var baseUri = getBaseUri(request);
        final var location = new URI(baseUri + "/" + verein.getId()); //NOSONAR
        return created(location).build();
    }

    /**
     * Einen vorhandenen Verein-Datensatz überschreiben.
     *
     * @param id ID der zu aktualisierenden Verein.
     * @param vereinDTO Das Vereinobjekt aus dem eingegangenen Request-Body.
     * @return Response mit Statuscode 204 oder Statuscode 422, falls Constraints verletzt sind oder
     *      der JSON-Datensatz syntaktisch nicht korrekt ist oder falls die Emailadresse bereits existiert oder
     *      Statuscode 400 falls syntaktische Fehler im Request-Body vorliegen.
     */
    @PutMapping(path = "{id:" + ID_PATTERN + "}", consumes = APPLICATION_JSON_VALUE)
    @Operation(summary = "Eine Verein mit neuen Werten aktualisieren", tags = "Aktualisieren")
    @ApiResponse(responseCode = "204", description = "Aktualisiert")
    @ApiResponse(responseCode = "400", description = "Syntaktische Fehler im Request-Body")
    @ApiResponse(responseCode = "404", description = "Verein nicht vorhanden")
    @ApiResponse(responseCode = "422", description = "Ungültige Werte oder Email vorhanden")
    ResponseEntity<Void> update(
        @PathVariable final UUID id,
        @RequestBody final VereinDTO vereinDTO
    ) {
        log.debug("update: id={}, {}", id, vereinDTO);
        service.update(vereinDTO.toVerein(), id);
        return noContent().build();
    }



    /**
     * Eine vorhandene Verein anhand seiner ID löschen.
     *
     * @param id ID der zu löschenden Verein.
     * @return Response mit Statuscode 204.
     */
    @DeleteMapping(path = "{id:" + ID_PATTERN + "}")
    @Operation(summary = "Eine Verein anhand der ID loeschen", tags = "Loeschen")
    @ApiResponse(responseCode = "204", description = "Gelöscht")
    ResponseEntity<Void> deleteById(@PathVariable final UUID id)  {
        log.debug("deleteById: id={}", id);
        service.deleteById(id);
        return noContent().build();
    }

    @ExceptionHandler(ConstraintViolationsException.class)
    @SuppressWarnings("unused")
    ResponseEntity<ProblemDetail> handleConstraintViolations(
        final ConstraintViolationsException ex,
        final HttpServletRequest request
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
        problemDetail.setType(URI.create(PROBLEM_PATH + com.acme.verein.rest.ProblemType.CONSTRAINTS.getValue()));
        final var uri = getRequestUri(request);
        problemDetail.setInstance(uri);

        return ResponseEntity.of(problemDetail).build();
    }

    @ExceptionHandler(EmailExistsException.class)
    @SuppressWarnings("unused")
    ResponseEntity<ProblemDetail> handleEmailExists(final EmailExistsException ex, final HttpServletRequest request) {
        log.debug("handleEmailExists: {}", ex.getMessage());
        final var problemDetail = ProblemDetail.forStatusAndDetail(UNPROCESSABLE_ENTITY, ex.getMessage());
        problemDetail.setType(URI.create(PROBLEM_PATH + com.acme.verein.rest.ProblemType.CONSTRAINTS.getValue()));
        final var uri = getRequestUri(request);
        problemDetail.setInstance(uri);
        return ResponseEntity.of(problemDetail).build();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @SuppressWarnings("unused")
    ResponseEntity<ProblemDetail> handleMessageNotReadable(
        final HttpMessageNotReadableException ex,
        final HttpServletRequest request
    ) {
        log.debug("handleMessageNotReadable: {}", ex.getMessage());
        final var problemDetail = ProblemDetail.forStatusAndDetail(BAD_REQUEST, ex.getMessage());
        problemDetail.setType(URI.create(PROBLEM_PATH + com.acme.verein.rest.ProblemType.BAD_REQUEST.getValue()));
        final var uri = getRequestUri(request);
        problemDetail.setInstance(uri);
        return ResponseEntity.of(problemDetail).build();
    }

    @ExceptionHandler(NotFoundException.class)
    @SuppressWarnings("unused")
    ResponseEntity<String> handleNotFound(final NotFoundException ex) {
        log.debug("handleNotFound: {}", ex.getMessage());
        return notFound().build();
    }


}
