package au.com.vacc.timesheets.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;


public class CustomRelativeLayout extends RelativeLayout {
    public CustomRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        setClickable(true);
        setBackgroundDrawable(CustomButton.getBackgroundStates(this, attrs));
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        
        Drawable drawable = getBackground();
        
        int width = Integer.MIN_VALUE;
        int height = Integer.MIN_VALUE;

        if (width == Integer.MIN_VALUE || height == Integer.MIN_VALUE) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            super.onMeasure(width, height);
            
            setMeasuredDimension(width, height);
            
            ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) getLayoutParams();
            params.width = width;
            params.height = height;
            setLayoutParams(params);
        }
    }

}