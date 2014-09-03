package tw.org.health20;

import java.net.URL;

import org.json.JSONObject;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.method.ScrollingMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.webkit.WebView;
import android.widget.TextView;

public class SubjectDetail extends Activity {
	private String user_cname,user_seqid,seq_id,parent_seqid;
	private String title,author,date,message;
	private TextView titleview,author_view,date_view,message_view;
	//private WebView message_wview;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.subject_detail);
		Bundle bundle=this.getIntent().getExtras();
		seq_id=bundle.getString("seq_id");
		user_cname=bundle.getString("user_cname");
		user_seqid=bundle.getString("user_seqid");
		parent_seqid=bundle.getString("parent_seqid");
		titleview=(TextView)findViewById(R.id.detail_title);
		author_view=(TextView)findViewById(R.id.detail_author);
		date_view=(TextView)findViewById(R.id.detail_date);
		message_view=(TextView)findViewById(R.id.detail_msg);
		message_view.setAutoLinkMask(Linkify.ALL);
		//message_wview=(WebView)findViewById(R.id.webview);
		String url="http://medhint.nhri.org.tw/hpforum/test4.jsp?seq_id="+seq_id;
		
		GetMessage jsonmsg = new GetMessage();
		String msg =jsonmsg.stringQuery(url);
		Log.e("msg",msg);
		try{
			JSONObject content =new JSONObject(msg);
			title=content.optString("title");
			author=content.optString("author");
			date=content.optString("date");
			message=content.optString("message");
		
		}catch(Exception e){
			e.printStackTrace();
		}
		ImageGetter imgGetter = new Html.ImageGetter() {
			public Drawable getDrawable(String source) {
			Drawable drawable = null;
			Log.d("Image Path", source);
			URL url;
			try {
			url = new URL(source);
			drawable = Drawable.createFromStream(url.openStream(), "");
			} catch (Exception e) {
			return null;
			}
			drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
			return drawable;
			}
			};
		titleview.setText(title);
		author_view.setText(author);
		date_view.setText(date);
		message_view.setMovementMethod(ScrollingMovementMethod.getInstance());//ºu°Ê
		//message_view.setText(Html.fromHtml(message));
		message_view.setText(Html.fromHtml(message,imgGetter,null));
		//Toast.makeText(this,"seq_id:"+seq_id+"title:"+title+"msg:"+message ,Toast.LENGTH_LONG).show();
		//message_wview.setWebViewClient(client)
	}
	
}
