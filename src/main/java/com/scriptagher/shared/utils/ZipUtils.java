package com.scriptagher.shared.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipUtils {

    /**
     * Unzips a ZIP file to the specified directory.
     *
     * @param zipFile        The ZIP file to extract.
     * @param destinationDir The directory to extract the contents into.
     * @throws IOException If there is an error during extraction.
     */
    public static void unzipFile(File zipFile, File destinationDir) throws IOException {
        boolean extractionSuccessful = false;

        // Try-with-resources to handle the ZipInputStream
        try (ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry entry;
            while ((entry = zipIn.getNextEntry()) != null) {
                getExtractedFile(zipFile, destinationDir, zipIn, entry);
            }

            // Extraction is considered successful if no errors occurred
            extractionSuccessful = true;

        } catch (IOException e) {
            e.printStackTrace(); // Handle IO errors during extraction
        } finally {
            // Check if extraction was successful before attempting to delete the zip
            dleteZipIfUnzipped(zipFile, extractionSuccessful);
        }
    }

    // TODO FIX
    /**
     * Deletes the ZIP file if it was successfully unzipped.
     *
     * @param zipFile              The ZIP file to delete.
     * @param extractionSuccessful Whether the extraction was successful or not.
     */
    public static void dleteZipIfUnzipped(File zipFile, boolean extractionSuccessful) {
        if (extractionSuccessful) {
            if (zipFile.exists()) {
                // Fix permissions before attempting to delete the ZIP file
                BotUtils.fixPermissions(zipFile);

                // Delete the ZIP file after extraction
                zipFile.delete();

                if (zipFile.exists()) {
                    // System.out.println("File ZIP eliminato con successo.");
                } else {
                    zipFile.delete();
                }
            } else {
                System.err.println("File ZIP non trovato al percorso: " + zipFile.getAbsolutePath());
            }
        }
    }

    /**
     * Extracts each file from the ZIP entry to the destination directory.
     *
     * @param zipFile        The ZIP file being processed.
     * @param destinationDir The directory to extract files into.
     * @param zipIn          The ZipInputStream reading the ZIP file.
     * @param entry          The current entry (file or directory) in the ZIP
     *                       archive.
     * @throws IOException If there is an error during extraction.
     */
    public static void getExtractedFile(File zipFile, File destinationDir, ZipInputStream zipIn, ZipEntry entry)
            throws IOException, FileNotFoundException {
        // Skip empty entries
        if (entry.getSize() == 0) {
            System.err.println("Empty file entry detected: " + entry.getName());
            return;
        }

        // Skip the entry if it's the zip file itself
        if (entry.getName().equals(zipFile.getName())) {
            System.err.println("Skipping the zip file itself: " + entry.getName());
            return;
        }

        String fileName = Paths.get(entry.getName()).getFileName().toString();
        File extractedFile = new File(destinationDir, fileName);

        // If it's a directory, create it
        if (entry.isDirectory()) {
            if (!extractedFile.exists()) {
                extractedFile.mkdirs();
            }
        } else {
            // Create the parent directory for the file if it doesn't exist
            File parentDir = extractedFile.getParentFile();
            if (!parentDir.exists()) {
                parentDir.mkdirs();
            }

            // Extract the file from the ZIP stream
            try (FileOutputStream fos = new FileOutputStream(extractedFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = zipIn.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
            }
        }

        // Close the current entry
        zipIn.closeEntry();
    }
}
