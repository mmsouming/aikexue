package com.dy.aikexue.ssolibrary.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

import com.dy.aikexue.ssolibrary.R;
import com.dy.sdk.view.WheelView;

import java.util.List;

/**
 * Created by user on 2016/8/10.
 */
public class SingleSelectDialog extends Dialog {
    private List<String> yearList;
    private View rootView;

    public SingleSelectDialog(Context context, List<String> yearList) {
        this(context);
        this.yearList = yearList;

    }

    private SingleSelectDialog(Context context) {
        super(context, R.style.BottomDialog);
        setCanceledOnTouchOutside(false);
    }

    WheelView wheelView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_year_view_layout);
        wheelView = (WheelView) findViewById(R.id.wheelView);
        rootView = findViewById(R.id.rootView);
        wheelView.setItemCount(5);
        wheelView.setData(yearList);
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        wheelView.setOnSelectItemListener(new WheelView.OnSelectItemListener() {
            @Override
            public void onSelectItem(WheelView wheelView, String s, int i) {
                if (onSelectItemListener != null) {
                    onSelectItemListener.selectItem(s, i);
                }
            }
            @Override
           public void onClickSelectItem(WheelView wheelView, String str, int position){
                SingleSelectDialog.this.dismiss();
            }
        });
    }

    @Override
    public void show() {
        super.show();
        setDialogFillWindow();
    }

    /**
     * 设置对话框填充整个窗口
     */
    private void setDialogFillWindow() {
        Window window = this.getWindow();
        // 设置宽高
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        // 设置弹出的动画效果
        window.setWindowAnimations(R.style.animation_bottom_to_top);
    }

    public void setOnSelectItemListener(OnSelectItemListener onSelectItemListener) {
        this.onSelectItemListener = onSelectItemListener;
    }

    OnSelectItemListener onSelectItemListener;

    public interface OnSelectItemListener {
        void selectItem(String str, int position);
    }


}
