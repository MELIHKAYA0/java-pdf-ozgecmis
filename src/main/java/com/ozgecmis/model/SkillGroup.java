package com.ozgecmis.model;

import java.util.List;
import java.util.Objects;

/** Aynı başlık altında toplanan yetenekler, örn. "Programlama: Java, SQL, Python". */
public final class SkillGroup {

    private final String name;
    private final List<String> skills;

    public SkillGroup(String name, List<String> skills) {
        this.name = Objects.requireNonNull(name, "name");
        this.skills = List.copyOf(skills);
    }

    public String getName() {
        return name;
    }

    public List<String> getSkills() {
        return skills;
    }
}
