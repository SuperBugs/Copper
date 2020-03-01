package com.tqz.tqz.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.tqz.tqz.R;
import com.tqz.tqz.activity.ChangePasswordActivity;
import com.tqz.tqz.activity.ContactUsActivity;
import com.tqz.tqz.activity.FeedBackActivity;
import com.tqz.tqz.activity.HelpActivity;
import com.tqz.tqz.activity.LawActivity;
import com.tqz.tqz.activity.ShareTaskActivity;
import com.tqz.tqz.common.CommonConfig;
import com.tqz.tqz.common.CommonFunction;
import com.tqz.tqz.update.UpdateApp;

import java.io.File;

public class SetFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    private LinearLayout mChangePasswordTv;
    private LinearLayout mLawTv;
    private LinearLayout mContactUsTv;
    private LinearLayout mAppVersionTv;
    private LinearLayout mShareTv;
    private LinearLayout mShareTaskTv;
    private LinearLayout mFeedBackTv;
    private LinearLayout mPinFenTv;
    private LinearLayout mHelpLayout;
    private LinearLayout mCleanLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //为fragment设置布局界面
        View vi = inflater.inflate(R.layout.f_set_layout, container, false);
        mChangePasswordTv=(LinearLayout)vi.findViewById(R.id.f_set_change_password);
        mLawTv=(LinearLayout)vi.findViewById(R.id.f_set_law);
        mAppVersionTv=(LinearLayout)vi.findViewById(R.id.f_set_app_version);
        mContactUsTv=(LinearLayout)vi.findViewById(R.id.f_set_contact_us);
        mShareTv=(LinearLayout)vi.findViewById(R.id.f_set_share);
        mShareTaskTv=(LinearLayout)vi.findViewById(R.id.f_set_share_activity);
        mFeedBackTv=(LinearLayout)vi.findViewById(R.id.f_set_feedback);
        mPinFenTv=(LinearLayout)vi.findViewById(R.id.f_set_pinfen);
        mHelpLayout=(LinearLayout)vi.findViewById(R.id.f_set_help);
        mCleanLayout=(LinearLayout)vi.findViewById(R.id.f_set_clean);
        setView();
        setListener();
        return vi;
    }

    private void setView() {

    }
    private void setListener() {
        mShareTaskTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), ShareTaskActivity.class);
                startActivity(intent);
            }
        });
        mHelpLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), HelpActivity.class);
                startActivity(intent);
            }
        });
        mPinFenTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        mFeedBackTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), FeedBackActivity.class);
                startActivity(intent);
            }
        });
        mShareTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.setType("text/*");

                sendIntent.putExtra(Intent.EXTRA_TEXT, "我发现一款超好用的手机赚钱和省钱软件,赶快和我在铜钱赚领取各种优惠券、参加各类赚钱活动！软件下载链接:http://a.app.qq.com/o/simple.jsp?pkgname=com.tqz.tqz");
                startActivity(sendIntent);
            }
        });
        mAppVersionTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 //检查更新
                UpdateApp updateApp=new UpdateApp();
                updateApp.cheakApp(getActivity(),true);
            }
        });
        mContactUsTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), ContactUsActivity.class);
                startActivity(intent);
            }
        });
        mChangePasswordTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), ChangePasswordActivity.class);
                startActivity(intent);
            }
        });
        mLawTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),LawActivity.class);
                startActivity(intent);
            }
        });
        mCleanLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonFunction commonFunction=new CommonFunction();
                CommonConfig commonConfig=new CommonConfig();
                commonFunction.deleteAllFiles(new File(commonConfig.getCacheDir()));
                Toast toast = Toast.makeText(getActivity(), "清理成功！", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

}

