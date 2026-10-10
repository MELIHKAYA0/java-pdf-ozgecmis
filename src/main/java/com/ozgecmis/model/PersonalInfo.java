package com.ozgecmis.model;

import java.util.Objects;

/**
 * Özgeçmiş sahibinin kişisel ve iletişim bilgilerini tutar.
 *
 * <p>Sınıf {@code final}, alanlar {@code private final}: nesne oluşturulduktan sonra
 * değiştirilemez (immutable). Dışarıya yalnızca okuma (getter) metotları açılır.</p>
 */
public final class PersonalInfo {

    private final String fullName;
    private final String title;
    private final String summary;
    private final String phone;
    private final String email;
    private final String location;
    private final String github;
    private final String linkedin;
    private final String photoResource;

    public PersonalInfo(String fullName, String title, String summary,
                        String phone, String email, String location,
                        String github, String linkedin, String photoResource) {
        this.fullName = requireText(fullName, "fullName");
        this.title = requireText(title, "title");
        this.summary = requireText(summary, "summary");
        this.phone = phone;
        this.email = email;
        this.location = location;
        this.github = github;
        this.linkedin = linkedin;
        this.photoResource = requireText(photoResource, "photoResource");
    }

    /** Sadece bu sınıf içinde kullanılan doğrulama yardımcısı. */
    private static String requireText(String value, String fieldName) {
        Objects.requireNonNull(value, fieldName + " boş olamaz");
        if (value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " boş olamaz");
        }
        return value;
    }

    public String getFullName() {
        return fullName;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getLocation() {
        return location;
    }

    public String getGithub() {
        return github;
    }

    public String getLinkedin() {
        return linkedin;
    }

    /** Fotoğrafın classpath içindeki yolu, örn. {@code /images/profil.jpg}. */
    public String getPhotoResource() {
        return photoResource;
    }
}
