package tw.org.health20;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class editor extends Activity {
	private EditText edittitle, editmsg;
	private Button back_post, post,upload;
	private String user_seqid, user_cname, parent_seqid, seq_id, hierarchy,
			strResult,title;
	//private TextView texttitle;
	//private WebView wv_edittor;
		protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.editor);

		Bundle bundle = this.getIntent().getExtras();
		seq_id = bundle.getString("seq_id");
		user_cname = bundle.getString("user_cname");
		user_seqid = bundle.getString("user_seqid");
		parent_seqid = bundle.getString("parent_seqid");
		if(!seq_id.equals("new")){
			if(bundle.getString("title").indexOf("re:")==-1)
			title="re:"+bundle.getString("title");
			else
			title=bundle.getString("title");
		}
		edittitle = (EditText) this.findViewById(R.id.edittitle);
		//texttitle=(TextView)findViewById(R.id.texttitle);
		editmsg = (EditText) this.findViewById(R.id.editmsg);
		post = (Button) this.findViewById(R.id.post);
		back_post=(Button)findViewById(R.id.back_post);
		upload=(Button)findViewById(R.id.upload);
		//parent_seqid = "112";// 測試用
		hierarchy = "3";
		/*Toast.makeText(
				editor.this,
				"seq_id:" + seq_id + ",parent_seqid:" + parent_seqid
						+ ",user_seqid:" + user_seqid, Toast.LENGTH_LONG)
				.show();*/
		if(title!=null){
			//texttitle.setText(title);
			//edittitle.getLayoutParams().width=0;
			//edittitle.getLayoutParams().height=0;
			//texttitle.getLayoutParams().width=texttitle.getLayoutParams().MATCH_PARENT;
			//edittitle.setText(title);
			edittitle.setText(title);
			edittitle.setEnabled(false);
		//}else{
			//edittitle.setText(title);
			//edittitle.setWidth(edittitle.getLayoutParams().WRAP_CONTENT);
			//texttitle.setText(title);
			//texttitle.getLayoutParams().width=0;
			//texttitle.getLayoutParams().height=0;
		}
		
		back_post.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		upload.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
			
		post.setOnClickListener(new View.OnClickListener() {
			
			
			public void onClick(View v) {
				if(seq_id.equals("new")){
				ppost();
				}else{
				repost();
				}
				//Toast.makeText(editor.this, "+" + strResult, Toast.LENGTH_LONG).show();
				Intent intent = new Intent();
				intent.setClass(editor.this, Ongoing_subject.class);
				Bundle bundle = new Bundle();
				bundle.putString("seq_id", parent_seqid);
				bundle.putString("user_cname", user_cname);
				bundle.putString("user_seqid", user_seqid);
				intent.putExtras(bundle);
				startActivity(intent);
				finish();
			}
		});

	}
	


		public void repost() {
			String uriAPI = "http://medhint.nhri.org.tw/hpforum/forum_subject_upd.jsp";
			
			HttpPost httpRequest = new HttpPost(uriAPI);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("parent_seqid", parent_seqid));
			params.add(new BasicNameValuePair("hierarchy", hierarchy));
			params.add(new BasicNameValuePair("author", user_cname));
			params.add(new BasicNameValuePair("author_id", user_seqid));
			params.add(new BasicNameValuePair("seq_id", "new"));
			params.add(new BasicNameValuePair("title", edittitle.getText()
					.toString()));
			params.add(new BasicNameValuePair("message0", editmsg.getText()
					.toString()));
			/*
			 * String seq_id = request.getParameter("seq_id"); if(seq_id==null)
			 * seq_id=""; String parent_seqid =
			 * request.getParameter("parent_seqid"); if(parent_seqid==null)
			 * parent_seqid=""; String time_stamp =
			 * request.getParameter("time_stamp"); if(time_stamp==null)
			 * time_stamp=""; String category_seqid =
			 * request.getParameter("category_seqid"); if(category_seqid==null)
			 * category_seqid=""; String hierarchy =
			 * request.getParameter("hierarchy"); if(hierarchy==null) hierarchy="";
			 * String author = request.getParameter("author"); if(author==null)
			 * author=""; String title = ""; if(request.getParameter("title")!=null)
			 * title=TextArea.trim_Single_quotes(request.getParameter("title"));
			 * String message = ""; if(request.getParameter("message0")!=null)
			 * message
			 * =TextArea.trim_Single_quotes(request.getParameter("message0"));
			 * String sub_topic = request.getParameter("sub_topic");
			 * if(sub_topic==null) sub_topic=""; String set =
			 * request.getParameter("set"); if(set==null) set=""; String author_id =
			 * request.getParameter("author_id"); if(author_id==null) author_id="";
			 */
			try {

				httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

				HttpResponse httpResponse = new DefaultHttpClient()
				.execute(httpRequest);

				if (httpResponse.getStatusLine().getStatusCode() == 200) {

					strResult = EntityUtils.toString(httpResponse.getEntity());
				} else {
					Log.e("n", "b");
				}
			} catch (ClientProtocolException e) {

				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {

				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	
	public void ppost() {
		String uriAPI = "http://medhint.nhri.org.tw/hpforum/forum_subject_upd.jsp";
		
		HttpPost httpRequest = new HttpPost(uriAPI);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("parent_seqid", parent_seqid));
		params.add(new BasicNameValuePair("hierarchy", hierarchy));
		params.add(new BasicNameValuePair("author", user_cname));
		params.add(new BasicNameValuePair("author_id", user_seqid));
		params.add(new BasicNameValuePair("seq_id", "new"));
		params.add(new BasicNameValuePair("title", edittitle.getText()
				.toString()));
		params.add(new BasicNameValuePair("message0", editmsg.getText()
				.toString()));
		/*
		 * String seq_id = request.getParameter("seq_id"); if(seq_id==null)
		 * seq_id=""; String parent_seqid =
		 * request.getParameter("parent_seqid"); if(parent_seqid==null)
		 * parent_seqid=""; String time_stamp =
		 * request.getParameter("time_stamp"); if(time_stamp==null)
		 * time_stamp=""; String category_seqid =
		 * request.getParameter("category_seqid"); if(category_seqid==null)
		 * category_seqid=""; String hierarchy =
		 * request.getParameter("hierarchy"); if(hierarchy==null) hierarchy="";
		 * String author = request.getParameter("author"); if(author==null)
		 * author=""; String title = ""; if(request.getParameter("title")!=null)
		 * title=TextArea.trim_Single_quotes(request.getParameter("title"));
		 * String message = ""; if(request.getParameter("message0")!=null)
		 * message
		 * =TextArea.trim_Single_quotes(request.getParameter("message0"));
		 * String sub_topic = request.getParameter("sub_topic");
		 * if(sub_topic==null) sub_topic=""; String set =
		 * request.getParameter("set"); if(set==null) set=""; String author_id =
		 * request.getParameter("author_id"); if(author_id==null) author_id="";
		 */
		try {

			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

			HttpResponse httpResponse = new DefaultHttpClient()
			.execute(httpRequest);

			if (httpResponse.getStatusLine().getStatusCode() == 200) {

				strResult = EntityUtils.toString(httpResponse.getEntity());
			} else {
				Log.e("n", "b");
			}
		} catch (ClientProtocolException e) {

			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
