package site.ichocomilk.badapple.video;

import java.awt.image.BufferedImage;
import java.util.function.Consumer;

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer;

final class VideoConsumer implements Consumer<BufferedImage> {

    private final int x, y, z;
    private final Player player;
    private final World world;
    private final RadioSongPlayer song;
    private final Plugin plugin;
    private final float framesPerSoundTick;

    private int soundTick, soundFramesLoop;

    public VideoConsumer(int amountFrames, int x, int y, int z, Player player, RadioSongPlayer song, Plugin plugin) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.player = player;
        this.world = player.getWorld();
        this.song = song;
        this.plugin = plugin;
        framesPerSoundTick = amountFrames / song.getSong().getLength();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void accept(final BufferedImage image) {
        plugin.getServer().getScheduler().runTask(plugin, () -> {
            if (song != null && player.isOnline()) {
                if (soundFramesLoop++ >= framesPerSoundTick) {
                    song.playTick(player, soundTick++);
                    soundFramesLoop = 0;
                }
            }
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
    }

    private static final DyeColor[] COLORS = DyeColor.values();
    @SuppressWarnings("deprecation")
    private static byte woolFromColor(final int red, final int green, final int blue) {
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
        if (closest == DyeColor.BLACK || closest == DyeColor.WHITE || closest == DyeColor.GRAY) {
            return closest.getWoolData();
        }
        return DyeColor.SILVER.getWoolData();
    }
}