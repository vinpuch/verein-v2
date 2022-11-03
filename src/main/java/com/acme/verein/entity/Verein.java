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
package com.acme.verein.entity;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
// import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
// import org.hibernate.validator.constraints.UniqueElements;

import java.net.URL;
import java.time.LocalDate;
// import java.util.List;
import java.util.UUID;

/**
 * Daten eines Vereins. In DDD ist Verein ist ein Aggregate Root.
 * <img src="../../../../../asciidoc/Verein.svg" alt="Klassendiagramm">
 *
 * @author <a href="mailto:Juergen.Zimmermann@h-ka.de">Jürgen Zimmermann</a>
 */
// https://thorben-janssen.com/java-records-hibernate-jpa
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Getter
@Setter
@ToString
@SuppressWarnings({"ClassFanOutComplexity", "JavadocDeclaration", "RequireEmptyLineBeforeBlockTagGroup"})
public class Verein {

    /**
     * Kleinster Wert für eine Kategorie.
     */
    public static final long MIN_KATEGORIE = 0L;

    /**
     * Maximaler Wert für eine Kategorie.
     */
    public static final long MAX_KATEGORIE = 9L;

    /**
     * Die ID des Vereins.
     * @param id Die ID.
     * @return Die ID.
     */
    @EqualsAndHashCode.Include
    private UUID id;

    /**
     * Der Name des Vereins.
     * @param name Der name.
     * @return Der name.
     */
    @NotEmpty

    private String name;

    /**
     * Die Emailadresse des Vereins.
     * @param email Die Emailadresse.
     * @return Die Emailadresse.
     */
    @Email
    @NotNull
    private String email;



    /**
     * Das Gruendungsdatum des Vereins.
     * @param gruendungsdatum Das Geburtsdatum.
     * @return Das Geburtsdatum.
     */
    @Past
    private LocalDate gruendungssdatum;

    /**
     * Die URL zur Homepage des Vereins.
     * @param homepage Die URL zur Homepage.
     * @return Die URL zur Homepage.
     */
    private URL homepage;


    /**
     * Der Umsatz des Vereins.
     * @param umsatz Der Umsatz.
     * @return Der Umsatz.
     */
    @ToString.Exclude
    private Umsatz umsatz;

    /**
     * Die Adresse des Vereins.
     * @param adresse Die Adresse.
     * @return Die Adresse.
     */
    @Valid
    @ToString.Exclude
    private Adresse adresse;
}
