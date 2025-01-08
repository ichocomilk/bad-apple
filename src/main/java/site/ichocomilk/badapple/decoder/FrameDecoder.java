package site.ichocomilk.badapple.decoder;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.bukkit.Bukkit;

public final class FrameDecoder {

    private final File datafolder;
    private final int filesAmount;
    private final Logger logger;
    private int i = 0;

    public FrameDecoder(File datafolder, File[] files, Logger logger) {
        this.datafolder = datafolder;
        this.filesAmount = files.length;
        this.logger = logger;
    }

    public int getFilesAmount() {
        return filesAmount;
    }

    public boolean hasNext() {
        return i++ < filesAmount;
    }

    private File loadNextFile() {
        if (i <= 9) {
            return new File(datafolder, "00"+i+".jpg");
        }
        if (i <= 99) {
            return new File(datafolder, "0"+i+".jpg");
        }
        return new File(datafolder, i+".jpg");
    }

    public void next(final Consumer<BufferedImage> consumer) {
        final File file = loadNextFile();
        if (!file.exists()) {
            Bukkit.broadcastMessage("The frame " + i + " don't exist");
            return;
        }

        BufferedImage image = null;
        try {
            image = ImageIO.read(file);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "error loading the image " + file.getName(), e);
            return;
        }

        final int width = image.getWidth() / 5;
        final int height = image.getHeight() / 5;
         
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(image, 0, 0, width, height, null);
        graphics2D.dispose();

        consumer.accept(resizedImage);
    }
}
