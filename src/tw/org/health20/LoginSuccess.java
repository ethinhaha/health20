package tw.org.health20;

import org.json.JSONObject;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LoginSuccess extends Activity {
	private TextView user_cname;
	private TextView user_seqid;
	private Button ongoing, previous;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu);
		user_cname = (TextView) findViewById(R.id.user_cname);
		user_seqid = (TextView) findViewById(R.id.user_seqid);
		ongoing = (Button) findViewById(R.id.ongoing);
		previous = (Button) findViewById(R.id.previous);

		Bundle bundle = this.getIntent().getExtras();
		String json = bundle.getString("msg");
		String user_mail = bundle.getString("user_mail");
		String user_pw = bundle.getString("user_pw");
		// Toast.makeText(this, "3."+json+":"+user_mail+":"+user_pw,
		// Toast.LENGTH_LONG).show();
		while (json == null) {
			String url = "http://medhint.nhri.org.tw/hpforum/test.jsp?email="
					+ user_mail + "&pw=" + user_pw;
			GetMessage message = new GetMessage();
			json = message.stringQuery(url);
		}
		// Toast.makeText(this, json, Toast.LENGTH_LONG).show();
		try {
			JSONObject content = new JSONObject(json);
			user_cname.setText(content.optString("user_cname").toString());
			user_seqid.setText(content.optString("user_seqid").toString());
		} catch (Exception e) {
			e.printStackTrace();

		}

		ongoing.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				GetMessage message = new GetMessage();
				String msg = message
						.stringQuery("http://medhint.nhri.org.tw/hpforum/test2.jsp?user_seqid="
								+ user_seqid.getText().toString()
								+ "&discussion=ongoing");
				// Toast
				// pop=Toast.makeText(LoginSuccess.this,msg,Toast.LENGTH_LONG);
				// pop.show();

				Intent intent = new Intent();
				intent.setClass(LoginSuccess.this, Ongoing.class);

				Bundle bundle = new Bundle();
				bundle.putString("user_cname", user_cname.getText().toString());
				bundle.putString("user_seqid", user_seqid.getText().toString());
				bundle.putString("msg", msg);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

		previous.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				GetMessage message = new GetMessage();
				String msg = message
						.stringQuery("http://medhint.nhri.org.tw/hpforum/test2.jsp?user_seqid="
								+ user_seqid.getText().toString()
								+ "&discussion=previous");
				// Toast
				// pop=Toast.makeText(LoginSuccess.this,msg,Toast.LENGTH_LONG);
				// pop.show();

				Intent intent = new Intent();
				intent.setClass(LoginSuccess.this, Previous.class);

				Bundle bundle = new Bundle();
				bundle.putString("msg", msg);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

	}

}
