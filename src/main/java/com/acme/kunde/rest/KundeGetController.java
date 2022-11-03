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
package com.acme.kunde.rest;

import com.acme.kunde.entity.Kunde;
import com.acme.kunde.service.KundeReadService;
import com.acme.kunde.service.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
//import org.springframework.hateoas.CollectionModel;
//import org.springframework.hateoas.Link;
//import org.springframework.hateoas.LinkRelation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//import java.util.Map;
import java.util.Collection;
import java.util.UUID;
//import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

/**
 * Eine @RestController-Klasse bildet die REST-Schnittstelle, wobei die HTTP-Methoden, Pfade und MIME-Typen auf die
 * Methoden der Klasse abgebildet werden.
 * <img src="../../../../../asciidoc/KundeGetController.svg" alt="Klassendiagramm">
 *
 * @author <a href="mailto:Juergen.Zimmermann@h-ka.de">Jürgen Zimmermann</a>
 */
@RestController
@RequestMapping("/")
@Tag(name = "Kunde API")
@RequiredArgsConstructor
@Slf4j
final class KundeGetController {
    /**
     * Muster für eine UUID. `$HEX_PATTERN{8}-($HEX_PATTERN{4}-){3}$HEX_PATTERN{12}` enthält eine _capturing group_
     * und ist nicht zulässig.
     */
    static final String ID_PATTERN =
        "[\\dA-Fa-f]{8}-[\\dA-Fa-f]{4}-[\\dA-Fa-f]{4}-[\\dA-Fa-f]{4}-[\\dA-Fa-f]{12}";

    /**
     * Pfad, um Nachnamen abzufragen.
     */
    @SuppressWarnings("TrailingComment")
    //static final String NACHNAME_PATH = "/nachname"; //NOSONAR

    private final KundeReadService service;

    // https://docs.spring.io/spring-framework/docs/current/reference/html/web-reactive.html#webflux-ann-methods
    // https://localhost:8080/swagger-ui.html

    /**
     * Suche anhand der Kunde-ID als Pfad-Parameter.
     *
     * @param id      ID des zu suchenden Kunden
     * @param request Das Request-Objekt, um Links für HATEOAS zu erstellen.
     * @return Eine Response mit dem Statuscode 200 und dem gefundenen Kunden mit Atom-Links oder Statuscode 404.
     */
    @GetMapping(path = "{id:" + ID_PATTERN + "}", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "Suche mit der Kunde-ID", tags = "Suchen")
    @ApiResponse(responseCode = "200", description = "Kunde gefunden")
    @ApiResponse(responseCode = "404", description = "Kunde nicht gefunden")
    ResponseEntity<Kunde> findById(@PathVariable final UUID id, final HttpServletRequest request) {
        log.debug("findById: id={}", id);

        // Anwendungskern
        final var kunde = service.findById(id);
        log.debug("findById: {}", kunde);

        return ok(kunde);
    }

    @ExceptionHandler(NotFoundException.class)
    @SuppressWarnings("unused")
    ResponseEntity<Void> handleNotFound(final NotFoundException ex) {
        log.debug("handleNotFound: {}", ex.getMessage());
        return notFound().build();

    }


    @GetMapping(produces = APPLICATION_JSON_VALUE)
    ResponseEntity<Collection<Kunde>> findAll() {
        final var kunden = service.findAll();
        return ok(kunden);
    }
}
