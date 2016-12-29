package io.github.ryanhoo.music.ui.local.all;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.github.ryanhoo.music.R;
import io.github.ryanhoo.music.data.model.Song;
import io.github.ryanhoo.music.ui.base.adapter.IAdapterView;
import io.github.ryanhoo.music.utils.TimeUtils;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 9/2/16
 * Time: 5:58 PM
 * Desc: LocalMusicItemView
 */
public class LocalMusicItemView extends RelativeLayout implements IAdapterView<Song> {

    @InjectView(R.id.text_view_name)
    TextView textViewName;
    @InjectView(R.id.text_view_artist)
    TextView textViewArtist;
    @InjectView(R.id.text_view_duration)
    TextView textViewDuration;

    public LocalMusicItemView(Context context) {
        super(context);
        View inflate = View.inflate(context, R.layout.item_local_music, this);
        ButterKnife.inject(this,inflate);
    }

    @Override
    public void bind(Song song, int position) {
        textViewName.setText(song.getDisplayName());
        textViewArtist.setText(song.getArtist());
        textViewDuration.setText(TimeUtils.formatDuration(song.getDuration()));
    }
}
