package site.ichocomilk.badapple.decoder;

import java.io.InputStream;
import java.util.logging.Logger;

import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;

public final class MusicDecoder {

    public static RadioSongPlayer load(final Logger logger) {
        final InputStream stream = MusicDecoder.class.getClassLoader().getResourceAsStream("music.nbs");
        if (stream == null) {
            logger.warning("Can't found the music in the .jar");
            return null;
        }
        final Song song = NBSDecoder.parse(stream);
        if (song == null) {
            return null;
        }
        return new RadioSongPlayer(song);
    }
}
