package com.ozgecmis.pdf;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.awt.Color;
import java.io.IOException;

/**
 * Sayfa ekleme ve çizim işlemlerini tek yerde toplayan sınıf.
 *
 * <p>PDFBox'ta bir sayfaya çizim yapmak için {@link PDPageContentStream} açılır ve iş bitince
 * mutlaka kapatılır; kapatılmazsa sayfa içeriği PDF'e yazılmaz. Bu sınıf
 * {@link AutoCloseable} arayüzünü uygular, böylece try-with-resources bloğu bittiğinde
 * açık kalan akış otomatik olarak kapatılır.</p>
 */
final class PageWriter implements AutoCloseable {

    /** Bezier eğrileriyle daire çizmek için kullanılan sabit: 4 * (√2 - 1) / 3. */
    private static final float CIRCLE_KAPPA = 0.5522848f;

    private final PDDocument document;
    private PDPageContentStream stream;
    private int pageCount;

    PageWriter(PDDocument document) {
        this.document = document;
    }

    /** Belgeye yeni bir A4 sayfa ekler ve sol kenar çubuğunun arka planını boyar. */
    void newPage() throws IOException {
        closeStream();
        PDPage page = new PDPage(Theme.PAGE_SIZE);
        document.addPage(page);
        stream = new PDPageContentStream(document, page);
        pageCount++;
        fillRect(0, 0, Theme.SIDEBAR_WIDTH, Theme.PAGE_HEIGHT, Theme.SIDEBAR_BG);
    }

    int getPageCount() {
        return pageCount;
    }

    // ------------------------------------------------------------------ metin

    void text(String text, PDFont font, float size, Color color, float x, float y) throws IOException {
        text(text, font, size, color, x, y, 0f);
    }

    /** Sol alt köşesi (taban çizgisi) {@code (x, y)} noktasında olacak şekilde metin yazar. */
    void text(String text, PDFont font, float size, Color color, float x, float y, float charSpacing)
            throws IOException {
        stream.beginText();
        stream.setFont(font, size);
        stream.setNonStrokingColor(color);
        stream.setCharacterSpacing(charSpacing);
        stream.newLineAtOffset(x, y);
        stream.showText(text);
        stream.endText();
    }

    /** Metni sağ kenarı {@code rightX} olacak şekilde sağa hizalı yazar. */
    void textRight(String text, PDFont font, float size, Color color, float rightX, float y) throws IOException {
        float width = TextWrapper.width(text, font, size);
        text(text, font, size, color, rightX - width, y);
    }

    // ------------------------------------------------------------------ şekiller

    void fillRect(float x, float y, float width, float height, Color color) throws IOException {
        stream.setNonStrokingColor(color);
        stream.addRect(x, y, width, height);
        stream.fill();
    }

    void line(float x1, float y1, float x2, float y2, float lineWidth, Color color) throws IOException {
        stream.setStrokingColor(color);
        stream.setLineWidth(lineWidth);
        stream.moveTo(x1, y1);
        stream.lineTo(x2, y2);
        stream.stroke();
    }

    void fillCircle(float cx, float cy, float radius, Color color) throws IOException {
        stream.setNonStrokingColor(color);
        circlePath(cx, cy, radius);
        stream.fill();
    }

    void strokeCircle(float cx, float cy, float radius, float lineWidth, Color color) throws IOException {
        stream.setStrokingColor(color);
        stream.setLineWidth(lineWidth);
        circlePath(cx, cy, radius);
        stream.stroke();
    }

    /**
     * Resmi daire şeklinde kırparak çizer. Resim kare değilse ortalanır ve daireyi
     * tamamen dolduracak şekilde ölçeklenir.
     */
    void circularImage(PDImageXObject image, float cx, float cy, float radius) throws IOException {
        stream.saveGraphicsState();
        circlePath(cx, cy, radius);
        stream.clip();

        float diameter = radius * 2;
        float scale = Math.max(diameter / image.getWidth(), diameter / image.getHeight());
        float width = image.getWidth() * scale;
        float height = image.getHeight() * scale;
        stream.drawImage(image, cx - width / 2, cy - height / 2, width, height);

        stream.restoreGraphicsState();
    }

    private void circlePath(float cx, float cy, float r) throws IOException {
        float k = CIRCLE_KAPPA * r;
        stream.moveTo(cx + r, cy);
        stream.curveTo(cx + r, cy + k, cx + k, cy + r, cx, cy + r);
        stream.curveTo(cx - k, cy + r, cx - r, cy + k, cx - r, cy);
        stream.curveTo(cx - r, cy - k, cx - k, cy - r, cx, cy - r);
        stream.curveTo(cx + k, cy - r, cx + r, cy - k, cx + r, cy);
        stream.closePath();
    }

    // ------------------------------------------------------------------ yaşam döngüsü

    @Override
    public void close() throws IOException {
        closeStream();
    }

    private void closeStream() throws IOException {
        if (stream != null) {
            stream.close();
            stream = null;
        }
    }
}
