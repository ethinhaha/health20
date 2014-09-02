package tw.org.health20;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class Previous extends Activity {
	private ListView pre_listview;
	String[] seq_id;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.previous_list);
		pre_listview = (ListView) this.findViewById(R.id.listView2_pre);
		Bundle bundle = this.getIntent().getExtras();
		String json = bundle.getString("msg");
		String[] ongoingTitle = parseJSONData(getJSONData(json));
		// Log.e("1",ongoingTitle[0]);
		// Log.e("2",ongoingTitle[1]);

		ListAdapter listAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, ongoingTitle);
		pre_listview.setAdapter(listAdapter);
		// TODO Auto-generated method stub
		pre_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// Toast.makeText(Ongoing.this,
				// "seq_id:"+seq_id[arg2],Toast.LENGTH_LONG).show();
				Intent intent = new Intent();
				intent.setClass(Previous.this, PreviousDetail.class);
				// TODO Auto-generated method stub
				Bundle bundle = new Bundle();
				bundle.putString("seq_id", seq_id[arg2]);
				intent.putExtras(bundle);
				startActivity(intent);
			}

		});
	}

	private JSONArray getJSONData(String json) {
		try {
			JSONArray jsonarr = new JSONArray(json);
			return jsonarr;
		} catch (Exception e) {
			return null;
		}
	}

	private String[] parseJSONData(JSONArray content) {
		try {
			String[] ongoingTitle = new String[content.length()];
			String[] ongoingSeqid = new String[content.length()];
			for (int i = 0; i < content.length(); i++) {
				JSONObject ongoing = content.getJSONObject(i);
				// Log.e("title",ongoing.getString("title"));
				// Log.e("seq_id",ongoing.getString("seq_id"));
				ongoingTitle[i] = ongoing.getString("title");
				ongoingSeqid[i] = ongoing.getString("seq_id");
			}
			seq_id = ongoingSeqid;
			return ongoingTitle;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
