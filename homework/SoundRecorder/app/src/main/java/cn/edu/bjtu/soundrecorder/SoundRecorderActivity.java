package cn.edu.bjtu.soundrecorder;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileObserver;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.oauth.BaiduOAuth;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cn.edu.bjtu.soundrecorder.utils.CommonUtils;
import cn.edu.bjtu.soundrecorder.utils.MusicFilter;

public class SoundRecorderActivity extends Activity {

    private static final String TAG = "SoundRecorder";
    private FileObserver mFileObserver;

    private final static String mSavePath = (Environment.getExternalStorageDirectory() + "/SoundRecorder/").toString();
    private String mSoundFileName = null;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ArrayList<String> menuList;
    private ArrayAdapter<String> adapter;
    private MediaPlayer mpSound = new MediaPlayer();
    private File dir = new File(mSavePath);//文件路径
    private ImageButton btnRecord;
    private boolean flag = true;
    private MediaRecorder mp;  //录音文件
    private TextView txtView_clock;
    private TextView txtView_clock_mills;//显示毫秒数
    private String InitClock = "00:00:00# 000";
    private long startTime;//记录开始时间

    SeekBar seekBar;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    int hposition = mpSound.getCurrentPosition();
                    int time = mpSound.getDuration();
                    int max = seekBar.getMax();
                    seekBar.setProgress(hposition * max / time);
                    break;
                default:
                    break;
            }
        }
    };

    //录音计时器
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            //recLen++;
            String durtime = ShowOclock(System.currentTimeMillis() - startTime);
            txtView_clock.setText(durtime.split("#")[0]);
            txtView_clock_mills.setText(durtime.split("#")[1]);
            mHandler.postDelayed(this, 2);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!dir.exists()) {
            dir.mkdirs();
        }

        new SyncFilesTask().execute();

        setContentView(R.layout.drawer_layout);
        // mTitle = (String) getTitle();
        if (null == mFileObserver) {
            mFileObserver = new SDCardFileObserver("/sdcard/SoundRecorder/");
            mFileObserver.startWatching();
            Log.d(TAG, "mFileObserver start");
        }

        // 启动Service监听网络变化
        Intent i=new Intent(getApplicationContext(),NetworkStateService.class);
        startService(i);

        /*findViewById(R.id.btnStartRecord).setOnClickListener(btnClickListener);
        findViewById(R.id.btnStopRecord).setOnClickListener(btnClickListener);*/

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        btnRecord = (ImageButton) findViewById(R.id.btnRecord);
        btnRecord.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (flag) {
                    startRecord();
                } else {
                    stopRecord();
                }
            }
        });

        //更新录音列表
        ReflashList();

        seekBar = (SeekBar) findViewById(R.id.seekbar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int dest = seekBar.getProgress();
                int time = mpSound.getDuration();
                int max = seekBar.getMax();

                mpSound.seekTo(time * dest / max);
            }
        });

        txtView_clock = (TextView) findViewById(R.id.txView_clock);
        txtView_clock_mills = (TextView) findViewById(R.id.txView_clockmills);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id){
            case R.id.action_settings:
//                Toast.makeText(this,R.string.action_settings,Toast.LENGTH_SHORT).show();
                Log.d(TAG, "云同步");
                SBaiduPCS.getInstance().test_login(SoundRecorderActivity.this);
                break;
            case R.id.account_setting:
                Toast.makeText(this,R.string.action_account,Toast.LENGTH_SHORT).show();
                break;
            case R.id.quit_setting:
                Toast.makeText(this,R.string.action_quit,Toast.LENGTH_SHORT).show();
                this.finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        int type = this.getResources().getConfiguration().orientation;
        if (type == Configuration.ORIENTATION_LANDSCAPE) {
            // setContentView(R.layout.drawer_layout);
        } else if (type == Configuration.ORIENTATION_PORTRAIT) {
            //setContentView(R.layout.drawer_layout);
        }
        super.onConfigurationChanged(newConfig);
    }

    //将获取的秒转换为时间00:00:00# 000
    public String ShowOclock(long time) {
        if (time >= 360000000) {
            return InitClock;
        }
        String timeCount;
        long hourc = time / 3600000;
        String hour = "0" + hourc;
        hour = hour.substring(hour.length() - 2, hour.length());

        long minuec = (time - hourc * 3600000) / (60000);
        String minue = "0" + minuec;
        minue = minue.substring(minue.length() - 2, minue.length());

        long secc = (time - hourc * 3600000 - minuec * 60000) / 1000;
        String sec = "0" + secc;
        sec = sec.substring(sec.length() - 2, sec.length());

        long millsc = (time - hourc * 3600000 - minuec * 60000) % 1000;
        String mills = "00" + millsc;
        mills = mills.substring(mills.length() - 3, mills.length());

        timeCount = hour + ":" + minue + ":" + sec + "#" + " " + mills;
        return timeCount;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mFileObserver) {
            mFileObserver.stopWatching();
            Log.d(TAG, "mFileObserver stopped");
        }
    }

    private View.OnClickListener btnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnStartRecord:
                    /*btnRecord.setBackgroundResource(R.drawable.record_button_normal);
                    flag = false;*/
                    startRecord();

                    break;
                case R.id.btnStopRecord:
                  /*  btnRecord.setBackgroundResource(R.drawable.pause_button_normal);
                    flag = true;*/
                    stopRecord();

                    break;
                default:
                    break;
            }
        }
    };


    private void initScreen() {
        txtView_clock.setText(InitClock.split("#")[0]);
        txtView_clock_mills.setText(InitClock.split("#")[1]);

    }

    private void startRecord() {
        initScreen();
        if (mp == null) {
            btnRecord.setBackgroundResource(R.drawable.record_button_normal);
            flag = false;
            //dir = new File(Environment.getExternalStorageDirectory(),"sounds");
            File mTmpDir = new File(mSavePath+"temp");
            if (!mTmpDir.exists()) {
                mTmpDir.mkdirs();
            }
            SimpleDateFormat sDateFormat = new SimpleDateFormat(
                    "yyyy-MM-dd_HH-mm-ss");
            String date = sDateFormat.format(new Date(System.currentTimeMillis()));
            mSoundFileName = date+".amr";
            File soundFile = new File(mTmpDir, mSoundFileName);
            if (!soundFile.exists()) {
                try {
                    soundFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            mp = new MediaRecorder();
            mp.setAudioSource(MediaRecorder.AudioSource.MIC);
            mp.setOutputFormat(MediaRecorder.OutputFormat.AMR_WB);
            mp.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB);

            mp.setOutputFile(soundFile.getAbsolutePath());
            try {
                mp.prepare();
                mp.start();
                mHandler.postDelayed(runnable, 10);//开启一个线程计算录音时间
                startTime = System.currentTimeMillis();
                /*findViewById(R.id.btnStartRecord).setEnabled(false);
                findViewById(R.id.btnStopRecord).setEnabled(true);*/


            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d(TAG,"SoundFile:"+mSoundFileName);
        }
    }

    private void stopRecord() {

        if (mp != null) {
            mp.stop();
            mp.release();
            mp = null;

            btnRecord.setBackgroundResource(R.drawable.pause_button_normal);
            flag = true;

            //清除计时器
            mHandler.removeCallbacks(runnable);

            CommonUtils.moveFile(mSavePath + "temp/" + mSoundFileName, mSavePath);

            ReflashList(); //刷新List
           /* findViewById(R.id.btnStartRecord).setEnabled(true);
            findViewById(R.id.btnStopRecord).setEnabled(true);*/
            initScreen();

//            mBaiduPCS.test_upload(mSavePath + mSoundFileName, mBaiduPCS.mbRootPath, mSoundFileName);
        }
    }

    private void ReflashList() {
        //此处从本地读取声音文件并显示在List上
        menuList = new ArrayList<String>();
        if (!dir.exists()) {
            dir.mkdirs();
        }
        if (dir.listFiles(new MusicFilter()).length > 0) {
            for (File file : dir.listFiles(new MusicFilter())) {
                menuList.add(file.getName());
            }
            adapter = new ArrayAdapter<String>(SoundRecorderActivity.this, android.R.layout.simple_list_item_1, menuList);

            mDrawerList.setAdapter(adapter);
            //点击播放声音
            mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //动态插入一个fragment到framelayout中
                    Fragment contentFragment = new ContentFragment();
                    initScreen();
                    Bundle args = new Bundle();
                    args.putString("text", menuList.get(position));
                    contentFragment.setArguments(args);
                    FragmentManager fm = getFragmentManager();
                    fm.beginTransaction().replace(R.id.content_frame, contentFragment).commit();
                    //播放
                    mpSound = new MediaPlayer();
                    String song = dir.getAbsolutePath() + File.separator + menuList.get(position);

                    try {

                        mpSound.setDataSource(song);
                        mpSound.prepare();
                        final int milliseconds = 5;
                        new Thread() {
                            @Override
                            public void run() {
                                while (true) {
                                    try {
                                        sleep(milliseconds);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    mHandler.sendEmptyMessage(0);
                                }
                            }
                        }.start();
                        mpSound.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mDrawerLayout.closeDrawer(mDrawerList);
                }
            });
        }
    }

    private class SyncFilesTask extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... params) {
            Log.d(TAG,"SyncFilesTask");
            if(CommonUtils.isWifiConnected(getApplicationContext())) {
                Log.d(TAG,"SyncFilesTask2");
                SBaiduPCS.getInstance().test_downloadAll();
            }
            return null;
        }
    }

    class SDCardFileObserver extends FileObserver {
        SDCardFileObserver(String path) {
            super(path);
        }

        SDCardFileObserver(String path, int mask) {
            super(path, mask);
        }

        @Override
        public void onEvent(int event, final String file) {
            int action = event & FileObserver.ALL_EVENTS;
            switch (action) {
//                case FileObserver.ACCESS:
//                    Log.d(TAG, "文件或目录被访问, file: " + file);
//                    break;
//                case FileObserver.ATTRIB:
//                    Log.d(TAG, "文件属性被修改, file: " + file);
//                    break;
//                case FileObserver.CLOSE_NOWRITE:
//                    Log.d(TAG, "不可写文件被close, file: " + file);
//                    break;
//                case FileObserver.CLOSE_WRITE:
//                    // TODO
//                    Log.d(TAG, "可写文件被close, file: " + file);
//                    new Thread() {
//                        @Override
//                        public void run() {
//                            super.run();
////                            mBaiduPCS.test_upload(mSavePath + file, mBaiduPCS.mbRootPath, file);
////                            test_upload("/sdcard/SoundRecorder/" + file, mbRootPath, file);
//                        }
//                    }.start();
//                    break;
//                case FileObserver.CREATE:
//                    Log.d(TAG, "创建新文件, file: " + file);
//                    break;
                case FileObserver.DELETE:
                    Log.d(TAG, "文件或目录被删除, file: " + file);
                    // TODO
                    break;
//                case FileObserver.DELETE_SELF:
//                    Log.d(TAG, "自删除, file: " + file);
//                    break;
//                case FileObserver.MODIFY:
//                    Log.d(TAG, "文件或目录被修改, file: " + file);
//                    break;
                case FileObserver.MOVE_SELF:
                    Log.d(TAG, "自移动, file: " + file);
                    break;
                case FileObserver.MOVED_FROM:
                    Log.d(TAG, "文件被移走, file: " + file);
                    break;
                case FileObserver.MOVED_TO:
                    Log.d(TAG, "文件被移来, file: " + file);
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            if(CommonUtils.isWifiConnected(getApplicationContext())) {
                                Log.d(TAG,"WIFI连接，上传文件");
                                SBaiduPCS.getInstance().test_upload(mSavePath + file, SBaiduPCS.getInstance().mbRootPath, file);
                            }
                            else {
                                Log.d(TAG,"非WIFI，等待");
                            }
                        }
                    }.start();
                    break;
//                case FileObserver.OPEN:
//                    Log.d(TAG, "文件或目录被打开, file: " + file);
//                    break;
            }
        }
    }


}