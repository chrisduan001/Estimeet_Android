package estimeet.meetup.util.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by AmyDuan on 3/07/16.
 */
public class OmnesButton extends Button {
    public OmnesButton(Context context) {
        super(context);
        init(null);
    }

    public OmnesButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public OmnesButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public OmnesButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        OmnesInitializer.initView(attrs, getContext(), this);
    }
}
