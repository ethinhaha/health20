package tw.org.health20;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

public class EditorHtml extends Activity {
	
	private String seq_id,user_cname,user_seqid,parent_seqid,editor_msg;
	private Button reset,back_pn,wpost_new;
	private WebView web_post;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web_editor);
		Bundle bundle = this.getIntent().getExtras();
		seq_id = bundle.getString("seq_id");
		user_cname = bundle.getString("user_cname");
		user_seqid = bundle.getString("user_seqid");
		parent_seqid = bundle.getString("parent_seqid");
		Log.e("msg",seq_id+","+user_cname+","+user_seqid+","+parent_seqid);
		editor_msg="http://medhint.nhri.org.tw/hpforum/editor_html.jsp?parent_seqid="+parent_seqid+"&seq_id="+seq_id+"&user_cname="+user_cname+"&user_seqid="+user_seqid;
		reset=(Button)findViewById(R.id.reset_post);
		back_pn=(Button)findViewById(R.id.back_pn);
		wpost_new=(Button)findViewById(R.id.wpost_new);
		web_post=(WebView)findViewById(R.id.web_post);
		Toast.makeText(this, editor_msg, Toast.LENGTH_LONG).show();
		back_pn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
				}
			});
		wpost_new.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				web_post.loadUrl("javascript:check_data();");
				//finish();
				}
			});
		reset.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				web_post.loadUrl("javascript:reset_android();");
				
				}
			});
		
		
		web_post.getSettings().setJavaScriptEnabled(true);
		web_post.setWebChromeClient(new WebChromeClient());
		web_post.setWebViewClient(new WebViewClient());
		web_post.loadUrl(editor_msg);
	}
	
	
	

}
