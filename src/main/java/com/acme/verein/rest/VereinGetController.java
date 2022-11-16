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

import com.acme.verein.entity.Verein;
import com.acme.verein.service.NotFoundException;
import com.acme.verein.service.VereinReadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import static com.acme.verein.rest.VereinGetController.REST_PATH;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.ok;

/**
 * Eine @RestController-Klasse bildet die REST-Schnittstelle, wobei die HTTP-Methoden, Pfade und MIME-Typen auf die
 * Methoden der Klasse abgebildet werden.
 * <img src="../../../../../asciidoc/VereinGetController.svg" alt="Klassendiagramm">
 *
 * @author <a href="mailto:Juergen.Zimmermann@h-ka.de">Jürgen Zimmermann</a>
 */
@RestController
@RequestMapping(REST_PATH)
@Tag(name = "Verein API")
@RequiredArgsConstructor
@Slf4j
final class VereinGetController {
    /**
     * Muster für eine UUID. `$HEX_PATTERN{8}-($HEX_PATTERN{4}-){3}$HEX_PATTERN{12}` enthält eine _capturing group_
     * und ist nicht zulässig.
     */
    static final String ID_PATTERN =
        "[\\dA-Fa-f]{8}-[\\dA-Fa-f]{4}-[\\dA-Fa-f]{4}-[\\dA-Fa-f]{4}-[\\dA-Fa-f]{12}";
    @SuppressWarnings("TrainingComment")
    static final String REST_PATH = "/rest";


    //static final String NACHNAME_PATH = "/nachname"; //NOSONAR
    private final VereinReadService service;

    // https://docs.spring.io/spring-framework/docs/current/reference/html/web-reactive.html#webflux-ann-methods
    // https://localhost:8080/swagger-ui.html


    @GetMapping(path = "{id:" + ID_PATTERN + "}", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Suche mit der Verein-ID", tags = "Suchen")
    @ApiResponse(responseCode = "200", description = "Verein gefunden")
    @ApiResponse(responseCode = "404", description = "Verein nicht gefunden")
    Verein findById(@PathVariable final UUID id) {
        log.debug("findByID: id={}", id);

        // Anwendungskern
        final var verein = service.findById(id);
        log.debug("findByID: {}", verein);

        return verein;
    }


    @GetMapping(path = "findAll", produces = APPLICATION_JSON_VALUE)
    ResponseEntity<Collection<Verein>> findAll() {
        final var vereine = service.findAll();
        return ok(vereine);
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Suche mit Suchkriterien", tags = "Suchen")
    @ApiResponse(responseCode = "200", description = "CollectionModel mit den Vereinen")
    @ApiResponse(responseCode = "404", description = "Keine Vereine gefunden")
    Collection<Verein> find(
        @RequestParam final Map<String, String> suchkriterien
    ) {
        log.debug("find: suchkriterien={}", suchkriterien);

        final var models = service.find(suchkriterien);

        log.debug("find: {}", models);
        return models;
    }

    @ExceptionHandler
    @ResponseStatus(NOT_FOUND)
    void onNotFound(final NotFoundException ex) {
        log.debug("handleNotFound: {}", ex.getMessage());
    }

}
