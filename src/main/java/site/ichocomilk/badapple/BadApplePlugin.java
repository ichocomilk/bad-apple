package site.ichocomilk.badapple;

import java.io.File;

import org.bukkit.plugin.java.JavaPlugin;

import site.ichocomilk.badapple.command.BadAppleStartCommand;
import site.ichocomilk.badapple.decoder.FrameDecoder;
import site.ichocomilk.badapple.video.VideoPlayer;

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
        final VideoPlayer videoPlayer = new VideoPlayer(frameDecoder, getLogger(), this);

        getCommand("badapple").setExecutor(new BadAppleStartCommand(videoPlayer));
    }
}