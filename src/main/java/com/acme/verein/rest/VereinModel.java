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

import com.acme.verein.entity.Adresse;
import com.acme.verein.entity.Verein;
import com.acme.verein.entity.Umsatz;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;


/**
 * Model-Klasse für Spring HATEOAS. @lombok.Data fasst die Annotationsn @ToString, @EqualsAndHashCode, @Getter, @Setter
 * und @RequiredArgsConstructor zusammen.
 *
 * @author <a href="mailto:Juergen.Zimmermann@h-ka.de">Jürgen Zimmermann</a>
 */
@JsonPropertyOrder({
    "nachname", "email", "gruendungsdatum", "homepage",
     "umsatz", "adresse"
})
@Relation(collectionRelation = "verein", itemRelation = "verein")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Getter
@Setter
@ToString(callSuper = true)
final class VereinModel extends RepresentationModel<VereinModel> {
    private final String name;

    @EqualsAndHashCode.Include
    private final String email;
    private final LocalDate gruendungssdatum;
    private final URL homepage;
    private final Umsatz umsatz;
    private final Adresse adresse;

    VereinModel(final Verein verein) {
        name = verein.getName();
        email = verein.getEmail();
        gruendungssdatum = verein.getGruendungsdatum();
        homepage = verein.getHomepage();
        umsatz = verein.getUmsatz();
        adresse = verein.getAdresse();
    }
}
