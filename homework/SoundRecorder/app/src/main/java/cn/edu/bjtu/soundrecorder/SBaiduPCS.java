package cn.edu.bjtu.soundrecorder;

import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.baidu.oauth.BaiduOAuth;
import com.baidu.pcs.BaiduPCSActionInfo;
import com.baidu.pcs.BaiduPCSClient;
import com.baidu.pcs.BaiduPCSStatusListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Jake on 2015/1/9.
 */
public class SBaiduPCS {
    // TAG
    private static final String TAG = "SBaiduPCS";
    // the api key
    private final static String mbApiKey = "L6g70tBRRIXLsY0Z3HwKqlRE"; //your app_key";
    // the default root folder
    public final String mbRootPath = "/apps/pcstest_oauth/SoundRecorder/";
    private String mbOauth = "23.245c040d7c8a1c769de9db48dd1c95d4.2592000.1423293365.538706709-238347";

    // the handler
    private Handler mbUiThreadHandler = null;

    private static SBaiduPCS ourInstance = new SBaiduPCS();

    public static SBaiduPCS getInstance() {
        return ourInstance;
    }

    private SBaiduPCS() {
        mbUiThreadHandler = new Handler();
    }

    //
    // get quota
    //
    public void test_getQuota() {

        if (null != mbOauth) {

            Thread workThread = new Thread(new Runnable() {
                public void run() {
                    BaiduPCSClient api = new BaiduPCSClient();
                    api.setAccessToken(mbOauth);
                    api.quota();
                    final BaiduPCSActionInfo.PCSQuotaResponse info = api.quota();

                    mbUiThreadHandler.post(new Runnable() {
                        public void run() {
                            if (null != info) {
                                if (0 == info.status.errorCode) {
//                                    Toast.makeText(ContextUtil.getInstance(), "Quota :" + info.total + "  used: " + info.used, Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "Quota :" + info.total + "  used: " + info.used);
                                } else {
                                    Toast.makeText(ContextUtil.getInstance(), "Quota failed: " + info.status.errorCode + "  " + info.status.message, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                }
            });

            workThread.start();
        }
    }

    //
    // login
    //
    public void test_login() {
        Log.d(TAG, "Login");
        BaiduOAuth oauthClient = new BaiduOAuth();
        oauthClient.startOAuth(ContextUtil.getInstance(), mbApiKey, new String[]{"basic", "netdisk"}, new BaiduOAuth.OAuthListener() {
            @Override
            public void onException(String msg) {
                Toast.makeText(ContextUtil.getInstance(), "Login failed " + msg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete(BaiduOAuth.BaiduOAuthResponse response) {
                if (null != response) {
                    Log.d(TAG, "Login2");
                    mbOauth = response.getAccessToken();

                    Log.d(TAG, "Login3");
                    Log.d(TAG, "mbOauth: " + mbOauth);
                    Toast.makeText(ContextUtil.getInstance(), "Token: " + mbOauth + "    User name:" + response.getUserName(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancel() {
                Toast.makeText(ContextUtil.getInstance(), "Login cancelled", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //
    // get quota
    //
    public void test_upload(final String uploadFile, final String path, final String name) {

        if (null != mbOauth) {
            Log.d(TAG,"Upload start");

            Thread workThread = new Thread(new Runnable() {
                public void run() {

//                    String tmpFile = "/mnt/sdcard/myDoc/htl.png";
                    //	String tmpFile = "/mnt/sdcard/DCIM/File/1.txt";

                    BaiduPCSClient api = new BaiduPCSClient();
                    api.setAccessToken(mbOauth);

//                    final BaiduPCSActionInfo.PCSFileInfoResponse response = api.uploadFile(tmpFile, mbRootPath + "/htl.png", new BaiduPCSStatusListener() {
                    final BaiduPCSActionInfo.PCSFileInfoResponse response = api.uploadFile(uploadFile, path + name, new BaiduPCSStatusListener() {

                        @Override
                        public void onProgress(long bytes, long total) {
                            // TODO Auto-generated method stub


                            final long bs = bytes;
                            final long tl = total;

                            mbUiThreadHandler.post(new Runnable() {
                                public void run() {
                                    Toast.makeText(ContextUtil.getInstance(), "total: " + tl + "    sent:" + bs, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public long progressInterval() {
                            return 1000;
                        }
                    });

                    mbUiThreadHandler.post(new Runnable() {
                        public void run() {
                            Toast.makeText(ContextUtil.getInstance(), response.status.errorCode + "  " + response.status.message + "  " + response.commonFileInfo.blockList, Toast.LENGTH_SHORT).show();
                            if (0 == response.status.errorCode)
                                Log.d(TAG, uploadFile + "已上传至" + path + name);
                            else
                                Log.d(TAG, "上传失败 "+response.status.message);
                        }
                    });
                }
            });

            workThread.start();
        }
    }

    //
    // test delete
    //
    public void test_delete() {
        if (null != mbOauth) {

            Thread workThread = new Thread(new Runnable() {
                public void run() {

                    BaiduPCSClient api = new BaiduPCSClient();
                    api.setAccessToken(mbOauth);

                    List<String> files = new ArrayList<String>();
                    files.add(mbRootPath + "/" + "198.jpg");
                    files.add(mbRootPath + "/" + "2.jpg");
                    files.add(mbRootPath + "/" + "3.jpg");

                    final BaiduPCSActionInfo.PCSSimplefiedResponse ret = api.deleteFiles(files);

                    mbUiThreadHandler.post(new Runnable() {
                        public void run() {
                            Toast.makeText(ContextUtil.getInstance(), "Delete files:  " + ret.errorCode + "  " + ret.message, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            workThread.start();
        }
    }

    //
    // download file
    //
    public void test_download(final String source, final String target) {
        if (null != mbOauth) {

            Thread workThread = new Thread(new Runnable() {
                public void run() {

                    BaiduPCSClient api = new BaiduPCSClient();
                    api.setAccessToken(mbOauth);
//                    String source = mbRootPath + "/189.jpg";
//                    String target = "/mnt/sdcard/DCIM/100MEDIA/yytest0801.mp4";
                    final BaiduPCSActionInfo.PCSSimplefiedResponse ret = api.downloadFileFromStream(source, target, new BaiduPCSStatusListener() {
                        @Override
                        public void onProgress(long bytes, long total) {
                            // TODO Auto-generated method stub
                            final long bs = bytes;
                            final long tl = total;

                            mbUiThreadHandler.post(new Runnable() {
                                public void run() {
                                    Toast.makeText(ContextUtil.getInstance(), "total: " + tl + "    downloaded:" + bs, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public long progressInterval() {
                            return 500;
                        }

                    });

                    mbUiThreadHandler.post(new Runnable() {
                        public void run() {
                            Toast.makeText(ContextUtil.getInstance(), "Download files:  " + ret.errorCode + "   " + ret.message, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            workThread.start();
        }
    }


    //
    // mkdir
    //
    public void test_mkdir() {
        if (null != mbOauth) {

            Thread workThread = new Thread(new Runnable() {
                public void run() {

                    BaiduPCSClient api = new BaiduPCSClient();
                    api.setAccessToken(mbOauth);
                    String path = mbRootPath + "/" + "JakeDu";

                    final BaiduPCSActionInfo.PCSFileInfoResponse ret = api.makeDir(path);

                    mbUiThreadHandler.post(new Runnable() {
                        public void run() {
                            Toast.makeText(ContextUtil.getInstance(), "Mkdir:  " + ret.status.errorCode + "   " + ret.status.message, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            workThread.start();
        }
    }

    //
    // meta file
    //
    public void test_meta() {
        if (null != mbOauth) {

            Thread workThread = new Thread(new Runnable() {
                public void run() {

                    BaiduPCSClient api = new BaiduPCSClient();
                    api.setAccessToken(mbOauth);
                    String path = mbRootPath + "/JakeDu/Ezreal.mp3";

                    final BaiduPCSActionInfo.PCSMetaResponse ret = api.meta(path);

                    mbUiThreadHandler.post(new Runnable() {
                        public void run() {

                            String extra = null;

                            switch (ret.type) {
                                case Media_Audio:
                                    BaiduPCSActionInfo.PCSAudioMetaResponse audioInfo = (BaiduPCSActionInfo.PCSAudioMetaResponse) ret;
                                    if (null != audioInfo) {
                                        extra = audioInfo.trackTitle;
                                    }
                                    break;

                                case Media_Video:
                                    BaiduPCSActionInfo.PCSVideoMetaResponse videoInfo = (BaiduPCSActionInfo.PCSVideoMetaResponse) ret;
                                    if (null != videoInfo) {
                                        extra = videoInfo.resolution;
                                    }
                                    break;

                                case Media_Image:
                                    BaiduPCSActionInfo.PCSImageMetaResponse imageInfo = (BaiduPCSActionInfo.PCSImageMetaResponse) ret;
                                    if (null != imageInfo) {
                                        extra = imageInfo.latitude + "  " + imageInfo.longtitude;
                                    }
                                    break;
                            }

                            Toast.makeText(ContextUtil.getInstance(), "Meta:  " + ret.status.errorCode + "   " + ret.status.message + "  " + extra, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            workThread.start();
        }
    }

    //
    // list
    //
    public void test_list() {
        if (null != mbOauth) {

            Thread workThread = new Thread(new Runnable() {
                public void run() {

                    BaiduPCSClient api = new BaiduPCSClient();
                    api.setAccessToken(mbOauth);
                    String path = mbRootPath;

                    final BaiduPCSActionInfo.PCSListInfoResponse ret = api.list(path, "name", "asc");
                    //final BaiduPCSActionInfo.PCSListInfoResponse ret = api.imageStream();
//                    Log.d(TAG,ret.list.toString());
                    List files = ret.list;
                    Log.d(TAG, "当前List文件数量：" + files.size());

                    for (Iterator i = files.iterator(); i.hasNext(); ) {
//                        long fsId = (String) i.next();
                        BaiduPCSActionInfo.PCSCommonFileInfo file = (BaiduPCSActionInfo.PCSCommonFileInfo) i.next();
                        Log.d(TAG, "file " + file.toString());
                        Log.d(TAG, "文件或是目录的Md5值 " + file.blockList);
                        Log.d(TAG, "文件的路径" + file.path);
                        Log.d(TAG, "文件的服务器创建时间，以毫秒表示 " + file.cTime);
                        Log.d(TAG, "文件在PCS的临时唯一标识id " + file.fsId);
                        Log.d(TAG, "该路径下是否包括有子目录 " + file.hasSubFolder);
                        Log.d(TAG, "当前路径是否是目录， 该参数在一些方法中没有意义 " + file.isDir);
                        Log.d(TAG, "文件的服务器修改时间,以毫秒表示 " + file.mTime);
                        Log.d(TAG, "文件或是目录的大小 " + file.size);
                    }

                    mbUiThreadHandler.post(new Runnable() {
                        public void run() {
                            Toast.makeText(ContextUtil.getInstance(), "List:  " + ret.status.errorCode + "    " + ret.status.message, Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            });

            workThread.start();
        }
    }

    //
    // diff
    //
    public void test_diff() {
        if (null != mbOauth) {

            Thread workThread = new Thread(new Runnable() {
                public void run() {

                    BaiduPCSClient api = new BaiduPCSClient();
                    api.setAccessToken(mbOauth);

                    final BaiduPCSActionInfo.PCSDiffResponse ret = api.diff("lPoXQ82tTNeQi17NfzbqlefWLhWlMZzDqioifhVxuA1ZMIOK3Da4gAEep+KIyVue3Iuy+tEQn9CpBPg8C4p8Imt7ypPiaLhF8ShPiNctAUBrtXcKhX/O80LUnmlhtwWosB3bJtl9i99y5QFE6zNAwEae5PL1JxAkxi3vQoNr2XYLnGv2r/u08o3SW0axqqj6qRo3f9rFxX36CkQhWZUGG7XOelgBPlus0d7CGObNs9ltH9OustCKLiTQXG2G96Ap");
                    mbUiThreadHandler.post(new Runnable() {
                        public void run() {
                            Toast.makeText(ContextUtil.getInstance(), "Diff:  " + ret.status.errorCode + "   " + ret.status.message + "  " + ret.entries.size(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            workThread.start();
        }
    }

    //
    //logout
    //
    public void test_logout() {
        if (null != mbOauth) {

            /**
             * you can call this method to logout in Android 2.X
             */
            Thread workThread = new Thread(new Runnable() {
                @Override
                public void run() {

                    BaiduOAuth oauth = new BaiduOAuth();
                    final boolean ret = oauth.logout(mbOauth);
                    mbUiThreadHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ContextUtil.getInstance(), "Logout " + ret, Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            });

            workThread.start();
        }

    }

}
