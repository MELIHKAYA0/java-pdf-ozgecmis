package com.ozgecmis.pdf;

import com.ozgecmis.model.Education;
import com.ozgecmis.model.Experience;
import com.ozgecmis.model.PersonalInfo;
import com.ozgecmis.model.Project;
import com.ozgecmis.model.Resume;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Sağdaki ana sütunu çizer: ad-soyad, profil, iş deneyimleri, eğitim ve projeler.
 *
 * <p>İçerik sayfaya sığmazsa {@link #ensureSpace(float)} yeni sayfa açar; bir iş deneyimi
 * hiçbir zaman iki sayfaya bölünmez.</p>
 */
final class MainContentRenderer {

    private static final float LEFT = Theme.MAIN_LEFT;
    private static final float RIGHT = Theme.MAIN_RIGHT;
    private static final float WIDTH = Theme.MAIN_WIDTH;

    private static final float BODY_SIZE = 9.8f;
    private static final float BODY_LEADING = 13.6f;
    private static final float BULLET_INDENT = 11f;

    private final PageWriter writer;
    private final PdfFonts fonts;
    private float y;

    MainContentRenderer(PageWriter writer, PdfFonts fonts) {
        this.writer = writer;
        this.fonts = fonts;
    }

    void render(Resume resume) throws IOException {
        y = Theme.PAGE_HEIGHT - Theme.TOP_MARGIN;
        drawHeader(resume.getPersonalInfo());

        section("Profil");
        paragraph(resume.getPersonalInfo().getSummary());

        section("İş Deneyimi");
        for (Experience experience : resume.getExperiences()) {
            drawExperience(experience);
        }

        section("Eğitim");
        for (Education education : resume.getEducations()) {
            drawEducation(education);
        }

        if (!resume.getProjects().isEmpty()) {
            section("Projeler");
            for (Project project : resume.getProjects()) {
                drawProject(project);
            }
        }
    }

    // ------------------------------------------------------------------ bölümler

    private void drawHeader(PersonalInfo info) throws IOException {
        y -= 26;
        writer.text(info.getFullName().toUpperCase(Theme.TURKISH), fonts.bold(), 30f, Theme.HEADING, LEFT, y, 1.5f);
        y -= 22;
        writer.text(info.getTitle(), fonts.semibold(), 14f, Theme.ACCENT, LEFT, y, 0.3f);
        y -= 13;
        writer.fillRect(LEFT, y, 46, 3, Theme.ACCENT);
        y -= 4;
    }

    private void section(String title) throws IOException {
        ensureSpace(60);
        y -= 27;
        String upper = title.toUpperCase(Theme.TURKISH);
        float spacing = 1.4f;
        writer.text(upper, fonts.bold(), 11.5f, Theme.HEADING, LEFT, y, spacing);
        float textEnd = LEFT + TextWrapper.width(upper, fonts.bold(), 11.5f) + spacing * upper.length();
        writer.line(textEnd + 8, y + 3.8f, RIGHT, y + 3.8f, 0.8f, Theme.RULE);
        y -= 4;
    }

    private void paragraph(String text) throws IOException {
        List<String> lines = TextWrapper.wrap(text, fonts.regular(), BODY_SIZE + 0.2f, WIDTH);
        ensureSpace(lines.size() * BODY_LEADING);
        for (String line : lines) {
            y -= BODY_LEADING + 0.6f;
            writer.text(line, fonts.regular(), BODY_SIZE + 0.2f, Theme.BODY, LEFT, y);
        }
    }

    private void drawExperience(Experience experience) throws IOException {
        List<List<String>> bullets = new ArrayList<>();
        int lineCount = 0;
        for (String highlight : experience.getHighlights()) {
            List<String> lines = TextWrapper.wrap(highlight, fonts.regular(), BODY_SIZE, WIDTH - BULLET_INDENT);
            bullets.add(lines);
            lineCount += lines.size();
        }
        // Başlık + şirket satırı + madde satırları kadar yer yoksa yeni sayfaya geç
        ensureSpace(18 + 15 + lineCount * BODY_LEADING + 8);

        y -= 18;
        writer.text(experience.getPosition(), fonts.semibold(), 11.5f, Theme.HEADING, LEFT, y);
        writer.textRight(experience.getPeriod(), fonts.regular(), 9f, Theme.MUTED, RIGHT, y);

        y -= 13.5f;
        String company = experience.getCompany();
        if (experience.getLocation() != null) {
            company += "  ·  " + experience.getLocation();
        }
        writer.text(company, fonts.semibold(), 9.8f, Theme.ACCENT, LEFT, y);
        y -= 1.5f;

        for (List<String> lines : bullets) {
            for (int i = 0; i < lines.size(); i++) {
                y -= BODY_LEADING;
                if (i == 0) {
                    writer.fillCircle(LEFT + 2.5f, y + 3.2f, 1.7f, Theme.ACCENT);
                }
                writer.text(lines.get(i), fonts.regular(), BODY_SIZE, Theme.BODY, LEFT + BULLET_INDENT, y);
            }
        }
        y -= 8;
    }

    private void drawEducation(Education education) throws IOException {
        List<String> detailLines = education.getDetail() == null
                ? List.of()
                : TextWrapper.wrap(education.getDetail(), fonts.italic(), 9.3f, WIDTH);
        ensureSpace(16 + 13 + detailLines.size() * 12.5f + 6);

        y -= 16;
        writer.text(education.getDegree(), fonts.semibold(), 11f, Theme.HEADING, LEFT, y);
        writer.textRight(education.getPeriod(), fonts.regular(), 9f, Theme.MUTED, RIGHT, y);
        y -= 13;
        writer.text(education.getSchool(), fonts.semibold(), 9.8f, Theme.ACCENT, LEFT, y);
        for (String line : detailLines) {
            y -= 12.5f;
            writer.text(line, fonts.italic(), 9.3f, Theme.MUTED, LEFT, y);
        }
        y -= 4;
    }

    private void drawProject(Project project) throws IOException {
        List<String> lines = TextWrapper.wrap(project.getDescription(), fonts.regular(), BODY_SIZE, WIDTH);
        ensureSpace(16 + lines.size() * BODY_LEADING + 4);

        y -= 16;
        writer.text(project.getName(), fonts.semibold(), 10.8f, Theme.HEADING, LEFT, y);
        writer.textRight(project.getTechnologies(), fonts.regular(), 9f, Theme.MUTED, RIGHT, y);
        for (String line : lines) {
            y -= BODY_LEADING;
            writer.text(line, fonts.regular(), BODY_SIZE, Theme.BODY, LEFT, y);
        }
        y -= 4;
    }

    // ------------------------------------------------------------------ sayfa kontrolü

    /** Gereken yükseklik kalan alana sığmıyorsa yeni sayfa açar ve imleci en üste taşır. */
    private void ensureSpace(float neededHeight) throws IOException {
        if (y - neededHeight < Theme.BOTTOM_MARGIN) {
            writer.newPage();
            y = Theme.PAGE_HEIGHT - Theme.TOP_MARGIN;
        }
    }
}
