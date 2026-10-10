package com.ozgecmis.model;

import java.util.List;
import java.util.Objects;

/**
 * Tek bir iş deneyimini temsil eder (pozisyon, şirket, tarih aralığı ve başarılar).
 *
 * <p>Başarı listesi {@link List#copyOf} ile kopyalanır; böylece dışarıdan verilen liste
 * sonradan değiştirilse bile bu nesnenin içeriği bozulmaz.</p>
 */
public final class Experience {

    private final String position;
    private final String company;
    private final String location;
    private final String startDate;
    private final String endDate;
    private final List<String> highlights;

    public Experience(String position, String company, String location,
                      String startDate, String endDate, List<String> highlights) {
        this.position = Objects.requireNonNull(position, "position");
        this.company = Objects.requireNonNull(company, "company");
        this.location = location;
        this.startDate = Objects.requireNonNull(startDate, "startDate");
        this.endDate = Objects.requireNonNull(endDate, "endDate");
        this.highlights = List.copyOf(highlights);
    }

    public String getPosition() {
        return position;
    }

    public String getCompany() {
        return company;
    }

    public String getLocation() {
        return location;
    }

    /** "Mart 2024 – Günümüz" biçiminde tarih aralığı döndürür. */
    public String getPeriod() {
        return startDate + " – " + endDate;
    }

    public List<String> getHighlights() {
        return highlights;
    }
}
