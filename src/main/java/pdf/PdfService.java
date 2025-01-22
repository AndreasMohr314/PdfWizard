package pdf;

import java.io.File;

// Interface for PDF operations
public interface PdfService {
    /**
     * Extracts, reorder or both specific pages from a PDF and saves it as a new file.
     *
     * @param inputFile  The source PDF file.
     * @param outputFile The destination file for the extracted or reorder pages.
     * @param pageIndices The index of the page to extract or reorder.
     * @throws Exception If the extraction fails.
     */
    void extractAndReorderPages(File inputFile, File outputFile, int[] pageIndices) throws Exception;

    /**
     * Gives back the numbers of Pages of an PDF file.
     *
     * @param inputFile  The source PDF file.
     * @throws Exception If the extraction fails.
     * @return The number of pages in Integer
     */
    int getNumberOfPages(File inputFile) throws Exception;
}


