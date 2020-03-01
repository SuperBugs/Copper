package com.tqz.tqz.toast;

import android.content.Context;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tqz.tqz.R;

public class MyToast extends android.widget.Toast {
    private Context context;
    private String textContent;
    private Toast toast;
    //tost文字填充
    int padding;

    private String time;

    public MyToast(Context context) {
        super(context);
    }

    /**
     * @param context
     * @param textContent
     * @param time        显示时间长：long 短：short
     * @return
     */
    public Toast getToast(Context context, String textContent, String time) {
        this.context = context;
        this.textContent = textContent;
        this.time=time;
        setToast();
        return toast;
    }
    private void setToast() {
        Toast toast1 = new Toast(context);
        toast1.setGravity(Gravity.CENTER, 0, 0);
        LinearLayout ll = new LinearLayout(context);

        //设置背景圆角
        ll.setBackgroundResource(R.drawable.bg_toast_corners);
        TextView tv = new TextView(context);
        tv.setTextColor(context.getResources().getColor(R.color.middle_black));
        tv.setText(textContent);
        tv.setTextSize(context.getResources().getDimension(R.dimen.toast_text));
        padding = (int) context.getResources().getDimension(R.dimen.tosat_text_padding);
        tv.setPadding(padding,padding,padding,padding);
        tv.setAlpha(0.5f);
        ll.setAlpha(0.5f);
        ll.addView(tv);
        toast1.setView(ll);
        //设置显示时间
        if(time=="long"){
            toast1.setDuration(Toast.LENGTH_LONG);
        }else {
            toast1.setDuration(Toast.LENGTH_SHORT);
        }
        toast=toast1;
    }
}
