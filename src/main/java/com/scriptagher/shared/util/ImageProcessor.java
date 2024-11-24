package com.scriptagher.shared.util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class ImageProcessor {

    /**
     * Processes a PNG image by converting it to black and white with a white border
     * and black interior.
     *
     * @param inputPath  the path to the input PNG file
     * @param outputPath the path to save the processed PNG file
     * @throws Exception if an error occurs during image processing
     */
    public static void processImage(String inputPath, String outputPath) throws Exception {
        // Load the input image
        BufferedImage inputImage = ImageIO.read(new File(inputPath));
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();

        // Create a new image with the same dimensions
        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics = outputImage.createGraphics();

        // Iterate through each pixel and apply the black-and-white conversion
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = inputImage.getRGB(x, y);

                // Get alpha, red, green, blue components
                int alpha = (pixel >> 24) & 0xFF;
                int red = (pixel >> 16) & 0xFF;
                int green = (pixel >> 8) & 0xFF;
                int blue = pixel & 0xFF;

                // Check if pixel is white (near 255 in RGB) and has full opacity
                boolean isWhite = (red > 200 && green > 200 && blue > 200 && alpha > 200);

                // Check brightness for grayscale (luminance formula)
                int brightness = (int) (0.299 * red + 0.587 * green + 0.114 * blue);

                if (isWhite) {
                    // Make white pixels transparent
                    outputImage.setRGB(x, y, 0x00FFFFFF); // Transparent
                } else if (brightness > 128 && alpha > 0) {
                    // Create white border for visible pixels
                    outputImage.setRGB(x, y, Color.WHITE.getRGB());
                } else if (alpha > 0) {
                    // Create black interior
                    outputImage.setRGB(x, y, Color.BLACK.getRGB());
                } else {
                    // Transparent background
                    outputImage.setRGB(x, y, 0x00FFFFFF);
                }
            }
        }

        // Clean up
        graphics.dispose();

        // Save the processed image
        ImageIO.write(outputImage, "png", new File(outputPath));
    }

    /**
     * Processes a PNG image by making the interior black while preserving the
     * original border.
     *
     * @param inputPath  the path to the input PNG file
     * @param outputPath the path to save the processed PNG file
     * @throws Exception if an error occurs during image processing
     */
    public static void processImageInteriorBlack(String inputPath, String outputPath) throws Exception {
        // Load the input image
        BufferedImage inputImage = ImageIO.read(new File(inputPath));
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();

        // Create a new image with the same dimensions
        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        // Iterate through each pixel to apply the transformation
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = inputImage.getRGB(x, y);

                // Get alpha, red, green, blue components
                int alpha = (pixel >> 24) & 0xFF;

                // Check if the pixel is part of the interior
                if (alpha > 0) {
                    // Make the interior black
                    outputImage.setRGB(x, y, new Color(0, 0, 0, alpha).getRGB());
                } else {
                    // Keep transparent background
                    outputImage.setRGB(x, y, 0x00FFFFFF);
                }
            }
        }

        // Save the processed image
        ImageIO.write(outputImage, "png", new File(outputPath));
    }

    public static void main(String[] args) {
        try {
            // Percorsi delle immagini originali e di output
            String inputClosedHandPath = "/home/diego/Documents/repositoryGitHub/scriptagher/src/main/resources/icons/icons8-grabbed-tool-48.png";
            String outputClosedHandPath = "/home/diego/Documents/repositoryGitHub/scriptagher/src/main/resources/icons/icons8-grabbed-tool-48.png";

            // Elaborazione delle immagini
            ImageProcessor.processImageInteriorBlack(inputClosedHandPath, outputClosedHandPath);

            System.out.println("Image processing completed!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}