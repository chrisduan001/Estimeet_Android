package estimeet.meetup.util.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by AmyDuan on 3/07/16.
 */
public class OmnesEditText extends EditText {
    public OmnesEditText(Context context) {
        super(context);
        init(null);
    }

    public OmnesEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OmnesEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public OmnesEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(AttributeSet attrs) {
        OmnesInitializer.initView(attrs, getContext(), this);
    }
}
