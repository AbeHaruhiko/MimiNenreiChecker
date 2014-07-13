package jp.caliconography.android.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import jp.caliconography.android.miminenreichecker.R;

public class CustomFontButtonWithRightIcon extends LinearLayout {
    private static final String TAG = "CustomFontButton";
    private CustomFontTextView mRightIconView;

    public CustomFontButtonWithRightIcon(Context context) {
        super(context);
    }

    public CustomFontButtonWithRightIcon(Context context, AttributeSet attrs) {
        super(context, attrs);

        View v = LayoutInflater.from(context).inflate(R.layout.button_with_right_icon, this);
        mRightIconView = (CustomFontTextView) v.findViewById(R.id.lblRight);
        setRightIcon(context, attrs, mRightIconView);
        CustomFontTextView textView = (CustomFontTextView) v.findViewById(R.id.lblMiddle);
        setText(context, attrs, textView);
        setTextColor(context, attrs, textView);

        setClickable(true);

    }

    private void setRightIcon(Context ctx, AttributeSet attrs, CustomFontTextView rightIconView) {
        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.CustomFontButtonWithRightIcon);
        String icon = a.getString(R.styleable.CustomFontButtonWithRightIcon_icon_right);
        rightIconView.setText(icon);
        a.recycle();
    }

    public String getRightIcon() {
        return mRightIconView.getText().toString();
    }

    public void setRightIcon(String icon) {
        mRightIconView.setText(icon);
    }

    private void setText(Context ctx, AttributeSet attrs, CustomFontTextView textView) {
        TypedArray a = ctx.obtainStyledAttributes(attrs, new int[]{android.R.attr.text, android.R.attr.textColor});
        String text = a.getString(0);
        textView.setText(text);
        a.recycle();
    }

    private void setTextColor(Context ctx, AttributeSet attrs, CustomFontTextView textView) {
        TypedArray a = ctx.obtainStyledAttributes(attrs, new int[]{android.R.attr.textColor});
        ColorStateList textColor = a.getColorStateList(0);
        textView.setTextColor(textColor);
        a.recycle();
    }
}