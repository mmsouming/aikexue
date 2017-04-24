package com.dy.aikexue.ssolibrary.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.dy.aikexue.ssolibrary.R;
import com.dy.aikexue.ssolibrary.util.Tools;
import com.dy.sdk.utils.L;
import com.dy.sdk.utils.ScreenUtil;
import com.dy.sdk.utils.ThemeUtil;
import com.dy.sdk.view.WheelView;

import java.util.List;

/**
 * Created by fuq on 2017/3/6.
 * 底部选择文本对话框
 */
public class SelectBottomDialog extends Dialog implements WheelView.OnSelectItemListener, View.OnClickListener {
    WheelView wheel_str;
    TextView tv_cancel;
    TextView tv_ok;
    private View rootView;
    private List<String> strList;
    private static final int ITEM_COUNT = 5;
    private String curValue;
    OnItemOnClickListener onItemOnClickListener;

    public SelectBottomDialog(Context context) {
        super(context, R.style.BottomDialog);
        this.init();
    }

    private void init() {
        this.rootView = View.inflate(this.getContext(), R.layout.date_select_layout, null);
        this.wheel_str = (WheelView) this.rootView.findViewById(R.id.wheel_year);
        rootView.findViewById(R.id.wheel_month).setVisibility(View.GONE);
        rootView.findViewById(R.id.wheel_day).setVisibility(View.GONE);
        this.tv_cancel = (TextView) this.rootView.findViewById(R.id.tv_cancel);
        this.tv_ok = (TextView) this.rootView.findViewById(R.id.tv_ok);
        this.tv_ok.setTextColor(ThemeUtil.getPressedTextStateSelectReverseDrawable(this.getContext()));
        this.wheel_str.setItemCount(ITEM_COUNT);
        this.tv_ok.setOnClickListener(this);
        this.tv_cancel.setOnClickListener(this);
        this.wheel_str.setOnSelectItemListener(this);
        this.prepareView();
    }

    @Override
    public void show() {
        super.show();
        Window window = this.getWindow();
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        int h = ScreenUtil.getScreenHeight(getContext());
        int h2 = rootView.getMeasuredHeight();
        wl.y = h - h2;
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        // 设置显示位置
        this.onWindowAttributesChanged(wl);
    }

    private void prepareView() {
        setDialogFillWindow();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(rootView);
    }

    /**
     * 设置对话框填充整个窗口
     */
    private void setDialogFillWindow() {
        Window window = this.getWindow();
        //设置弹出的动画效果
        window.setWindowAnimations(R.style.AnimBottom);
    }

    /**
     * 外部调用 设置数据
     *
     * @param datas
     * @param selectData
     */
    public void setData(List<String> datas, String selectData) {
        if (!Tools.isListNotNull(datas)) {
            return;
        }
        this.strList = datas;
        this.curValue = Tools.isStringNull(selectData) ? strList.get(0) : selectData;
        this.wheel_str.setData(this.strList);
        setSelect(curValue, strList, wheel_str);
    }

    private void setSelect(String select, List<String> dataList, WheelView wheelView) {
        if (dataList != null) {
            int index = dataList.indexOf(select);
            if (index >= 0 && index < dataList.size()) {
                wheelView.setSelection(index);
            }
        }

    }

    @Override
    public void onSelectItem(WheelView wheelView, String s, int i) {
        L.debug(s);
        curValue = s;
    }

    public void onClickSelectItem(WheelView view, String str, int position) {
    }

    public void setOnItemOnClickListener(OnItemOnClickListener onItemOnClickListener) {
        this.onItemOnClickListener = onItemOnClickListener;
    }

    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_cancel) {
            if (this.onItemOnClickListener != null) {
                this.onItemOnClickListener.onClickCancel(this);
            }
        } else if (id == R.id.tv_ok && this.onItemOnClickListener != null) {
            curValue = wheel_str.getSelectString();
            this.onItemOnClickListener.onClickOk(this, wheel_str.getSelectString());
        }

    }


    public void dismiss() {
        super.dismiss();
    }

    public interface OnItemOnClickListener {
        void onClickOk(Dialog var1, String curValue);

        void onClickCancel(Dialog var1);
    }
}
