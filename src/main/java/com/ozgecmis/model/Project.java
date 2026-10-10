package com.ozgecmis.model;

import java.util.Objects;

/** Özgeçmişte listelenen kişisel veya okul projesi. */
public final class Project {

    private final String name;
    private final String technologies;
    private final String description;

    public Project(String name, String technologies, String description) {
        this.name = Objects.requireNonNull(name, "name");
        this.technologies = Objects.requireNonNull(technologies, "technologies");
        this.description = Objects.requireNonNull(description, "description");
    }

    public String getName() {
        return name;
    }

    public String getTechnologies() {
        return technologies;
    }

    public String getDescription() {
        return description;
    }
}
