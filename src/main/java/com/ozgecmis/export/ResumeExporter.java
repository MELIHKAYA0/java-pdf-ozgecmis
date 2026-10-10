package com.ozgecmis.export;

import com.ozgecmis.model.Resume;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Bir özgeçmişi dosyaya aktaran sınıfların ortak sözleşmesi (interface).
 *
 * <p>Main sınıfı somut PDF sınıfını değil bu arayüzü tanır. İleride örneğin
 * {@code HtmlResumeExporter} veya {@code DocxResumeExporter} yazılırsa Main'de yalnızca
 * oluşturulan nesne değişir; geri kalan kod aynı kalır (polimorfizm).</p>
 */
public interface ResumeExporter {

    void export(Resume resume, Path output) throws IOException;
}
