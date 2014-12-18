package cn.bjtu.edu.android.hellocustomui;

import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			tv_time.setText(timeStr);
		};
	};
	private String timeStr;

	private GridView gv_lancher;
	// 图标数组
	private int[] icons = { R.drawable.messages, R.drawable.appstore,
			R.drawable.calculator, R.drawable.compass, R.drawable.contacts,
			R.drawable.facetime, R.drawable.ibooks, R.drawable.itunesstore,
			R.drawable.reminders,R.drawable.notes,R.drawable.photos};
	// 图标对应的名称
	private String[] names = new String[] { "Messages", "App Store", "Calculator", "Compass",
			"Contacts", "Face Time", "iBooks", "iTunes Store", "Reminder","Notes","Photos" };
	private TextView tv_time;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置全屏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		tv_time = (TextView) findViewById(R.id.tv_time);
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				long time = System.currentTimeMillis();
				SimpleDateFormat dateFormat = new SimpleDateFormat(
						"yyyy年MM月dd日HH时mm分ss秒");
				timeStr = dateFormat.format(time);
				handler.sendEmptyMessage(0);
			}
		};
		timer.schedule(task, 0, 1000);
		// 格式化时间

		gv_lancher = (GridView) findViewById(R.id.gv_lancher);
		// 为gridview设置适配数据
		gv_lancher.setAdapter(new MyAdapter());
		// 为gridview设置条目点击事件
		gv_lancher.setOnItemClickListener(new MyItemClickLIstener());
		
	}

	private class MyItemClickLIstener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			Toast.makeText(MainActivity.this, names[position]+"is clicked!",Toast.LENGTH_SHORT).show();
		}

	}

	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return icons.length;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			View view;
			if (convertView != null) {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			} else {
				view = View
						.inflate(MainActivity.this, R.layout.list_item, null);
				holder = new ViewHolder();
				holder.iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
				holder.tv_title = (TextView) view.findViewById(R.id.tv_title);
				view.setTag(holder);
			}
			holder.iv_icon.setImageResource(icons[position]);
			holder.tv_title.setText(names[position]);
			return view;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return names[position];
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

	}

	class ViewHolder {
		ImageView iv_icon;
		TextView tv_title;
	}

}
