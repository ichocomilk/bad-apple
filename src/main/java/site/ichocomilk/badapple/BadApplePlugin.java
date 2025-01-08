package site.ichocomilk.badapple;

import java.io.File;

import org.bukkit.plugin.java.JavaPlugin;

import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer;

import site.ichocomilk.badapple.command.BadAppleStartCommand;
import site.ichocomilk.badapple.decoder.FrameDecoder;
import site.ichocomilk.badapple.decoder.MusicDecoder;
import site.ichocomilk.badapple.video.VideoFrameTask;

public final class BadApplePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getDataFolder().mkdir();
        final File[] files = getDataFolder().listFiles();
        if (files == null || files.length == 0) {
            getLogger().warning("""
                Remember add all images to plugin folder,
                download in: https://github.com/pikapower9080/bad-apple-frames
            """);
            return;
        }
    
        final FrameDecoder frameDecoder = new FrameDecoder(getDataFolder(), files, getLogger());
        final RadioSongPlayer radio = MusicDecoder.load(getLogger());
        final VideoFrameTask videoPlayer = new VideoFrameTask(frameDecoder, getLogger(), this, radio);

        getCommand("badapple").setExecutor(new BadAppleStartCommand(videoPlayer));
    }
}