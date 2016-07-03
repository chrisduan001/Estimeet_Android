package estimeet.meetup.util.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import estimeet.meetup.R;

/**
 * Created by AmyDuan on 2/07/16.
 */
public class OmnesTextView extends TextView {
    public OmnesTextView(Context context) {
        super(context);
        init(null);
    }

    public OmnesTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public OmnesTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public OmnesTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        OmnesInitializer.initView(attrs, getContext(), this);
    }
}
