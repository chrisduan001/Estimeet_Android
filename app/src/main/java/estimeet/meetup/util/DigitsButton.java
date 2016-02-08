package estimeet.meetup.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.digits.sdk.android.DigitsAuthButton;

import estimeet.meetup.R;

/**
 * Created by AmyDuan on 8/02/16.
 */
public class DigitsButton extends DigitsAuthButton {
    public DigitsButton(Context context) {
        super(context);
    }

    public DigitsButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DigitsButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setupButton() {
        Resources res = this.getResources();
        this.setCompoundDrawablePadding(res.getDimensionPixelSize(com.digits.sdk.android.R.dimen.tw__login_btn_drawable_padding));
        this.setText("Phone number sign up");
        this.setTextColor(res.getColor(com.digits.sdk.android.R.color.tw__solid_white));
        this.setTextSize(0, (float) res.getDimensionPixelSize(com.digits.sdk.android.R.dimen.tw__login_btn_text_size));
        this.setTypeface(Typeface.DEFAULT_BOLD);
        this.setPadding(res.getDimensionPixelSize(com.digits.sdk.android.R.dimen.tw__login_btn_right_padding), 0, res.getDimensionPixelSize(com.digits.sdk.android.R.dimen.tw__login_btn_right_padding), 0);
        this.setBackgroundResource(R.drawable.digits_button);
    }
}
