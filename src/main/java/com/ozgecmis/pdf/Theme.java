package com.ozgecmis.pdf;

import org.apache.pdfbox.pdmodel.common.PDRectangle;

import java.awt.Color;
import java.util.Locale;

/**
 * Sayfa ölçüleri, kenar boşlukları ve renkler gibi tasarım sabitleri.
 *
 * <p>Sınıf ve alanlar <i>package-private</i> (erişim belirteci yazılmamış): yalnızca
 * {@code com.ozgecmis.pdf} paketindeki sınıflar görebilir, paket dışına kapalıdır.</p>
 */
final class Theme {

    private Theme() {
    }

    static final Locale TURKISH = Locale.forLanguageTag("tr-TR");

    // ---- Sayfa (A4 = 595 x 842 punto) ----
    static final PDRectangle PAGE_SIZE = PDRectangle.A4;
    static final float PAGE_WIDTH = PAGE_SIZE.getWidth();
    static final float PAGE_HEIGHT = PAGE_SIZE.getHeight();
    static final float TOP_MARGIN = 42f;
    static final float BOTTOM_MARGIN = 36f;

    // ---- Sol kenar çubuğu ----
    static final float SIDEBAR_WIDTH = 190f;
    static final float SIDEBAR_PADDING = 22f;

    // ---- Sağ ana sütun ----
    static final float MAIN_LEFT = SIDEBAR_WIDTH + 28f;
    static final float MAIN_RIGHT = PAGE_WIDTH - 32f;
    static final float MAIN_WIDTH = MAIN_RIGHT - MAIN_LEFT;

    // ---- Renkler ----
    static final Color SIDEBAR_BG = new Color(30, 41, 59);
    static final Color SIDEBAR_TEXT = new Color(241, 245, 249);
    static final Color SIDEBAR_SOFT = new Color(203, 213, 225);
    static final Color SIDEBAR_MUTED = new Color(148, 163, 184);
    static final Color SIDEBAR_RULE = new Color(71, 85, 105);
    static final Color SIDEBAR_ACCENT = new Color(125, 211, 252);

    static final Color ACCENT = new Color(3, 105, 161);
    static final Color HEADING = new Color(15, 23, 42);
    static final Color BODY = new Color(51, 65, 85);
    static final Color MUTED = new Color(100, 116, 139);
    static final Color RULE = new Color(203, 213, 225);
}
