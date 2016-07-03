package estimeet.meetup.util.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import estimeet.meetup.R;

/**
 * Created by AmyDuan on 3/07/16.
 */
public class OmnesInitializer {
    public static void initView(AttributeSet attrs, Context context, TextView view) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.OmnesTextView);
            String typeface = a.getString(R.styleable.OmnesTextView_typeface);

            if (!TextUtils.isEmpty(typeface)) {
                Typeface omnesTypeface = null;
                switch (typeface) {
                    case "regular":
                        omnesTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/Omnes-Regular.otf");
                        break;
                    case "medium":
                        omnesTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/Omnes-Medium.otf");
                        break;
                    case "semiBold":
                        omnesTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/Omnes-Semibold.otf");
                        break;
                }
                if (omnesTypeface != null) {
                    view.setTypeface(omnesTypeface);
                }
            }

            a.recycle();
        }
    }
}
