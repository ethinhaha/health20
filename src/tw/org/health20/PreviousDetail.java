package tw.org.health20;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class PreviousDetail extends Activity {
	private String user_cname, user_seqid, seq_id;
	private String title, date, message;
	private TextView titleview, date_view;
	private WebView message_view;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.previous_detail);
		Bundle bundle = this.getIntent().getExtras();
		seq_id = bundle.getString("seq_id");
		user_cname = bundle.getString("user_cname");
		user_seqid = bundle.getString("user_seqid");
		titleview = (TextView) findViewById(R.id.detail_pre_title);
		date_view = (TextView) findViewById(R.id.detail_pre_date);
		message_view = (WebView) findViewById(R.id.detail_pre_msg);
		//message_view.setAutoLinkMask(Linkify.ALL);

		String url = "http://medhint.nhri.org.tw/hpforum/test4.jsp?seq_id="
				+ seq_id+"&user_cname="+user_cname+"&user_seqid="+user_seqid;

		GetMessage jsonmsg = new GetMessage();
		String msg = jsonmsg.stringQuery(url);
		// Log.e("msg",msg);

		try {
			JSONObject content = new JSONObject(msg);
			title = content.optString("title");
			date = content.optString("date");
			message = content.optString("message");

		} catch (Exception e) {
			e.printStackTrace();
		}

		titleview.setText(title);
		date_view.setText(date);
		message_view.getSettings().setJavaScriptEnabled(true);
		//message_view.setWebViewClient(new WebViewClient());  
		message_view.loadUrl(message);
		//message_view.setMovementMethod(ScrollingMovementMethod.getInstance());// ºu°Ê
		//message_view.setText(Html.fromHtml(message));
		// Toast.makeText(this,"seq_id:"+seq_id+"title:"+title+"msg:"+message
		// ,Toast.LENGTH_LONG).show();
	}
}
