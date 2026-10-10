package com.ozgecmis.data;

import com.ozgecmis.model.Education;
import com.ozgecmis.model.Experience;
import com.ozgecmis.model.Language;
import com.ozgecmis.model.PersonalInfo;
import com.ozgecmis.model.Project;
import com.ozgecmis.model.Resume;
import com.ozgecmis.model.SkillGroup;

import java.util.List;

/**
 * Özgeçmişte kullanılan verileri üretir.
 *
 * <p>Ödev yönergesine göre kişisel bilgilerin birebir gerçek olması gerekmez; iletişim
 * bilgileri, iş deneyimleri ve sertifikalar hayalidir. Bilgileri değiştirmek için yalnızca
 * bu dosyayı düzenlemek yeterlidir; PDF çizim kodu verilerden tamamen bağımsızdır.</p>
 *
 * <p>Bu bir yardımcı (utility) sınıftır: constructor {@code private} olduğu için
 * nesnesi oluşturulamaz, metoduna sınıf adıyla erişilir: {@code SampleResumeData.create()}.</p>
 */
public final class SampleResumeData {

    private SampleResumeData() {
        // nesne oluşturulmasını engeller
    }

    public static Resume create() {
        PersonalInfo info = new PersonalInfo(
                "Melih Kaya",
                "Yazılım Mühendisliği Öğrencisi",
                "Kırklareli Üniversitesi Yazılım Mühendisliği 2. sınıf öğrencisiyim. Java ve nesne "
                        + "yönelimli programlama başta olmak üzere backend geliştirmeye ilgi duyuyorum. "
                        + "Staj ve yarı zamanlı çalışmalarımda REST servisleri, veritabanı sorguları ve "
                        + "birim testleri üzerinde deneyim kazandım. Temiz ve okunabilir kod yazmayı, "
                        + "yeni teknolojiler öğrenmeyi ve ekip içinde çalışmayı seviyorum.",
                "+90 555 123 45 67",
                "melih.kaya@example.com",
                "Kırklareli, Türkiye",
                null,
                null,
                "/images/profil.jpg");

        return Resume.builder(info)
                // ---- 3 hayali iş deneyimi (en yeniden eskiye) ----
                .addExperience(new Experience(
                        "Yarı Zamanlı Java Geliştirici",
                        "Trakya Bilişim Teknolojileri",
                        "Kırklareli",
                        "Eylül 2026",
                        "Günümüz",
                        List.of(
                                "Spring Boot ile geliştirilen stok takip uygulamasına yeni REST uç noktaları ekliyor.",
                                "JUnit 5 ile birim testler yazarak hataların erken yakalanmasına katkı sağlıyor.",
                                "Kod incelemelerine katılıyor, Git ve GitHub iş akışıyla ekip içinde çalışıyor.")))
                .addExperience(new Experience(
                        "Yazılım Geliştirme Stajyeri",
                        "Atlas Yazılım A.Ş.",
                        "İstanbul",
                        "Temmuz 2026",
                        "Ağustos 2026",
                        List.of(
                                "Müşteri yönetim sistemi için Java ile aylık satış raporu modülü geliştirdi.",
                                "PostgreSQL veritabanı için veri doğrulama ve raporlama sorguları yazdı.",
                                "Geliştirdiği modülün teknik dokümantasyonunu ve kullanım kılavuzunu hazırladı.")))
                .addExperience(new Experience(
                        "Serbest Web Geliştirici",
                        "Serbest Çalışma",
                        "Uzaktan",
                        "Şubat 2026",
                        "Haziran 2026",
                        List.of(
                                "Küçük işletmeler için HTML, CSS ve JavaScript ile 4 kurumsal web sitesi hazırladı.",
                                "Sitelerin mobil uyumluluğunu sağladı ve sayfa açılış hızlarını iyileştirdi.",
                                "Müşterilerle görüşerek gereksinimleri belirledi, projeleri zamanında teslim etti.")))

                // ---- Eğitim ----
                .addEducation(new Education(
                        "Yazılım Mühendisliği (Lisans)",
                        "Kırklareli Üniversitesi",
                        "2025 – Devam ediyor",
                        "2. sınıf · İlgili dersler: Nesne Yönelimli Programlama, Veri Yapıları, Veritabanı Sistemleri"))

                // ---- Projeler ----
                .addProject(new Project(
                        "PDF Özgeçmiş Oluşturucu",
                        "Java 17 · Apache PDFBox · Maven",
                        "Model verilerinden fotoğraflı, A4 boyutunda ve Türkçe karakter destekli PDF "
                                + "özgeçmiş üreten uygulama (bu proje)."))
                .addProject(new Project(
                        "Kütüphane Yönetim Sistemi",
                        "Java · MySQL",
                        "Nesne yönelimli programlama dersi kapsamında kitap ödünç alma, iade ve üye takibi "
                                + "yapan konsol uygulaması."))
                .addProject(new Project(
                        "Tarif Sitesi",
                        "HTML · CSS · JavaScript",
                        "Kategorilere göre yemek tariflerinin listelendiği, mobil uyumlu statik web sitesi."))

                // ---- Kenar çubuğu bilgileri ----
                .addSkillGroup(new SkillGroup("Programlama",
                        List.of("Java", "C", "Python", "SQL")))
                .addSkillGroup(new SkillGroup("Web",
                        List.of("HTML", "CSS", "JavaScript")))
                .addSkillGroup(new SkillGroup("Kütüphaneler",
                        List.of("Spring Boot", "JUnit 5", "Apache PDFBox")))
                .addSkillGroup(new SkillGroup("Araçlar",
                        List.of("Git", "GitHub", "Maven", "IntelliJ IDEA", "Cursor")))
                .addLanguage(new Language("Türkçe", "Ana dil", 5))
                .addLanguage(new Language("İngilizce", "B1", 3))
                .addLanguage(new Language("Almanca", "A1", 1))
                .addCertificate("Java ile Nesne Yönelimli Programlama (2025)")
                .addCertificate("Git ve GitHub Temelleri (2026)")
                .build();
    }
}
