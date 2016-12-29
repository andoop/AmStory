package andoop.voicecut.android;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private String musicPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        musicPath= Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator
                +"aaamusic/";
        File file = new File(musicPath);
        if(!file.exists()){
            file.mkdirs();
        }

    }


    public void hebing(View view){
        File musicfile = new File(musicPath);
        File[] files = musicfile.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".mp3");
            }
        });

        try {
            final String s = CaoZuoMp3Utils.heBingMp3(files[0].getAbsolutePath(), files[1].getAbsolutePath(), "test");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
