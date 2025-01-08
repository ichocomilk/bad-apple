package site.ichocomilk.badapple.video;

import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer;

import site.ichocomilk.badapple.decoder.FrameDecoder;

public final class VideoFrameTask {

    private final FrameDecoder frameDecoder;
    private final Logger logger;
    private final Plugin plugin;
    private final RadioSongPlayer song;

    public VideoFrameTask(FrameDecoder frameDecoder, Logger logger, Plugin plugin, RadioSongPlayer song) {
        this.frameDecoder = frameDecoder;
        this.logger = logger;
        this.plugin = plugin;
        this.song = song;
    }

    public void load(final int x, final int y, final int z, final int fps, final Player player) {
        final long millis = 1000 / fps;
        final VideoConsumer consumer = new VideoConsumer(frameDecoder.getFilesAmount(), x, y, z, player, song, plugin);

        while (frameDecoder.hasNext()) {
            frameDecoder.next(consumer);
            try {
                Thread.sleep(millis);
            } catch (final Exception e) {
                logger.warning("Error sleeping, skipping to next frame");
            }
        }
    }
}