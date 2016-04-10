package cn.qiuc.org.islidingmenu;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

public class SlidingMenu extends ViewGroup {


    /** simulated sliding value **/
    private Scroller scroller;
    private View menuView;
    private View mainView;
    private int menuViewWidth;
    private float downX;
    private float destX;
    private float distanceX;

    public SlidingMenu(Context context) {
        super(context);
        init();
    }

    public SlidingMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        scroller = new Scroller(getContext());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //get menu
        menuView = getChildAt(0);
        //main view
        mainView = getChildAt(1);

        menuViewWidth = menuView.getLayoutParams().width;
        menuView.measure(menuViewWidth, heightMeasureSpec);
        mainView.measure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        menuView.layout(l-menuViewWidth, t, l, b);
        mainView.layout(l,t,r,b);
    }

    private boolean isMove;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                distanceX = event.getX() - downX + destX;

                //prevent sliding out of bounds
                if (distanceX > menuViewWidth) {
                    distanceX = menuViewWidth;
                }else if (distanceX < 0) {
                    distanceX = 0;
                }
                scrollTo(distanceX);
                isMove = true;
                break;
            case MotionEvent.ACTION_UP:
                if (!isMove) {
                    //if do not move, do not go to the following code
                    isMove = false;
                    return true;
                }

                if (getMyScrollX() > menuViewWidth / 2) {
                    //if the menu sliding distance is more than the width of 1/2 menu
                    //the menu is fully display
                    destX = menuViewWidth;
                } else {
                    //hide the menu completely
                    destX = 0;
                }
                startScroll();
                break;
            default:
                break;
        }

        //consume touch event
        return true;

    }

    /**
     * start scroll
     */
    private void startScroll() {
        //where to start sliding
        int startX = (int) this.distanceX;
        //sliding distance
        int dx = (int) (destX - distanceX);

        //when is the proportion of the final requirement of what value
        //the unit value as a dividend
        float scale = 1000f / menuViewWidth;
        //the move execution time
        int duration = (int) (Math.abs(dx) * scale);
        Log.d("SlidingMenu.java", "duration=" + duration);
        scroller.startScroll(startX, 0, dx, 0, duration);

        invalidate();
        //call the invalidate method system will recall the drawChild method
        //drawChild will call draw(Canvas, ViewGroup, long)
        //internal draw method will call computerScroll
    }

    @Override
    public void computeScroll() {
//        super.computeScroll();
        if (scroller.computeScrollOffset()) {
            int currX = scroller.getCurrX();
            scrollTo(currX);

            invalidate();
        }
    }

    /**
     * modify the original method
     * when the positive transfer, view sliding to right
     * when the negative transfer, view sliding to left
     * @param distanceX
     */
    private void scrollTo(float distanceX) {
        super.scrollTo((int) -distanceX, 0);
    }

    /**
     * get the position of the slide
     * @return
     */
    public int getMyScrollX() {
        return -super.getScrollX();
    }


    public void openMenu() {
        if (getMyScrollX() == 0) {
            //the original is hidden
            //need to be displayed
            distanceX = 0;
            destX = this.menuViewWidth;
        } else {
            //the original is display
            //need to hide
            distanceX = menuViewWidth;
            destX = 0;
        }

        startScroll();
    }

}
