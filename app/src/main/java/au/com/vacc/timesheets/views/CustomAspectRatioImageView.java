package au.com.vacc.timesheets.views;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

public class CustomAspectRatioImageView extends AppCompatImageView {

    public CustomAspectRatioImageView(Context context) {
        super(context);
    }

    public CustomAspectRatioImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomAspectRatioImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
 	   if (getDrawable() != null) {
	        int width = MeasureSpec.getSize(widthMeasureSpec);  
	        int height = width * getDrawable().getIntrinsicHeight() / getDrawable().getIntrinsicWidth();
	        setMeasuredDimension(width, height);
	    } else {
	        setMeasuredDimension(0, 0);
	    }
    }
}