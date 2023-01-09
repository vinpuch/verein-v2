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

import com.acme.verein.service.NotFoundException;
import com.acme.verein.service.VereinReadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;

import static io.micrometer.core.instrument.binder.http.HttpRequestTags.status;
import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_MODIFIED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.status;

/**
 * Eine @RestController-Klasse bildet die REST-Schnittstelle, wobei die HTTP-Methoden, Pfade und MIME-Typen auf die
 * Methoden der Klasse abgebildet werden.
 * <img src="../../../../../asciidoc/VereinGetController.svg" alt="Klassendiagramm">
 *
 * @author <a href="mailto:Juergen.Zimmermann@h-ka.de">Jürgen Zimmermann</a>
 */
@RestController
@RequestMapping("/rest")
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

    /**
     * Pfad, um Namen abzufragen.
     */
    @SuppressWarnings("TrailingComment")
    private static final String NAME_PATH = "/name"; //NOSONAR
    private final VereinReadService service;
    private final UriHelper uriHelper;


    // https://docs.spring.io/spring-framework/docs/current/reference/html/web-reactive.html#webflux-ann-methods
    // https://localhost:8080/swagger-ui.html


    @GetMapping(path = "{id:" + ID_PATTERN + "}", produces = HAL_JSON_VALUE)
    @Operation(summary = "Suche mit der Verein-ID", tags = "Suchen")
    @ApiResponse(responseCode = "200", description = "Verein gefunden")
    @ApiResponse(responseCode = "404", description = "Verein nicht gefunden")
    VereinModel findById( @PathVariable final UUID id,
                          @RequestHeader("If-None-Match") final Optional<String> version,
                          final HttpServletRequest request) {

        final var verein = service.findById(id, user);
        log.debug("findById: {}", verein);

        final var currentVersion = "\"" + verein.getVersion() + '"';
        if (Objects.equals(version.orElse(null), currentVersion)) {
            return status(NOT_MODIFIED).build();
        }
        log.debug("findById: id={}", id);

        // Geschaeftslogik bzw. Anwendungskern

        return ok().eTag(currentVersion).body(model);



        // HATEOAS
        final var model = new VereinModel(verein);
        // evtl. Forwarding von einem API-Gateway
        final var baseUri = uriHelper.getBaseUri(request).toString();
        final var idUri = baseUri + '/' + verein.getId();
        final var selfLink = Link.of(idUri);
        final var listLink = Link.of(baseUri, LinkRelation.of("list"));
        final var addLink = Link.of(baseUri, LinkRelation.of("add"));
        final var updateLink = Link.of(idUri, LinkRelation.of("update"));
        model.add(selfLink, listLink, addLink, updateLink);

        log.debug("findById: {}", model);
        return model;

    }



    @GetMapping(produces = HAL_JSON_VALUE)
    @Operation(summary = "Suche mit Suchkriterien", tags = "Suchen")
    @ApiResponse(responseCode = "200", description = "CollectionModel mit den Vereinen")
    @ApiResponse(responseCode = "404", description = "Keine Vereine gefunden")
    CollectionModel<? extends VereinModel> find(
        @RequestParam final Map<String, String> suchkriterien,
        final HttpServletRequest request
    ) {
        log.debug("find: suchkriterien={}", suchkriterien);
        final var baseUri = uriHelper.getBaseUri(request).toString();

        //Geschäftslogik
        final var models = service.find(suchkriterien)
            .stream()
            .map(verein -> {
                final var model = new VereinModel(verein);
                final var idUri = baseUri + '/' + verein.getId();
                final var selfLink = Link.of(idUri);
                final var listLink = Link.of(baseUri, LinkRelation.of("list"));
                final var addLink = Link.of(baseUri, LinkRelation.of("add"));
                final var updateLink = Link.of(idUri, LinkRelation.of("update"));
                model.add(selfLink, listLink, addLink, updateLink);
                return model;
            }).toList();

        log.debug("find: {}", models);
        return CollectionModel.of(models);
    }
    /**
     * Abfrage, welche Namen es zu einem Präfix gibt.
     *
     * @param prefix Name-Präfix als Pfadvariable.
     * @return Die passenden Namen oder Statuscode 404, falls es keine gibt.
     */
    @GetMapping(path = NAME_PATH + "/{prefix}", produces = APPLICATION_JSON_VALUE)
    String findNamenByPrefix(@PathVariable final String prefix) {
        log.debug("findNamenByPrefix: {}", prefix);
        final var namen = service.findNamenByPrefix(prefix);
        log.debug("findNamenByPrefix: {}", namen);
        return namen.toString();
    }
    @ExceptionHandler
    @ResponseStatus(NOT_FOUND)
    void onNotFound(final NotFoundException ex) {
        log.debug("handleNotFound: {}", ex.getMessage());
    }

}
