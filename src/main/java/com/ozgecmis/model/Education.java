package com.ozgecmis.model;

import java.util.Objects;

/** Bir eğitim kaydını (bölüm, okul, yıllar, ek bilgi) temsil eder. */
public final class Education {

    private final String degree;
    private final String school;
    private final String period;
    private final String detail;

    public Education(String degree, String school, String period, String detail) {
        this.degree = Objects.requireNonNull(degree, "degree");
        this.school = Objects.requireNonNull(school, "school");
        this.period = Objects.requireNonNull(period, "period");
        this.detail = detail;
    }

    public String getDegree() {
        return degree;
    }

    public String getSchool() {
        return school;
    }

    public String getPeriod() {
        return period;
    }

    /** Not ortalaması, bitirme projesi gibi ek bilgi; yoksa {@code null}. */
    public String getDetail() {
        return detail;
    }
}
