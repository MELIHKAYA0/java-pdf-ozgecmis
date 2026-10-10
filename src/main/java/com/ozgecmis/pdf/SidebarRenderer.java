package com.ozgecmis.pdf;

import com.ozgecmis.model.Language;
import com.ozgecmis.model.PersonalInfo;
import com.ozgecmis.model.Resume;
import com.ozgecmis.model.SkillGroup;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.IOException;
import java.util.List;

/**
 * İlk sayfanın sol kenar çubuğunu çizer: fotoğraf, iletişim, yetenekler, diller ve sertifikalar.
 *
 * <p>{@code y} alanı, bir sonraki çizimin yapılacağı dikey konumu tutar. PDF koordinat
 * sisteminde (0,0) sayfanın <b>sol alt</b> köşesidir; bu yüzden aşağı inildikçe y azalır.</p>
 */
final class SidebarRenderer {

    private static final float PHOTO_RADIUS = 56f;
    private static final float LEFT = Theme.SIDEBAR_PADDING;
    private static final float RIGHT = Theme.SIDEBAR_WIDTH - Theme.SIDEBAR_PADDING;
    private static final float WIDTH = RIGHT - LEFT;

    private final PageWriter writer;
    private final PdfFonts fonts;
    private float y;

    SidebarRenderer(PageWriter writer, PdfFonts fonts) {
        this.writer = writer;
        this.fonts = fonts;
    }

    void render(Resume resume, PDImageXObject photo) throws IOException {
        y = Theme.PAGE_HEIGHT - Theme.TOP_MARGIN + 4;
        drawPhoto(photo);
        drawContact(resume.getPersonalInfo());
        drawSkills(resume.getSkillGroups());
        drawLanguages(resume.getLanguages());
        drawCertificates(resume.getCertificates());
    }

    private void drawPhoto(PDImageXObject photo) throws IOException {
        float cx = Theme.SIDEBAR_WIDTH / 2;
        float cy = y - PHOTO_RADIUS;
        writer.strokeCircle(cx, cy, PHOTO_RADIUS + 4, 1.5f, Theme.SIDEBAR_ACCENT);
        writer.circularImage(photo, cx, cy, PHOTO_RADIUS);
        y = cy - PHOTO_RADIUS - 14;
    }

    private void drawContact(PersonalInfo info) throws IOException {
        heading("İletişim");
        contactItem("Telefon", info.getPhone());
        contactItem("E-posta", info.getEmail());
        contactItem("Konum", info.getLocation());
        contactItem("GitHub", info.getGithub());
        contactItem("LinkedIn", info.getLinkedin());
    }

    /** Değeri boş olan iletişim bilgileri çizilmez. */
    private void contactItem(String label, String value) throws IOException {
        if (value == null || value.isBlank()) {
            return;
        }
        y -= 12;
        writer.text(label.toUpperCase(Theme.TURKISH), fonts.semibold(), 7.5f, Theme.SIDEBAR_MUTED, LEFT, y, 0.6f);
        for (String line : TextWrapper.wrap(value, fonts.regular(), 9.5f, WIDTH)) {
            y -= 12;
            writer.text(line, fonts.regular(), 9.5f, Theme.SIDEBAR_TEXT, LEFT, y);
        }
        y -= 3;
    }

    private void drawSkills(List<SkillGroup> groups) throws IOException {
        if (groups.isEmpty()) {
            return;
        }
        heading("Yetenekler");
        for (SkillGroup group : groups) {
            y -= 13;
            writer.text(group.getName(), fonts.semibold(), 9.5f, Theme.SIDEBAR_TEXT, LEFT, y);
            for (String line : TextWrapper.wrapItems(group.getSkills(), "  ·  ", fonts.regular(), 9f, WIDTH)) {
                y -= 12;
                writer.text(line, fonts.regular(), 9f, Theme.SIDEBAR_SOFT, LEFT, y);
            }
            y -= 4;
        }
    }

    private void drawLanguages(List<Language> languages) throws IOException {
        if (languages.isEmpty()) {
            return;
        }
        heading("Yabancı Diller");
        for (Language language : languages) {
            y -= 13;
            writer.text(language.getName(), fonts.semibold(), 9.5f, Theme.SIDEBAR_TEXT, LEFT, y);
            writer.textRight(language.getLevel(), fonts.regular(), 8.5f, Theme.SIDEBAR_MUTED, RIGHT, y);
            y -= 10;
            drawLevelDots(language.getScore());
            y -= 6;
        }
    }

    /** Dil seviyesini 5 noktalı gösterge olarak çizer: dolu noktalar = puan. */
    private void drawLevelDots(int score) throws IOException {
        float radius = 3f;
        float gap = 11f;
        for (int i = 0; i < Language.MAX_SCORE; i++) {
            float cx = LEFT + radius + i * gap;
            if (i < score) {
                writer.fillCircle(cx, y, radius, Theme.SIDEBAR_ACCENT);
            } else {
                writer.fillCircle(cx, y, radius, Theme.SIDEBAR_RULE);
            }
        }
    }

    private void drawCertificates(List<String> certificates) throws IOException {
        if (certificates.isEmpty()) {
            return;
        }
        heading("Sertifikalar");
        for (String certificate : certificates) {
            List<String> lines = TextWrapper.wrap(certificate, fonts.regular(), 9f, WIDTH - 10);
            for (int i = 0; i < lines.size(); i++) {
                y -= 12;
                if (i == 0) {
                    writer.fillCircle(LEFT + 2, y + 3, 1.6f, Theme.SIDEBAR_ACCENT);
                }
                writer.text(lines.get(i), fonts.regular(), 9f, Theme.SIDEBAR_SOFT, LEFT + 10, y);
            }
            y -= 3;
        }
    }

    private void heading(String title) throws IOException {
        y -= 24;
        writer.text(title.toUpperCase(Theme.TURKISH), fonts.bold(), 10.5f, Theme.SIDEBAR_ACCENT, LEFT, y, 1.2f);
        y -= 6;
        writer.line(LEFT, y, RIGHT, y, 0.6f, Theme.SIDEBAR_RULE);
        y -= 2;
    }
}
