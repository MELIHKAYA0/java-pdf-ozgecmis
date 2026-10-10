package com.ozgecmis.pdf;

import org.apache.pdfbox.pdmodel.font.PDFont;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Metin ölçme ve satırlara bölme yardımcısı.
 *
 * <p>PDF'te metin kendiliğinden alt satıra geçmez; her satırın konumunu programın
 * hesaplaması gerekir. Bu sınıf bir metni, verilen genişliğe sığacak şekilde
 * kelime kelime satırlara böler.</p>
 */
final class TextWrapper {

    private TextWrapper() {
    }

    /** Metnin punto cinsinden genişliği. Font ölçüleri 1000 birimlik kareye göre verilir. */
    static float width(String text, PDFont font, float fontSize) throws IOException {
        return font.getStringWidth(text) / 1000f * fontSize;
    }

    static List<String> wrap(String text, PDFont font, float fontSize, float maxWidth) throws IOException {
        List<String> lines = new ArrayList<>();
        StringBuilder line = new StringBuilder();

        for (String word : text.trim().split("\\s+")) {
            String candidate = line.length() == 0 ? word : line + " " + word;
            if (width(candidate, font, fontSize) <= maxWidth) {
                line.setLength(0);
                line.append(candidate);
                continue;
            }
            if (line.length() > 0) {
                lines.add(line.toString());
                line.setLength(0);
            }
            // Tek başına satıra sığmayan çok uzun kelimeler (ör. bağlantılar) harf harf bölünür
            while (width(word, font, fontSize) > maxWidth) {
                int cut = fittingLength(word, font, fontSize, maxWidth);
                lines.add(word.substring(0, cut));
                word = word.substring(cut);
            }
            line.append(word);
        }
        if (line.length() > 0) {
            lines.add(line.toString());
        }
        return lines;
    }

    /**
     * Kısa öğeleri (ör. yetenekler) araya ayraç koyarak satırlara dizer. {@link #wrap} metodundan
     * farkı, "IntelliJ IDEA" gibi bir öğeyi asla iki satıra bölmemesidir.
     */
    static List<String> wrapItems(List<String> items, String separator, PDFont font, float fontSize,
                                  float maxWidth) throws IOException {
        List<String> lines = new ArrayList<>();
        String line = "";
        for (String item : items) {
            String candidate = line.isEmpty() ? item : line + separator + item;
            if (line.isEmpty() || width(candidate, font, fontSize) <= maxWidth) {
                line = candidate;
            } else {
                lines.add(line);
                line = item;
            }
        }
        if (!line.isEmpty()) {
            lines.add(line);
        }
        return lines;
    }

    private static int fittingLength(String word, PDFont font, float fontSize, float maxWidth) throws IOException {
        int length = 1;
        while (length < word.length() && width(word.substring(0, length + 1), font, fontSize) <= maxWidth) {
            length++;
        }
        return length;
    }
}
