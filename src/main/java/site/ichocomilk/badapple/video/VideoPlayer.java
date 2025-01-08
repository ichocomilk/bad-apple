package site.ichocomilk.badapple.video;

import java.awt.image.BufferedImage;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import site.ichocomilk.badapple.decoder.FrameDecoder;

public final class VideoPlayer {
    private final FrameDecoder frameDecoder;
    private final Logger logger;
    private final Plugin plugin;

    public VideoPlayer(FrameDecoder frameDecoder, Logger logger, Plugin plugin) {
        this.frameDecoder = frameDecoder;
        this.logger = logger;
        this.plugin = plugin;
    }

    @SuppressWarnings("deprecation")
    public void load(final int x, final int y, final int z, final int fps) {
        final World world = Bukkit.getWorlds().get(0);
        final long millis = 1000 / fps;
        while (frameDecoder.hasNext()) {
            frameDecoder.next((frame) -> {
                final BufferedImage image = frame.image();
                plugin.getServer().getScheduler().runTask(plugin, () -> {
                    for (int i = 0; i < image.getWidth(); i++) {
                        for (int c = 0; c < image.getHeight(); c++) {
                            final int rgba = image.getRGB(i, c);
                            final int red = (rgba >> 16) & 0xFF;
                            final int green = (rgba >> 8) & 0xFF;
                            final int blue = (rgba) & 0xFF;
                            final byte woolData = woolFromColor(red, green, blue);
                            world.getBlockAt(x+i, y, z+c).setTypeIdAndData(Material.WOOL.getId(), woolData, false);                  
                        }
                    }
                });
            });

            try {
                Thread.sleep(millis);
            } catch (final Exception e) {
                logger.warning("Error sleeping, skipping to next frame");
            }
        }
    }

    private static final DyeColor[] COLORS = DyeColor.values();
    @SuppressWarnings("deprecation")
    private byte woolFromColor(final int red, final int green, final int blue) {
        int distance = Integer.MAX_VALUE;
        DyeColor closest = null;
        for (final DyeColor dye : COLORS) {
            final Color color = dye.getColor();
            int dist = Math.abs(color.getRed() - red) + Math.abs(color.getGreen() - green) + Math.abs(color.getBlue() - blue);
            if (dist < distance) {
                distance = dist;
                closest = dye;
            }
        }
        return closest.getWoolData();
    }
}
