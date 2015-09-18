package net.xisberto.magicmuzei.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.ImageView;

import net.xisberto.magicmuzei.R;

/**
 * Created by xisberto on 12/09/15.
 */
public class SelectableImageView extends ImageView {
    private Drawable mForegroundSelector;

    public SelectableImageView(Context context) {
        super(context);
        init(null, 0);
    }

    public SelectableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public SelectableImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        TypedArray a = getContext().obtainStyledAttributes(new int[]{R.attr.selectableItemBackground});
        mForegroundSelector = a.getDrawable(0);
        if (mForegroundSelector != null) {
            mForegroundSelector.setCallback(this);
        }
        a.recycle();
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        super.draw(canvas);

        mForegroundSelector.draw(canvas);
    }

    @Override
    protected boolean verifyDrawable(Drawable dr) {
        return super.verifyDrawable(dr) || (dr == mForegroundSelector);
    }

    @Override
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        mForegroundSelector.jumpToCurrentState();
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();

        mForegroundSelector.setState(getDrawableState());

        invalidate();
    }

    @TargetApi(21)
    @Override
    public void drawableHotspotChanged(float x, float y) {
        super.drawableHotspotChanged(x, y);
        if (mForegroundSelector != null) {
            mForegroundSelector.setHotspot(x, y);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mForegroundSelector.setBounds(0, 0, w, h);
    }
}
