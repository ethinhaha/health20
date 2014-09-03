package tw.org.health20;



import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class Login_app extends Activity {

	
	private EditText email,pw;
	private Button login;
	private CheckBox auto_cb; 
	private String user_mail,user_pw;
	private SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//網路執行續
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
		   .detectDiskReads()
		   .detectDiskWrites()
		   .detectNetwork() 
		   .penaltyLog()
		   .build());
		    StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
		   .detectLeakedSqlLiteObjects() 
		   .penaltyLog() 
		   .penaltyDeath()
		   .build()); 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_app);
		email =(EditText) this.findViewById(R.id.email);
		pw =(EditText) this.findViewById(R.id.pw);
		login = (Button) this.findViewById(R.id.login);
		auto_cb=(CheckBox)findViewById(R.id.rem_log);
		sp = this.getSharedPreferences("userInfo", 0);
		//rempw_cb=(CheckBox)findViewById(R.id.rempw_cb);
		//Toast.makeText(this, sp.getBoolean("auto_login", false)+"", Toast.LENGTH_LONG).show();
		//Toast.makeText(this,sp.getBoolean("auto_login", true)+"", Toast.LENGTH_LONG).show();
		if(sp.getBoolean("auto_login", false)){
			auto_cb.setChecked(true);
			email.setText(sp.getString("user_mail", ""));
			pw.setText(sp.getString("user_pw", ""));
            
			//Toast.makeText(this,"123"+sp.getString("user_mail", "")+sp.getString("user_pw", ""), Toast.LENGTH_LONG).show();
			user_mail=sp.getString("user_mail", "");
			user_pw=sp.getString("user_pw", "");
			Intent intent = new Intent(this,LoginSuccess.class);  
            Bundle bundle=new Bundle();
			bundle.putString("user_mail", user_mail);
			bundle.putString("user_pw",user_pw);  
			intent.putExtras(bundle);	
            Login_app.this.startActivity(intent);  
		}
		
		login.setOnClickListener(new View.OnClickListener(){ 
			 public void onClick(View v) 
			 {
				 	
				 user_mail=email.getText().toString();
				 user_pw=pw.getText().toString();
				 GetMessage message = new GetMessage();
				 String msg = message.stringQuery("http://medhint.nhri.org.tw/hpforum/test.jsp?email="+user_mail+"&pw="+user_pw);
				 if(msg.indexOf("false")==-1){
					 if(auto_cb.isChecked()){
						 Editor editor = sp.edit();  
	                      editor.putString("user_mail", user_mail);  
	                      editor.putString("user_pw",user_pw);  
	                      editor.commit();  
					 }
					 Intent intent = new Intent();
					 intent.setClass(Login_app.this, LoginSuccess.class);
					 
					 Bundle bundle=new Bundle();
					 bundle.putString("msg", msg);
					 intent.putExtras(bundle);					
					 startActivity(intent); 
					}else{
					Toast pop=Toast.makeText(Login_app.this,"輸入錯誤請重新輸入",Toast.LENGTH_LONG);
					pop.show();
					}
			 } 
			 });
		auto_cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {  
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {  
                if (auto_cb.isChecked()) {  
                    Toast.makeText(Login_app.this, "自動登入已選", Toast.LENGTH_SHORT).show();  
                    sp.edit().putBoolean("auto_login", true).commit();  
  
                } else {  

                    Toast.makeText(Login_app.this, "自動登入已取消", Toast.LENGTH_SHORT).show();  
                    sp.edit().putBoolean("auto_login", false).commit();  
                }  
            }  
        }); 
		
	
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login_app, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	


}
