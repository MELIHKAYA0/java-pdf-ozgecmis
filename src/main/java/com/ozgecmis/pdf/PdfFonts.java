package com.ozgecmis.pdf;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import java.io.IOException;
import java.io.InputStream;

/**
 * PDF'e gömülen yazı tiplerini (font) yükler ve bir arada tutar.
 *
 * <p>PDFBox'ın hazır Standard 14 fontları (Helvetica vb.) "ğ, ş, ı, İ" gibi Türkçe harfleri
 * desteklemez. Bu yüzden Türkçe karakter içeren Source Sans Pro (TTF) fontu
 * {@link PDType0Font} ile PDF'e gömülür. Yalnızca kullanılan harfler gömüldüğü (subset)
 * için dosya boyutu küçük kalır.</p>
 */
final class PdfFonts {

    private static final String FONT_DIR = "/fonts/";

    private final PDFont regular;
    private final PDFont semibold;
    private final PDFont bold;
    private final PDFont italic;

    private PdfFonts(PDFont regular, PDFont semibold, PDFont bold, PDFont italic) {
        this.regular = regular;
        this.semibold = semibold;
        this.bold = bold;
        this.italic = italic;
    }

    /** Fontları verilen belgeye bağlı olarak yükler (fabrika metodu). */
    static PdfFonts load(PDDocument document) throws IOException {
        return new PdfFonts(
                loadFont(document, "SourceSansPro-Regular.ttf"),
                loadFont(document, "SourceSansPro-Semibold.ttf"),
                loadFont(document, "SourceSansPro-Bold.ttf"),
                loadFont(document, "SourceSansPro-It.ttf"));
    }

    private static PDFont loadFont(PDDocument document, String fileName) throws IOException {
        String path = FONT_DIR + fileName;
        // try-with-resources: okuma bitince InputStream otomatik kapatılır
        try (InputStream in = PdfFonts.class.getResourceAsStream(path)) {
            if (in == null) {
                throw new IOException("Font dosyası bulunamadı: " + path);
            }
            return PDType0Font.load(document, in);
        }
    }

    PDFont regular() {
        return regular;
    }

    PDFont semibold() {
        return semibold;
    }

    PDFont bold() {
        return bold;
    }

    PDFont italic() {
        return italic;
    }
}
