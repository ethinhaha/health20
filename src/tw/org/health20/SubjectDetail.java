package tw.org.health20;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SubjectDetail extends Activity {
	private String user_cname, user_seqid, seq_id, parent_seqid;
	private String title, author, date, message;
	private Button back_sd,re_post;
	private TextView titleview, author_view, date_view;
	private WebView message_view;
	
	// private WebView message_wview;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.subject_detail);
		Bundle bundle = this.getIntent().getExtras();
		seq_id = bundle.getString("seq_id");
		user_cname = bundle.getString("user_cname");
		user_seqid = bundle.getString("user_seqid");
		parent_seqid = bundle.getString("parent_seqid");
		message_view = (WebView) findViewById(R.id.detail_msg);		
		titleview = (TextView) findViewById(R.id.detail_title);
		author_view = (TextView) findViewById(R.id.detail_author);
		date_view = (TextView) findViewById(R.id.detail_date);
		re_post=(Button)findViewById(R.id.re_post);
		back_sd=(Button)findViewById(R.id.back_sd);
		
		// message_view.setAutoLinkMask(Linkify.ALL);
		// message_wview=(WebView)findViewById(R.id.webview);
		String url = "http://medhint.nhri.org.tw/hpforum/test4.jsp?seq_id="
				+ seq_id + "&user_cname=" + user_cname + "&user_seqid="
				+ user_seqid;

		GetMessage jsonmsg = new GetMessage();
		String msg = jsonmsg.stringQuery(url);
		Log.e("msg", msg);
		try {
			JSONObject content = new JSONObject(msg);
			title = content.optString("title");
			author = content.optString("author");
			date = content.optString("date");
			message = content.optString("message");

		} catch (Exception e) {
			e.printStackTrace();
		}
		/*ImageGetter imgGetter = new Html.ImageGetter() {
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
				drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight());
				return drawable;
			}
		};*/
		back_sd.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
					finish();
				}
			});
		re_post.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(SubjectDetail.this,
						editor.class);
				// TODO Auto-generated method stub
				Bundle bundle = new Bundle();
				bundle.putString("user_cname", user_cname);
				bundle.putString("user_seqid", user_seqid);
				bundle.putString("seq_id",seq_id);
				bundle.putString("parent_seqid", parent_seqid);
				bundle.putString("title",titleview.getText().toString());
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		titleview.setText(title);
		author_view.setText(author);
		date_view.setText(date);
		message_view.getSettings().setJavaScriptEnabled(true);
		message_view.setWebViewClient(new WebViewClient());
		message_view.loadUrl(message);
		message_view.setDownloadListener(new DownloadListener(){
            public void onDownloadStart(String url, String userAgent,
                    String contentDisposition, String mimetype,
                    long contentLength) {
              Intent i = new Intent(Intent.ACTION_VIEW);
              i.setData(Uri.parse(url));
              startActivity(i);
            }
        });
		//message_view.setMovementMethod(ScrollingMovementMethod.getInstance());//
		// message_view.setText(Html.fromHtml(message));
		// message_view.setText(Html.fromHtml(message, imgGetter, null));
		// Toast.makeText(this,"seq_id:"+seq_id+"title:"+title+"msg:"+message
		// ,Toast.LENGTH_LONG).show();
		// message_wview.setWebViewClient(client)
	}
	  
}
