package pdf;

import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;

// Implementation of PdfService using Apache PDFBox
public class PdfServiceImpl implements PdfService {
    @Override
    public void extractAndReorderPages(File inputFile, File outputFile, int[] pageIndices) throws Exception {
        try (PDDocument document = PDDocument.load(inputFile)) {
            PDDocument extractedPagesDoc = new PDDocument();

            for (int pageIndex : pageIndices) {
                if (pageIndex >= 0 && pageIndex < document.getNumberOfPages()) {
                    extractedPagesDoc.addPage(document.getPage(pageIndex));
                } else {
                    System.out.println("Page index out of bounds: " + pageIndex);
                }
            }

            extractedPagesDoc.save(outputFile);
            extractedPagesDoc.close();
        }
    }

    @Override
    public int getNumberOfPages(File inputFile) throws Exception {
        int result = 0;
        try (PDDocument document = PDDocument.load(inputFile)) {
            result = document.getNumberOfPages();
        }
        return result;
    };
}
