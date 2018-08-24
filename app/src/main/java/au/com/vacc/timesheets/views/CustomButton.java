
package au.com.vacc.timesheets.views;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import au.com.vacc.timesheets.R;


public class CustomButton extends AppCompatButton {
    protected boolean isRuby;
    public static final int PRESS_COLORFILTER = 0;
    public static final int PRESS_RESOURCE = 1;
    public static final int STYLE_DARK = 0;
    public static final int STYLE_LIGHT = 1;
    public static final int STYLE_DARKBG_WHITETXT = 2;

    public static final String[] PRESSED_SOURCE = new String[]{"colorFilter", "resource"};
    public static final String[] PRESSED_STYLE = new String[]{"light", "dark"};
    String customFont;
    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        int pressedStyle = attrs.getAttributeIntValue(context.getResources().getString(R.string.xmlns_android)
                , "pressedStyle", 0);

        if (pressedStyle == STYLE_DARKBG_WHITETXT) {
            ColorStateList colors = new ColorStateList(
                    new int[][]{new int[]{android.R.attr.state_pressed}, new int[0]},
                    new int[]{Color.WHITE, getTextColors().getDefaultColor()});
            setTextColor(colors);
        }

        String textSizeResId = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "textSize");
        if (textSizeResId == null) {
            // default text size
            float textSize = getResources().getDimension(R.dimen.dp_15);
            setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        }
        setPadding(10, 10, 10, 10);

        setBackgroundDrawable(getBackgroundStates(this, attrs));
    }

    public void setPressedStyle(int style) {
        setBackgroundDrawable(getBackgroundStateColorFilter(this, style));
    }

    public static Drawable getBackgroundStates(View view, AttributeSet attrs) {
        Drawable background = view.getBackground();
        if (background != null) {
            if (attrs.getAttributeListValue("http://schemas.android.com/apk/res-auto", "pressedSource", CustomButton.PRESSED_SOURCE, 0) == PRESS_RESOURCE) {
                return getBackgroundStateResource(view.getContext(), background,
                        attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "background", -1));
            }
            return getBackgroundStateColorFilter(view,
                    attrs.getAttributeListValue("http://schemas.android.com/apk/res-auto", "pressedStyle", CustomButton.PRESSED_STYLE, 0));
        }
        return null;
    }

    public static LayerDrawable getBackgroundStateColorFilter(View view, int filterStyle) {
        return new LDBackgroundStateColorFilter(view, filterStyle);
    }

    private static StateListDrawable getBackgroundStateResource(Context context, Drawable background, int backgroundResId) {
        if (background != null && backgroundResId > 0) {
            StateListDrawable states = new StateListDrawable();
            Resources resources = context.getResources();
            Drawable backgroundOn = resources.getDrawable(resources.getIdentifier(
                    resources.getResourceEntryName(backgroundResId) + "_on", "drawable", context.getPackageName()));

            if (backgroundOn != null) {
                states.addState(PRESSED_ENABLED_STATE_SET, backgroundOn);
                states.addState(SELECTED_STATE_SET, backgroundOn);
            }
            states.addState(ENABLED_STATE_SET, background);
            return states;
        }
        return null;
    }

    public static class LDBackgroundStateColorFilter extends LayerDrawable {
        public static ColorFilter filterDisabled;
        public static ColorFilter filterPressedLight;
        public static ColorFilter filterPressedDark;

        private int pressedEffect;
        private View view;

        /**
         * @param view          for which the effect is to be applied on
         * @param pressedEffect int {@link #STYLE_DARK} (default), int
         *                      {@link #STYLE_LIGHT}
         */
        public LDBackgroundStateColorFilter(View view, int pressedEffect) {
            super(new Drawable[]{view.getBackground()});
            this.pressedEffect = pressedEffect;
            this.view = view;
        }

        @Override
        protected boolean onStateChange(int[] states) {
            mutate();
            if (view.isEnabled()) {
                if (view.isPressed()) {
                    setColorFilter(getSelectedFilter(pressedEffect));
                } else {
                    clearColorFilter();
                }
            } else {
                setColorFilter(getDisabledFilter());
            }
            invalidateSelf();
            return super.onStateChange(states);
        }

        @Override
        public boolean isStateful() {
            return true;
        }

        /**
         * @param effect int {@link #STYLE_DARK} (default), int
         *               {@link #STYLE_LIGHT}
         */
        private static ColorFilter getSelectedFilter(int effect) {
            if (effect == STYLE_LIGHT) {
                if (filterPressedLight == null) {
                    float scale = 1.5f;
                    ColorMatrix colorMatrix = new ColorMatrix();
                    colorMatrix.set(new float[]{scale, 0, 0, 0, 0, 0, scale, 0, 0, 0, 0, 0, scale, 0, 0, 0, 0, 0, 1, 0});
                    filterPressedLight = new ColorMatrixColorFilter(colorMatrix);
                }
                return filterPressedLight;
            } else {
                if (filterPressedDark == null)
                    filterPressedDark = new PorterDuffColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
                return filterPressedDark;
            }
        }

        private static ColorFilter getDisabledFilter() {
            if (filterDisabled == null) {
                ColorMatrix colorMatrix = new ColorMatrix();
                colorMatrix.setSaturation(0);
                filterDisabled = new ColorMatrixColorFilter(colorMatrix);
            }
            return filterDisabled;
        }

        public int getPressedStyle() {
            return pressedEffect;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        return;
    }


}
