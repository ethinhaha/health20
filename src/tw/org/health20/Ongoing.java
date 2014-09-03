package tw.org.health20;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Ongoing extends Activity {
	

	private ListView ongoing_listview;
	private String[] seq_id;
	private String user_cname,user_seqid;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ongoing_list);
		
		ongoing_listview=(ListView) this.findViewById(R.id.listsubject);
		//listview_test=(ListView)findViewById(R.id.listsubject);
		Bundle bundle=this.getIntent().getExtras();
		String json=bundle.getString("msg");
		user_cname=bundle.getString("user_cname");
		user_seqid=bundle.getString("user_seqid");
		String[] ongoingTitle=parseJSONData(getJSONData(json));
		//Log.e("1",ongoingTitle[0]);
		//Log.e("2",ongoingTitle[1]);
		
		ListAdapter listAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,ongoingTitle);
		ongoing_listview.setAdapter(listAdapter);
		// TODO Auto-generated method stub
		ongoing_listview.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				 //Toast.makeText(Ongoing.this, "seq_id:"+seq_id[arg2],Toast.LENGTH_LONG).show();
				 Intent intent = new Intent();
				 intent.setClass(Ongoing.this, Ongoing_subject.class);
				 //TODO Auto-generated method stub
				 Bundle bundle=new Bundle();
				 bundle.putString("seq_id",seq_id[arg2] );
				 bundle.putString("user_cname", user_cname);
				 bundle.putString("user_seqid", user_seqid);
				 intent.putExtras(bundle);					
				 startActivity(intent); 
			}
			
		});
	}
	private JSONArray getJSONData(String json){
		try{
		JSONArray jsonarr=new JSONArray(json);
		return jsonarr;
		}catch(Exception e){
		return null;
		}
	}
	
	private String[] parseJSONData(JSONArray content){
	try{
		String[] ongoingTitle=new String[content.length()];
		String[] ongoingSeqid=new String[content.length()];
		for(int i=0;i<content.length();i++){
			JSONObject ongoing=content.getJSONObject(i);
			//Log.e("title",ongoing.getString("title"));
			//Log.e("seq_id",ongoing.getString("seq_id"));
			ongoingTitle[i]=ongoing.getString("title");
			ongoingSeqid[i]=ongoing.getString("seq_id");
			}
		seq_id=ongoingSeqid;
		return ongoingTitle;  
	}catch(Exception e){
		e.printStackTrace();
		return null;
	}
	}
	
}
