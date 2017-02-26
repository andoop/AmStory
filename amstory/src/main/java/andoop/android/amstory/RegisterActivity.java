package andoop.android.amstory;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import andoop.android.amstory.utils.SpUtils;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {

    @InjectView(R.id.et_register_account)
    EditText etRegisterAccount;
    @InjectView(R.id.et_register_name)
    EditText etRegisterName;

    @InjectView(R.id.et_register_pass)
    EditText etRegisterPass;
    @InjectView(R.id.et_register_pass2)
    EditText etRegisterPass2;
    @InjectView(R.id.btn_register_register)
    Button btnRegisterRegister;
    @InjectView(R.id.iv_register_icon)
    ImageView ivRegisterIcon;
    @InjectView(R.id.rb_register_man)
    RadioButton rbRegisterMan;
    @InjectView(R.id.rb_register_woman)
    RadioButton rbRegisterWoman;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.inject(this);
    }

    @OnClick(R.id.btn_register_register)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_register_register:
                String account = etRegisterAccount.getText().toString();
                String paw = etRegisterPass.getText().toString();
                String paw2 = etRegisterPass2.getText().toString();
                String name = etRegisterName.getText().toString();
                if (TextUtils.isEmpty(account) || TextUtils.isEmpty(paw) ||
                        TextUtils.isEmpty(paw2) || TextUtils.isEmpty(name)) {
                    Toast.makeText(RegisterActivity.this, "请填写完整信息", Toast.LENGTH_SHORT).show();
                } else if (!paw.equals(paw2)) {
                    Toast.makeText(RegisterActivity.this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    SpUtils.getInstace().save("account",account);
                    SpUtils.getInstace().save("paw", paw);
                    SpUtils.getInstace().save("name", name);

                    if(rbRegisterMan.isChecked()) {
                        SpUtils.getInstace().save("gender","男");
                    }else {
                        SpUtils.getInstace().save("gender","女");
                    }

                    Intent intent = getIntent();
                    intent.putExtra("account", account);
                    setResult(RESULT_OK, intent);
                    finish();
                }

                break;
        }
    }
}
