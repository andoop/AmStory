package andoop.android.amstory;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import andoop.android.amstory.utils.SpUtils;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class SplashActivity extends AppCompatActivity {
    @InjectView(R.id.et_splash_account)
    EditText etSplashAccount;
    @InjectView(R.id.et_splash_pass)
    EditText etSplashPass;
    @InjectView(R.id.btn_splash_login)
    Button btnSplashLogin;
    @InjectView(R.id.tv_splash_register)
    TextView tvSplashRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.inject(this);
    }

    @OnClick({R.id.btn_splash_login, R.id.tv_splash_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_splash_login:
                String account = etSplashAccount.getText().toString();
                String psw = etSplashPass.getText().toString();

                if(TextUtils.isEmpty(account) || TextUtils.isEmpty(psw)) {
                    Toast.makeText(SplashActivity.this, "账号或密码不能为空", Toast.LENGTH_SHORT).show();

                }else {
                    String psw2 = SpUtils.getInstace().getString("account", "");
                    if(!psw.equals(psw2)) {
                        Toast.makeText(SplashActivity.this, "账号或密码错误，请重新输入", Toast.LENGTH_SHORT).show();
                        etSplashPass.setText("");
                        etSplashAccount.setText("");
                    }else {
                        Toast.makeText(SplashActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this,MainActivity.class));
                        finish();
                    }
                }
                break;
            case R.id.tv_splash_register:
                Intent intent = new Intent(this,RegisterActivity.class);
                startActivityForResult(intent,0);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_OK && resultCode == 0) {
            String account = data.getStringExtra("account");
            etSplashAccount.setText(account);
        }
    }
}
