package cn.xiaoxige.atext;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private TextView msg;
    private Button start;
    private Button stop;
    private Button startPlay;
    private Button stopPlay;
    private EditText startTime;
    private EditText endTime;
    private Listener listener;
    private Button btnCut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListener();
    }

    private void initView() {
        msg = (TextView) findViewById(R.id.msg);
        start = (Button) findViewById(R.id.start);
        stop = (Button) findViewById(R.id.stop);
        startPlay = (Button) findViewById(R.id.startPlay);
        stopPlay = (Button) findViewById(R.id.stopPlay);
        startTime = (EditText) findViewById(R.id.startTime);
        endTime = (EditText) findViewById(R.id.endTime);
        btnCut = (Button) findViewById(R.id.btnCut);
        listener = new Listener(this, start, stop, startPlay, stopPlay, btnCut, msg, startTime, endTime, new AudioInterface() {
            @Override
            public void Error(String error) {
                Toast.makeText(MainActivity.this, "错误：" + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initListener() {
        start.setOnClickListener(listener);
        stop.setOnClickListener(listener);
        startPlay.setOnClickListener(listener);
        stopPlay.setOnClickListener(listener);
        btnCut.setOnClickListener(listener);
    }
}
