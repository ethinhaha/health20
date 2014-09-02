package tw.org.health20;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class Ongoing_subject extends Activity {
	private ListView subject_list;
	private String user_cname, user_seqid, seq_id;
	private Button back_o, post_new;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.subject);
		Bundle bundle = this.getIntent().getExtras();
		seq_id = bundle.getString("seq_id");
		user_cname = bundle.getString("user_cname");
		user_seqid = bundle.getString("user_seqid");

		String url = "http://medhint.nhri.org.tw/hpforum/test3.jsp?seq_id="
				+ seq_id;

		GetMessage message = new GetMessage();
		String msg = message.stringQuery(url);

		subject_list = (ListView) findViewById(R.id.listsubject);
		post_new = (Button) this.findViewById(R.id.post_new);
		Log.e("msg", msg);
		// Toast.makeText(this, url, Toast.LENGTH_SHORT).show();
		// Toast.makeText(this, subjectTitle[0], Toast.LENGTH_SHORT).show();
		/*
		 * for(int i=0;i<subjectTitle.length;i++){
		 * Log.e("test"+i,subjectTitle[i]); }
		 */
		List<Map<String, Object>> data = this.parseJSONData(getJSONData(msg));
		ListAdapter adapter = new SimpleAdapter(this, data,
				R.layout.listview_subject, new String[] { "title", "author",
						"date" }, new int[] { R.id.title, R.id.author,
						R.id.date });
		subject_list
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					public void onItemClick(AdapterView<?> adapterView,
							View view, int position, long itemId) {
						SimpleAdapter adapter = (SimpleAdapter) adapterView
								.getAdapter();
						Map<String, Object> map = (Map<String, Object>) adapter
								.getItem(position);
						Intent intent = new Intent();
						intent.setClass(Ongoing_subject.this,
								SubjectDetail.class);
						// TODO Auto-generated method stub
						Bundle bundle = new Bundle();
						bundle.putString("user_cname", user_cname);
						bundle.putString("user_seqid", user_seqid);
						bundle.putString("seq_id", map.get("seq_id") + "");
						bundle.putString("parent_seqid", seq_id);
						intent.putExtras(bundle);
						startActivity(intent);

					}
				});

		subject_list.setAdapter(adapter);

		post_new.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(Ongoing_subject.this, editor.class);
				Bundle bundle = new Bundle();
				bundle.putString("user_cname", user_cname);
				bundle.putString("user_seqid", user_seqid);
				bundle.putString("seq_id", "new");
				bundle.putString("parent_seqid", seq_id);
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

	private List<Map<String, Object>> parseJSONData(JSONArray content) {

		List<Map<String, Object>> result = null;
		if (content != null && content.length() != 0) {
			result = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < content.length(); i++) {
				try {
					JSONObject ongoing = content.getJSONObject(i);
					// Log.e("title",ongoing.getString("title"));
					// Log.e("seq_id",ongoing.getString("seq_id"));
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("title", ongoing.getString("title"));
					map.put("seq_id", ongoing.getString("seq_id"));
					map.put("author", ongoing.getString("author"));
					map.put("date", ongoing.getString("date"));
					result.add(map);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
}