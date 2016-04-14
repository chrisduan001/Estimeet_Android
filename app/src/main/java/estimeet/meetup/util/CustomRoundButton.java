package estimeet.meetup.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;

import estimeet.meetup.R;

/**
 * Created by Hayden on 2016-03-04.
 */
public class CustomRoundButton extends Button {

    private int buttonColour;
    private String buttonText;
    private int buttonRound;
    private Paint paintColorStyle;
    private int buttonTextColour;
    private float scaledTextSize;
    private RectF rect;
    private Rect bounds;

    public CustomRoundButton(Context context, AttributeSet attrs){
        super(context, attrs);

        float buttonTextSize;

        //Retrieve attributes in XML
        TypedArray attributeValueArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomRoundButton,0,0);
        try{
            buttonColour = attributeValueArray.getInteger(R.styleable.CustomRoundButton_customButtonColor,0);
            buttonText = attributeValueArray.getString(R.styleable.CustomRoundButton_customButtonText);
            buttonRound = attributeValueArray.getInteger(R.styleable.CustomRoundButton_customButtonRound, 0);
            buttonTextColour = attributeValueArray.getInteger(R.styleable.CustomRoundButton_customButtonTextColor,0);
            buttonTextSize = attributeValueArray.getFloat(R.styleable.CustomRoundButton_customButtonTextSize, 0);
        } finally{
            attributeValueArray.recycle();
        }

        setFocusable(true);
        setBackgroundColor(Color.WHITE);
        setClickable(true);

        //Initialise
        rect = new RectF(0,0,0,0);
        bounds = new Rect();
        paintColorStyle = new Paint();

        //Converts text size to dp
        scaledTextSize = dpToPixel(buttonTextSize, context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paintColorStyle.setStyle(Paint.Style.FILL);
        paintColorStyle.setAntiAlias(true);
        paintColorStyle.setColor(buttonColour);

        //Gets width and height
        int width = this.getMeasuredWidth();
        int height = this.getMeasuredHeight();

        //Draw rounded rectangle
        rect.set(0,0,width,height);
        canvas.drawRoundRect(rect, buttonRound, buttonRound, paintColorStyle);

        //Changes text colour
        paintColorStyle.setColor(buttonTextColour);
        paintColorStyle.setTextSize(scaledTextSize);

        //Center text vertically and horizontally
        paintColorStyle.getTextBounds(buttonText, 0, buttonText.length(), bounds);
        int xPos = (width / 2) - (bounds.width() / 2);
        int yPos = (int) ((height / 2) - ((paintColorStyle.descent() + paintColorStyle.ascent()) / 2)) ;

        //Draw text
        canvas.drawText(buttonText, xPos, yPos, paintColorStyle);
    }

    public static float dpToPixel(float dipValue,Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }
}
