package com.tqz.tqz.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.tqz.tqz.R;
import com.tqz.tqz.common.CommonDateFunction;
import com.tqz.tqz.common.CommonFunction;
import com.tqz.tqz.fragment.UserFragment;
import com.tqz.tqz.post.DiskCache;
import com.tqz.tqz.toast.MyToast;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

public class ChangeHeadImageActivity extends Activity {
    private ImageView mUserHeadVi;
    private Button mCameraBtn;
    private Button mAlbumBtn;
    private Bitmap mUserHead;
    private CommonFunction commonFunction;
    private DiskCache mDiskCache;
    private Activity activity;
    private Handler showHeadHandler;
    private MyToast myToast;
    private Dialog dialog;
    private String mImageUrl;
    private CommonDateFunction commonDateFunction;
    private ArrayList<HashMap<String, String>> data;
    private String userPhoneNumber;
    private static final String TAG = "ChangeHeadImageActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.activity_change_head_image);
        mCameraBtn = (Button) findViewById(R.id.btn_camera);
        mAlbumBtn = (Button) findViewById(R.id.btn_albumBtn);
        mUserHeadVi = (ImageView) findViewById(R.id.iv_user_head);
        commonFunction = new CommonFunction();
        mDiskCache = new DiskCache();
        commonDateFunction = new CommonDateFunction();
        //显示用户界面的头像
        mUserHeadVi.setScaleType(ImageView.ScaleType.FIT_XY);
        mUserHeadVi.setImageBitmap(UserFragment.mUserHead);
        data = (ArrayList<HashMap<String, String>>) commonFunction.getDate("userDate", activity);
        userPhoneNumber = data.get(0).get("account");
        //接受修改信息，0表示通知用户修改成功显示网络图片，1表示通知用户修改失败显示默认图片
        showHeadHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        //通知用户修改成功
                        mUserHead = commonFunction.makeCirculaImage(mUserHead);
                        mUserHeadVi.setImageBitmap(mUserHead);
                        //为用户界面设置修改头像
                        UserFragment.mUserHeadVi.setImageBitmap(mUserHead);
                        UserFragment.mUserHead = mUserHead;
//                        myToast = newest MyToast(activity);
//                        myToast.getToast(activity, "修改成功", "short").show();
                        Toast toast = Toast.makeText(activity, getString(R.string.
                                toast_change_user_head_true), Toast.LENGTH_SHORT);
                        toast.show();
                        //头像url保存到本地
                        //修改本地用户数据
                        if (data != null) {
                            data.get(0).put("head", mImageUrl);
                            commonFunction.putDate("userDate", data, activity);
                        }
                        //将图片保存到本地
                        mDiskCache.put(mImageUrl, mUserHead, activity);
                        dialog.dismiss();
                        break;
                    case 1:
                        //通知用户修改失败，显示图片不变
//                        myToast = newest MyToast(activity);
//                        myToast.getToast(activity, "失败", "short").show();
                        Toast toastTwo = Toast.makeText(activity,getString(R.string.
                                toast_change_user_head_false), Toast.LENGTH_SHORT);
                        toastTwo.show();
                        break;
                }
                //结束此activity
                finish();
            }
        };

        mCameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //系统常量， 启动相机的关键
                Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(openCameraIntent, 0);
            }
        });
        mAlbumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //启动图库的intent，获取图片
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.
                        Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
            }
        });
    }

    //获取返回结果，保存获取的图片，上传到服务器
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            switch (requestCode) {

                case 0:
                    //从相机返回
                    Uri imageCameraUri = data.getData();
                    Bundle bundle = data.getExtras();
                    // 获取相机返回的数据，并转换为Bitmap图片格式
                    mUserHead = (Bitmap) bundle.get("data");
                    if (data.getData() != null) {
                        imageCameraUri = data.getData();
                    } else {
                        imageCameraUri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), mUserHead, null, null));
                    }
                    //裁剪图片
                    startPhotoRoom(imageCameraUri);
                    break;

                case 1:
                    //从图库返回
                    Uri imageCaptureUri = data.getData();
                    //裁剪图片
                    startPhotoRoom(imageCaptureUri);
                    break;
                case 2:
                    //获取裁剪后的图片，上传到服务器
                    Uri imageUri = data.getData();
                    if (imageUri != null) {
                        try {
                            //获取图片
                            mUserHead = BitmapFactory.decodeStream(
                                    getContentResolver().openInputStream(imageUri));
                            dialog = ProgressDialog.show(ChangeHeadImageActivity.this, null, getString(R.string.dialog_change_head_now), false, true);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    //将图片发送到服务器，获得返回的图片url
                                    mImageUrl = commonDateFunction.postUserHeadImage(mUserHead, activity, userPhoneNumber);
                                    if (mImageUrl.equals("false")) {
                                        //通知用户失败
                                        Message message = new Message();
                                        message.what = 1;
                                        showHeadHandler.sendMessage(message);
                                    } else {
                                        //通知用户成功
                                        Message message = new Message();
                                        message.what = 0;
                                        showHeadHandler.sendMessage(message);
                                    }

                                }
                            }).start();

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    } else {
                        //通知用户失败
                        Message message = new Message();
                        message.what = 1;
                        showHeadHandler.sendMessage(message);
                    }
                    break;
            }

        } else {
            //通知用户系统错误
            Message message = new Message();
            message.what = 1;
            showHeadHandler.sendMessage(message);
        }

    }


    //根据图片地址裁剪图片
    private void startPhotoRoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        //crop=true设置在开启的Intent中设置显示的view可裁剪
        intent.putExtra("crop", "true");
        //aspectX aspectY是宽高比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //outputX outputY是裁剪图片的宽高
        intent.putExtra("outputX", 500);
        intent.putExtra("outputY", 500);
        intent.putExtra("return-date", true);
        startActivityForResult(intent, 2);
    }
    //获取返回键点击事件
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == android.view.KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

