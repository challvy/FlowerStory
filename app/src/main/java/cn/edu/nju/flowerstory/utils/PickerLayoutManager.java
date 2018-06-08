package cn.edu.nju.flowerstory.utils;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by adityagohad on 06/06/17.
 */

public class PickerLayoutManager extends LinearLayoutManager {

    private float scaleDownBy = 1.0f;
    private float scaleDownDistance = 1.0f;
    private boolean changeAlpha = false;

    private onScrollStopListener onScrollStopListener;
    private int curSelectedIndex = 0;

    public PickerLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
        scaleDownView();
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).setSelected(false);
            getChildAt(i).setAlpha(0.5f);
        }
        getChildAt(curSelectedIndex).setSelected(true);
        getChildAt(curSelectedIndex).setAlpha(1.0f);
        onScrollStopListener.selectedView(getChildAt(curSelectedIndex));
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int orientation = getOrientation();
        if (orientation == HORIZONTAL) {
            int scrolled = super.scrollHorizontallyBy(dx, recycler, state);
            scaleDownView();
            return scrolled;
        } else {
            return 0;
        }
    }

    private void scaleDownView() {
        float mid = getWidth() / 2.0f;
        float unitScaleDownDist = scaleDownDistance * mid;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            float childMid = (getDecoratedLeft(child) + getDecoratedRight(child)) / 2.0f;
            float scale = 1.0f + (-1 * scaleDownBy) * (Math.min(unitScaleDownDist, Math.abs(mid - childMid))) / unitScaleDownDist;
            if(0.83f<=scale && scale<=1.20f){
                child.setSelected(true);
            } else {
                child.setSelected(false);
            }
            if (changeAlpha) {
                child.setAlpha(scale);
            }
        }
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        if (onScrollStopListener != null) {
            float lastHeight = 0f;
            for (int i = 0; i < getChildCount(); i++) {
                if (lastHeight < getChildAt(i).getScaleY()) {
                    lastHeight = getChildAt(i).getScaleY();
                }
            }
            for (int i = 0; i < getChildCount(); i++) {
                if(getChildAt(i).isSelected()) {
                    onScrollStopListener.selectedView(getChildAt(i));
                    getChildAt(i).setAlpha(1.0f);
                    curSelectedIndex = i;
                } else {
                    getChildAt(i).setAlpha(0.5f);
                }
            }
        }
    }

    public float getScaleDownBy() {
        return scaleDownBy;
    }

    public void setScaleDownBy(float scaleDownBy) {
        this.scaleDownBy = scaleDownBy;
    }

    public float getScaleDownDistance() {
        return scaleDownDistance;
    }

    public void setScaleDownDistance(float scaleDownDistance) {
        this.scaleDownDistance = scaleDownDistance;
    }

    public boolean isChangeAlpha() {
        return changeAlpha;
    }

    public void setChangeAlpha(boolean changeAlpha) {
        this.changeAlpha = changeAlpha;
    }

    public void setOnScrollStopListener(onScrollStopListener onScrollStopListener) {
        this.onScrollStopListener = onScrollStopListener;
    }

    public interface onScrollStopListener {
        void selectedView(View view);
    }
}
