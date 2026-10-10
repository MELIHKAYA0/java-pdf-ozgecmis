package com.ozgecmis.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Özgeçmişin tamamını bir arada tutan ana model sınıfı.
 *
 * <p>Çok sayıda alanı olduğu için nesne <b>Builder</b> deseni ile oluşturulur:
 * constructor {@code private} olduğundan dışarıdan {@code new Resume(...)} yazılamaz,
 * nesne yalnızca {@link #builder(PersonalInfo)} → {@link Builder#build()} zinciriyle üretilir.</p>
 *
 * <pre>
 * Resume resume = Resume.builder(info)
 *         .addExperience(...)
 *         .addEducation(...)
 *         .build();
 * </pre>
 */
public final class Resume {

    private final PersonalInfo personalInfo;
    private final List<Experience> experiences;
    private final List<Education> educations;
    private final List<SkillGroup> skillGroups;
    private final List<Language> languages;
    private final List<Project> projects;
    private final List<String> certificates;

    /** Sadece Builder çağırabilir; değiştirilemez kopyalar alınır. */
    private Resume(Builder builder) {
        this.personalInfo = builder.personalInfo;
        this.experiences = List.copyOf(builder.experiences);
        this.educations = List.copyOf(builder.educations);
        this.skillGroups = List.copyOf(builder.skillGroups);
        this.languages = List.copyOf(builder.languages);
        this.projects = List.copyOf(builder.projects);
        this.certificates = List.copyOf(builder.certificates);
    }

    public static Builder builder(PersonalInfo personalInfo) {
        return new Builder(personalInfo);
    }

    public PersonalInfo getPersonalInfo() {
        return personalInfo;
    }

    public List<Experience> getExperiences() {
        return experiences;
    }

    public List<Education> getEducations() {
        return educations;
    }

    public List<SkillGroup> getSkillGroups() {
        return skillGroups;
    }

    public List<Language> getLanguages() {
        return languages;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public List<String> getCertificates() {
        return certificates;
    }

    /**
     * Resume nesnesini adım adım kuran yardımcı sınıf.
     * {@code static} iç sınıf olduğu için bir Resume nesnesine ihtiyaç duymadan oluşturulabilir.
     */
    public static final class Builder {

        private final PersonalInfo personalInfo;
        private final List<Experience> experiences = new ArrayList<>();
        private final List<Education> educations = new ArrayList<>();
        private final List<SkillGroup> skillGroups = new ArrayList<>();
        private final List<Language> languages = new ArrayList<>();
        private final List<Project> projects = new ArrayList<>();
        private final List<String> certificates = new ArrayList<>();

        private Builder(PersonalInfo personalInfo) {
            this.personalInfo = Objects.requireNonNull(personalInfo, "personalInfo");
        }

        public Builder addExperience(Experience experience) {
            experiences.add(Objects.requireNonNull(experience));
            return this;
        }

        public Builder addEducation(Education education) {
            educations.add(Objects.requireNonNull(education));
            return this;
        }

        public Builder addSkillGroup(SkillGroup skillGroup) {
            skillGroups.add(Objects.requireNonNull(skillGroup));
            return this;
        }

        public Builder addLanguage(Language language) {
            languages.add(Objects.requireNonNull(language));
            return this;
        }

        public Builder addProject(Project project) {
            projects.add(Objects.requireNonNull(project));
            return this;
        }

        public Builder addCertificate(String certificate) {
            certificates.add(Objects.requireNonNull(certificate));
            return this;
        }

        /** Zorunlu kuralları kontrol eder ve değiştirilemez Resume nesnesini üretir. */
        public Resume build() {
            if (experiences.isEmpty()) {
                throw new IllegalStateException("Özgeçmişte en az bir iş deneyimi olmalı");
            }
            if (educations.isEmpty()) {
                throw new IllegalStateException("Özgeçmişte en az bir eğitim bilgisi olmalı");
            }
            return new Resume(this);
        }
    }
}
