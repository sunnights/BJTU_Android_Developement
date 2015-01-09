package cn.edu.bjtu.soundrecorder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by andrew on 2015/1/9.
 */
public class LoginActivity extends Activity{
    private final String TAG = "LoginActivity";
    private EditText etUserName;
    private EditText etPassword;
    private Button btnSubmit;
    private TextView btnRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        etUserName = (EditText) findViewById(R.id.etUserName);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnRegister = (TextView) findViewById(R.id.btnRegister);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //此处判断用户输入的etUserName和etPassword


                Intent i = new Intent(LoginActivity.this,SoundRecorderActivity.class);
                startActivity(i);
                LoginActivity.this.finish();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this,SoundRecorderActivity.class);
                startActivity(i);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id){
            case R.id.action_settings:
                Toast.makeText(this, R.string.action_settings, Toast.LENGTH_SHORT).show();
                break;
            case R.id.account_setting:
                Toast.makeText(this,R.string.action_account,Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Login");
//                SBaiduPCS.getInstance().test_login();
                Log.d(TAG, "Login2");
//                SBaiduPCS mSBaiduPCS = SBaiduPCS.getInstance();
//                mSBaiduPCS.test_login();
                break;
            case R.id.quit_setting:
                Toast.makeText(this,R.string.action_quit,Toast.LENGTH_SHORT).show();
                this.finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
