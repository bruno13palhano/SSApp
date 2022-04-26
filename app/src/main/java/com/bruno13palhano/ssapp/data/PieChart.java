package com.bruno13palhano.ssapp.data;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import com.bruno13palhano.ssapp.R;

public class PieChart extends View {
    private boolean mShowText;
    private Integer textPosition;

    public PieChart(Context context, AttributeSet attrs){
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.PieChart,
                0, 0);
        try{
            mShowText = a.getBoolean(R.styleable.PieChart_showText, false);
            textPosition = a.getInteger(R.styleable.PieChart_labelPosition, 0);
        }finally {
            a.recycle();
        }
    }

    public boolean isShowText(){
        return mShowText;
    }

    public void setShowText(boolean showText){
        mShowText = showText;
        invalidate();
        requestLayout();
    }
}
