package com.ozgecmis;

import com.ozgecmis.data.SampleResumeData;
import com.ozgecmis.export.ResumeExporter;
import com.ozgecmis.model.Resume;
import com.ozgecmis.pdf.PdfResumeExporter;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Uygulamanın giriş noktası.
 *
 * <p>Main yalnızca nesneleri oluşturur ve işi ilgili sınıfa devreder; PDF ile ilgili hiçbir
 * çizim kodu burada yoktur. PDF üretimi {@link PdfResumeExporter} sınıfında yapılır.</p>
 *
 * <p>Kullanım: {@code java com.ozgecmis.Main [cikti-dosyasi.pdf]}</p>
 */
public class Main {

    private static final String DEFAULT_OUTPUT = "ozgecmis.pdf";

    public static void main(String[] args) {
        Path output = Path.of(args.length > 0 ? args[0] : DEFAULT_OUTPUT);

        Resume resume = SampleResumeData.create();
        ResumeExporter exporter = new PdfResumeExporter();

        try {
            exporter.export(resume, output);
            System.out.println("Özgeçmiş oluşturuldu: " + output.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("PDF oluşturulamadı: " + e.getMessage());
            System.exit(1);
        }
    }
}
