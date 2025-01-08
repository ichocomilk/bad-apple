package site.ichocomilk.badapple.command;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import site.ichocomilk.badapple.video.VideoPlayer;

public final class BadAppleStartCommand implements CommandExecutor {

    private final VideoPlayer videoPlayer;

    public BadAppleStartCommand(VideoPlayer videoPlayer) {
        this.videoPlayer = videoPlayer;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cYou need be a player");
            return true;
        }
        if (args.length != 1) {
            sender.sendMessage("§cFormat: /badapple (fps)");
            return true;
        }
        final int fps = parseInt(args[0], sender);
        if (fps == -1) {
            return true;
        }
        sender.sendMessage("§aPlaying badapple");
        final Location location = player.getLocation();
        new Thread( () -> videoPlayer.load(location.getBlockX(), location.getBlockY(), location.getBlockZ(), fps)).start();
        return true;
    }

    private final int parseInt(final String text, final CommandSender sender) {
        try {
            final int value = Integer.parseInt(text);
            if (value <= 0) {
                sender.sendMessage("§cFps need be >= 1");
                return -1;
            }
            return value;
        } catch (NumberFormatException e) {
            sender.sendMessage("§cFps need be a number >= 1");
            return -1;
        }
    }
}
