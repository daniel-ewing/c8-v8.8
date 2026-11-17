package org.example.model;

import java.time.LocalDate;

public record PersonInformation(
        String firstName,
        String lastName,
        LocalDate dateOfBirth
) {}