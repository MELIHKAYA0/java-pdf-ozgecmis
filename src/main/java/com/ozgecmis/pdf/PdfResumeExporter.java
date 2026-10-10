package com.ozgecmis.pdf;

import com.ozgecmis.export.ResumeExporter;
import com.ozgecmis.model.PersonalInfo;
import com.ozgecmis.model.Resume;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Calendar;
import java.util.Objects;

/**
 * Bir {@link Resume} nesnesinden A4 boyutunda PDF özgeçmiş üretir.
 *
 * <p>PDF oluşturma işinin tamamı bu sınıftan başlar; {@code main} metodu yalnızca
 * {@link #export(Resume, Path)} metodunu çağırır.</p>
 *
 * <h2>Nesne yaşam döngüsü</h2>
 * <ol>
 *   <li>{@link PDDocument} try-with-resources ile oluşturulur.</li>
 *   <li>Fontlar ve fotoğraf bu belgeye bağlı olarak yüklenir.</li>
 *   <li>{@link PageWriter} sayfaları ve içerik akışlarını açar; blok bitince akış kapanır.</li>
 *   <li>Belge bir dosya akışına ({@link OutputStream}) kaydedilir, akış kapatılır.</li>
 *   <li>Blok bitince {@code PDDocument.close()} otomatik çağrılır ve kullanılan bellek
 *       serbest bırakılır; nesneler artık kullanılmadığı için çöp toplayıcı (GC) temizleyebilir.</li>
 * </ol>
 */
public final class PdfResumeExporter implements ResumeExporter {

    @Override
    public void export(Resume resume, Path output) throws IOException {
        Objects.requireNonNull(resume, "resume");
        Objects.requireNonNull(output, "output");

        Path parent = output.toAbsolutePath().getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }

        try (PDDocument document = new PDDocument()) {
            fillMetadata(document, resume.getPersonalInfo());

            PdfFonts fonts = PdfFonts.load(document);
            PDImageXObject photo = loadPhoto(document, resume.getPersonalInfo().getPhotoResource());

            try (PageWriter writer = new PageWriter(document)) {
                writer.newPage();
                new SidebarRenderer(writer, fonts).render(resume, photo);
                new MainContentRenderer(writer, fonts).render(resume);
            } // içerik akışı (PDPageContentStream) burada kapanır

            try (OutputStream out = new BufferedOutputStream(Files.newOutputStream(output))) {
                document.save(out);
            } // dosya akışı burada kapanır
        } // PDDocument burada kapanır
    }

    /** PDF dosyasının "Özellikler" penceresinde görünen bilgiler. */
    private void fillMetadata(PDDocument document, PersonalInfo info) {
        PDDocumentInformation metadata = document.getDocumentInformation();
        metadata.setTitle(info.getFullName() + " - Özgeçmiş");
        metadata.setAuthor(info.getFullName());
        metadata.setSubject("Özgeçmiş (CV)");
        metadata.setCreator("Java PDF Özgeçmiş Oluşturucu");
        metadata.setCreationDate(Calendar.getInstance());
    }

    /** Fotoğrafı classpath'ten (src/main/resources) okur. JPG ve PNG desteklenir. */
    private PDImageXObject loadPhoto(PDDocument document, String resourcePath) throws IOException {
        try (InputStream in = PdfResumeExporter.class.getResourceAsStream(resourcePath)) {
            if (in == null) {
                throw new IOException("Fotoğraf bulunamadı: " + resourcePath
                        + " (dosyayı src/main/resources altına koyun)");
            }
            return PDImageXObject.createFromByteArray(document, in.readAllBytes(), "profil-fotografi");
        }
    }
}
