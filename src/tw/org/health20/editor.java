package tw.org.health20;

import java.io.File;
import java.io.FileNotFoundException;
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
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class editor extends Activity {
	private static final int PHOTO_SUCCESS = 1;  
	private static final int CAMERA_SUCCESS = 2;   
	private static String requestURL = "http://medhint.nhri.org.tw/hpforum/testupload.jsp";
	private String picPath = null;	
	private EditText edittitle,editmsg;
	private Button back_post, post,upload;
	private String user_seqid, user_cname, parent_seqid, seq_id, hierarchy,
			strResult,title;
	//private MyEditText editmsg;
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
			       Intent getImage = new Intent(Intent.ACTION_GET_CONTENT);   
			       getImage.addCategory(Intent.CATEGORY_OPENABLE);   
			       getImage.setType("image/*");   
			      startActivityForResult(getImage, PHOTO_SUCCESS);   
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
		
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {  
		    ContentResolver resolver = getContentResolver();   
		    if (resultCode == RESULT_OK) {  
		        switch (requestCode) {  
		        case PHOTO_SUCCESS:  
		            //获得图片的uri   
		            Uri originalUri = intent.getData();   
		            Bitmap bitmap = null;
		            //File file =new File(picPath);
		            //FileImageUpload FIU=new FileImageUpload();
		            //FIU.uploadFile(file, requestURL, seq_id);
		            //picPath=intent.getStringExtra(this.KEY_PHOTO_PATH);

		            try {  
		                Bitmap originalBitmap = BitmapFactory.decodeStream(resolver.openInputStream(originalUri));  
		                bitmap = resizeImage(originalBitmap, 200, 200);  
		            } catch (FileNotFoundException e) {  
		                e.printStackTrace();  
		            }  
		            if(bitmap != null){  
		                //根据Bitmap对象创建ImageSpan对象  
		                ImageSpan imageSpan = new ImageSpan(this, bitmap);  
		                //创建一个SpannableString对象，以便插入用ImageSpan对象封装的图像  
		                SpannableString spannableString = new SpannableString("[local]"+1+"[/local]");  
		                //  用ImageSpan对象替换face  
		                spannableString.setSpan(imageSpan, 0, "[local]1[local]".length()+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);  
		                //将选择的图片追加到EditText中光标所在位置  
		                int index = editmsg.getSelectionStart(); //获取光标所在位置  
		                Editable edit_text = editmsg.getEditableText();  
		                if(index <0 || index >= edit_text.length()){  
		                    edit_text.append(spannableString);  
		                }else{  
		                    edit_text.insert(index, spannableString);  
		                }  
		            }else{  
		                Toast.makeText(this, "获取图片失败", Toast.LENGTH_SHORT).show();  
		            }  
		            break;  
		          default:  
		            break;  
		        }  
		    }  
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
	
	private Bitmap resizeImage(Bitmap originalBitmap, int newWidth, int newHeight){  
	    int width = originalBitmap.getWidth();  
	    int height = originalBitmap.getHeight();  
	    //定义欲转换成的宽、高  
//	      int newWidth = 200;  
//	      int newHeight = 200;  
	    //计算宽、高缩放率  
	    float scanleWidth = (float)newWidth/width;  
	    float scanleHeight = (float)newHeight/height;  
	    //创建操作图片用的matrix对象 Matrix  
	    Matrix matrix = new Matrix();  
	    // 缩放图片动作  
	    matrix.postScale(scanleWidth,scanleHeight);  
	    //旋转图片 动作  
	    //matrix.postRotate(45);  
	    // 创建新的图片Bitmap  
	    Bitmap resizedBitmap = Bitmap.createBitmap(originalBitmap,0,0,width,height,matrix,true);  
	    return resizedBitmap;  
	}
}
