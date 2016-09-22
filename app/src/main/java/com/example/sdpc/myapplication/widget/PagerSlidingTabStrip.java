package com.example.sdpc.myapplication.widget;

import android.app.ActionBar;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Animatable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.CallSuper;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.sdpc.myapplication.R;
import com.example.sdpc.myapplication.widget.interfaces.TabAnimatable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PagerSlidingTabStrip extends HorizontalScrollView {

    private static final String TAG = PagerSlidingTabStrip.class.getSimpleName();
    private static final int LAYOUT_STATE_NORMAL = 0;
    private static final int LAYOUT_STATE_IMPORTANCE = 1;
    private static final boolean mHasOverlappingRendering = false;


    // @formatter:off
    private static final int[] ATTRS = new int[]{android.R.attr.textSize,
            android.R.attr.textColor};
    // @formatter:on
    private LinearLayout.LayoutParams defaultTabLayoutParams;

    private final PageListener internalPageListener = new PageListener();
    private OnPageChangeListener delegatePageListener;
    private List<OnPageChangeListener> delegatePageListeners;
    private OnTabItemFocusChangeListener mOnTabItemFocusChangeListener;


    private LinearLayout tabsContainer;
    private ViewPager pager;
    private PagerAdapter mAdapter;
    private TabStripObserver mObserver;

    private int tabCount;
    private int selectedPosition = 0;

    private int tabPadding = 20;
    private int tabTextSize = 12;
    private int tabTextColor = 0x8fffffff;
    private int selectedTabTextColor = 0x0fffffff;
    private Typeface tabTypeface = null;
    private int tabTypefaceStyle = Typeface.NORMAL;

    private int lastScrollX = 0;

    private int tabBackgroundResId = android.R.color.transparent;

    private Locale locale;
    private final State mState = new State();

    public PagerSlidingTabStrip(Context context) {
        this(context, null);
    }

    public PagerSlidingTabStrip(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PagerSlidingTabStrip(Context context, AttributeSet attrs,
                                int defStyle) {
        super(context, attrs, defStyle);
        setClipChildren(false);
        setClipToPadding(false);
        setFillViewport(true);
        setWillNotDraw(false);
        setChildrenDrawingOrderEnabled(mHasOverlappingRendering);
        setSmoothScrollingEnabled(false);
        //set left and right edge to indicate that this line have more items
        setHorizontalFadingEdgeEnabled(true);
        setFadingEdgeLength(100);

        addOnLayoutChangeListener(new OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                Log.d(TAG, "on layout changed");
                //the first time this view get's its layout  ...
                invokeGradient();
            }
        });

        //init container that holds the tabItems
        tabsContainer = new LinearLayout(context);
        tabsContainer.setOrientation(LinearLayout.HORIZONTAL);
        tabsContainer.setLayoutParams(new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(tabsContainer);
        //end init

        DisplayMetrics dm = getResources().getDisplayMetrics();

        tabPadding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, tabPadding, dm);
        tabTextSize = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, tabTextSize, dm);

        // get system attrs (android:textSize and android:textColor)

        TypedArray a = context.obtainStyledAttributes(attrs, ATTRS);

        tabTextSize = a.getDimensionPixelSize(0, tabTextSize);
        tabTextColor = a.getColor(1, tabTextColor);
        a.recycle();

        // get custom attrs

        a = context.obtainStyledAttributes(attrs,
                R.styleable.PagerSlidingTabStrip);

        tabPadding = a.getDimensionPixelSize(
                R.styleable.PagerSlidingTabStrip_pstsTabPaddingLeftRight,
                tabPadding);
        tabBackgroundResId = a.getResourceId(
                R.styleable.PagerSlidingTabStrip_pstsTabBackground,
                tabBackgroundResId);
        a.recycle();

        defaultTabLayoutParams = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        if (locale == null) {
            locale = getResources().getConfiguration().locale;
        }

    }


    private OnShowImportanceListener mImportanceListener;

    public void setViewPager(ViewPager pager) {
        this.pager = pager;
        if (pager.getAdapter() == null) {
            throw new IllegalStateException(
                    "ViewPager does not have adapter instance.");
        }
        pager.setOnPageChangeListener(internalPageListener);

        if (mAdapter != null) {
            mAdapter.unregisterDataSetObserver(mObserver);
        }
        mAdapter = pager.getAdapter();

        if (mAdapter != null) {
            if (mObserver == null) {
                mObserver = new TabStripObserver();
            }
            mAdapter.registerDataSetObserver(mObserver);
        }

        notifyDataSetChanged();

    }

    @Deprecated
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        this.delegatePageListener = listener;
    }

    public void addOnPageChangeListener(OnPageChangeListener listener) {
        if (delegatePageListeners == null) {
            delegatePageListeners = new ArrayList<OnPageChangeListener>();
        }
        delegatePageListeners.add(listener);
    }

    public void removeOnPageChangeListener(OnPageChangeListener listener) {
        if (delegatePageListeners != null) {
            delegatePageListeners.remove(listener);
        }
    }

    public void clearOnPageChangeListener() {
        if (delegatePageListeners != null) {
            delegatePageListeners.clear();
        }
    }

    public void notifyDataSetChanged() {

        tabsContainer.removeAllViews();

        tabCount = mAdapter.getCount();

        for (int i = 0; i < tabCount; i++) {
            if (mAdapter instanceof IconTabProvider) {
                addIconTab(i, ((IconTabProvider) mAdapter)
                        .getPageIconResId(i));
            } else {
                addTextTab(i, mAdapter.getPageTitle(i).toString());
            }

        }
        if (selectedPosition > tabCount) {
            selectedPosition = tabCount - 1;
        }

        updateTabStyles();
        getViewTreeObserver().addOnGlobalLayoutListener(
                new OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        getViewTreeObserver()
                                .removeGlobalOnLayoutListener(this);
                        scrollToChild(selectedPosition);
                    }
                });

    }

    private void addTextTab(final int position, String title) {
        TabItem tabItem = new TabItem(getContext());
        AlphaGradientTextView tabText = tabItem.getTabTextView();
        tabText.setText(title);
        tabText.setGravity(Gravity.CENTER);
        tabText.setTextSize(TypedValue.COMPLEX_UNIT_PX, tabTextSize);
        addTab(position, tabItem);
    }

    private void addIconTab(final int position, int resId) {
        ImageButton tab = new ImageButton(getContext());
        tab.setImageResource(resId);
        addTab(position, tab);
    }

    private void addTab(final int position, View tab) {
        tab.setFocusable(true);
        tab.setFocusableInTouchMode(true);
        tab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(position);
            }
        });
        tab.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    pager.setCurrentItem(position);
                }

                if (mOnTabItemFocusChangeListener != null) {
                    mOnTabItemFocusChangeListener.onFocusChange(v, hasFocus, position);
                }
            }
        });

        tabsContainer.addView(tab, position, defaultTabLayoutParams);
    }

    /**
     * return the tabItem at specific position as a {@link View}
     *
     * @param position
     * @return
     */
    public View getTabItem(int position) {
        if (!checkIndex(position)) {
            return null;
        }
        return tabsContainer.getChildAt(position);
    }

    public void startTabItemAnimation(int position) {
        if (!checkIndex(position)) {
            return;
        }
        if (getTabItem(position) instanceof TabAnimatable) {
            ((TabAnimatable) getTabItem(position)).start();
        }
    }

    public void stopTabItemAnimation(int position) {
        if (!checkIndex(position)) {
            return;
        }
        if (getTabItem(position) instanceof TabAnimatable) {
            ((TabAnimatable) getTabItem(position)).stop();
        }
    }

    public void clearTabItemAnimation(int position) {
        if (!checkIndex(position)) {
            return;
        }
        if (getTabItem(position) instanceof TabAnimatable) {
            ((TabAnimatable) getTabItem(position)).clearTabAnimation();
        }
    }

    public int getTabCount() {
        return tabsContainer.getChildCount();
    }

    /**
     * @return true if the position is illegal
     */
    private boolean checkIndex(int position) {
        return position >= 0 && position < tabCount;
    }

    private void updateTabStyles() {
        Log.d(TAG, "updateTabStyles");

        for (int i = 0; i < tabCount; i++) {

            View tab = tabsContainer.getChildAt(i);

            tab.setBackgroundResource(tabBackgroundResId);
            tab.setPadding(tabPadding, 0, tabPadding, 0);

            if (tab instanceof TabItem) {

                AlphaGradientTextView tabTextView = ((TabItem) tab).getTabTextView();
                tabTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, tabTextSize);
                tabTextView.setTypeface(tabTypeface, tabTypefaceStyle);
                tabTextView.setTextColor(tabTextColor);

                if (i == selectedPosition) {
                    tabTextView.setTextColor(selectedTabTextColor);
                    if (isInTouchMode()) {
                        Log.d(TAG, " " + tab.requestFocus());
                    }
                }
            }
        }

    }

    /**
     * parse the position(witch is currently scrolling to) to direction{@link #FOCUS_RIGHT} or {@link #FOCUS_LEFT}
     *
     * @param position the position we scrolling towards
     * @return direction relative to current focused item
     */
    private int parsePositionToDirection(int position) {
        int newScrollX = tabsContainer.getChildAt(position).getLeft();
        Log.d(TAG, "newScrollX : " + newScrollX + " lastScrollX : " + lastScrollX);
        int direction = -1;
        if (newScrollX > lastScrollX) {
            direction = View.FOCUS_RIGHT;
        } else if (newScrollX < lastScrollX) {
            direction = View.FOCUS_LEFT;
        } else {
            Log.d(TAG, "no need to scroll");
        }

        if (newScrollX != lastScrollX) {
            lastScrollX = newScrollX;
        }
        return direction;
    }

    /**
     * control the scroll.
     * the logic is copied from {@link HorizontalScrollView#arrowScroll(int)}
     *
     * @param position
     */
    private void scrollToChild(int position) {
        //按照父类 arrowScroll的方法，滚动ScrollView
        if (tabCount == 0) {
            return;
        }

        View nextFocused = tabsContainer.getChildAt(position);
        final Rect mTempRect = new Rect();
        final int maxJump = getMaxScrollAmount();
        int direction = parsePositionToDirection(position);

        if (nextFocused != null && isWithinDeltaOfScreen(nextFocused, maxJump)) {
            nextFocused.getDrawingRect(mTempRect);
            offsetDescendantRectToMyCoords(nextFocused, mTempRect);
            int scrollDelta = computeScrollDeltaToGetChildRectOnScreen(mTempRect);
            scrollBy(scrollDelta, 0);
        } else {
            // no new focus
            int scrollDelta = maxJump;

            if (direction == View.FOCUS_LEFT && getScrollX() < scrollDelta) {
                scrollDelta = getScrollX();
            } else if (direction == View.FOCUS_RIGHT && getChildCount() > 0) {

                int daRight = getChildAt(0).getRight();

                int screenRight = getScrollX() + getWidth();

                if (daRight - screenRight < maxJump) {
                    scrollDelta = daRight - screenRight;
                }
            }
            if (scrollDelta == 0) {
                return;
            }
            scrollBy(direction == View.FOCUS_RIGHT ? scrollDelta : -scrollDelta, 0);
        }

    }

    /**
     * @return whether the descendant of this scroll view is within delta
     * pixels of being on the screen.
     */
    private boolean isWithinDeltaOfScreen(View descendant, int delta) {
        Rect mTempRect = new Rect();
        descendant.getDrawingRect(mTempRect);
        offsetDescendantRectToMyCoords(descendant, mTempRect);

        return (mTempRect.right + delta) >= getScrollX()
                && (mTempRect.left - delta) <= (getScrollX() + getWidth());
    }

    public void setImportanceListener(OnShowImportanceListener listener) {
        this.mImportanceListener = listener;
    }


    private class PageListener implements OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {
            if (delegatePageListener != null) {
                delegatePageListener.onPageScrolled(position, positionOffset,
                        positionOffsetPixels);
            }
            if (delegatePageListeners != null) {
                for (int i = 0; i < delegatePageListeners.size(); i++) {
                    OnPageChangeListener listener = delegatePageListeners.get(i);
                    if (listener != null) {
                        listener.onPageScrolled(position, positionOffset,
                                positionOffsetPixels);
                    }
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (delegatePageListener != null) {
                delegatePageListener.onPageScrollStateChanged(state);
            }

            if (delegatePageListeners != null) {
                for (int i = 0; i < delegatePageListeners.size(); i++) {
                    OnPageChangeListener listener = delegatePageListeners.get(i);
                    if (listener != null) {
                        listener.onPageScrollStateChanged(state);
                    }
                }
            }
        }

        @Override
        public void onPageSelected(int position) {
            selectedPosition = position;
            updateTabStyles();
            scrollToChild(pager.getCurrentItem());
            invokeGradient();
            //TODO update TabStrip's State

            if (delegatePageListener != null) {
                delegatePageListener.onPageSelected(position);
            }
            if (delegatePageListeners != null) {
                for (int i = 0; i < delegatePageListeners.size(); i++) {
                    OnPageChangeListener listener = delegatePageListeners.get(i);
                    if (listener != null) {
                        listener.onPageSelected(position);
                    }
                }
            }
        }

    }

    /**
     * show a special effect at a specific position
     *
     * @param position
     */
    public void showImportance(int position) {
        mState.currentState = LAYOUT_STATE_IMPORTANCE;
        mState.mCurrentImprotancePosition = position;
        //TODO should update background

        if (position >= 0 && position < tabCount) {
            int leftPoint = position - 1;
            int rightPoint = position + 1;
            int numTabs = tabCount;

            for (int i = 0; i < numTabs; i++) {
                View target = tabsContainer.getChildAt(i);
                target.setAlpha(1.0f);//for those already out of boundary
                if (leftPoint == i || rightPoint == i) {
                    if (target instanceof TabItem) {
                        ((TabItem) target).getTabTextView().setGraditentDirection(leftPoint == i
                                ? AlphaGradientTextView.RIGHT2LEFT
                                : AlphaGradientTextView.LEFT2RIGHT);
                        ((TabItem) target).getTabTextView().showGradient();
                    } else {
                        target.setAlpha(0.4f);
                    }
                } else if (i != position) {
                    target.setAlpha(0);
                }
            }
            //TODO background for importance position
            if (mImportanceListener != null) {
                mImportanceListener.onImportanceChanged(true);
            }
        }
    }

    public void hideImportance() {
        //TODO
        if (LAYOUT_STATE_NORMAL == mState.currentState) {
            return;
        }
        mState.currentState = LAYOUT_STATE_NORMAL;
        mState.mCurrentImprotancePosition = -1;
        int numTabs = tabCount;
        for (int i = 0; i < numTabs; i++) {
            View target = tabsContainer.getChildAt(i);
            target.setAlpha(1.0f);
            if (target instanceof TabItem) {
                ((TabItem) target).getTabTextView().hideGradient();
            }
        }
        if (mImportanceListener != null) {
            mImportanceListener.onImportanceChanged(false);
        }
        invokeGradient();
    }

    public void setTextSize(int textSizePx) {
        this.tabTextSize = textSizePx;
        updateTabStyles();
    }

    public int getTextSize() {
        return tabTextSize;
    }

    public void setTextColor(int textColor) {
        this.tabTextColor = textColor;
        updateTabStyles();
    }

    public void setTextColorResource(int resId) {
        this.tabTextColor = getResources().getColor(resId);
        updateTabStyles();
    }

    public int getTextColor() {
        return tabTextColor;
    }

    public void setSelectedTextColor(int textColor) {
        this.selectedTabTextColor = textColor;
        updateTabStyles();
    }

    public void setSelectedTextColorResource(int resId) {
        this.selectedTabTextColor = getResources().getColor(resId);
        updateTabStyles();
    }

    public int getSelectedTextColor() {
        return selectedTabTextColor;
    }

    public void setTypeface(Typeface typeface, int style) {
        this.tabTypeface = typeface;
        this.tabTypefaceStyle = style;
        updateTabStyles();
    }

    public void setTabBackground(int resId) {
        this.tabBackgroundResId = resId;
        updateTabStyles();
    }

    public int getTabBackground() {
        return tabBackgroundResId;
    }

    public void setTabPaddingLeftRight(int paddingPx) {
        this.tabPadding = paddingPx;
        updateTabStyles();
    }

    public int getTabPaddingLeftRight() {
        return tabPadding;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        selectedPosition = savedState.selectedPosition;
        requestLayout();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.selectedPosition = selectedPosition;
        return savedState;
    }

    static class SavedState extends BaseSavedState {
        int selectedPosition;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            selectedPosition = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(selectedPosition);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    /**
     * control the view's alpha when scroll out of the window
     */

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        //TODO in some condition we need to maintain is state ...Like we are focusing in Live tab;

        switch (mState.currentState) {
            case LAYOUT_STATE_IMPORTANCE:
                break;
            default:
                // FUCK the API has handled this ...keep this to
//                //normal Mode
//                for (int i = 0; i < tabCount; i++) {
//                    AlphaGradientTextView v = (AlphaGradientTextView) tabsContainer.getChildAt(i);
//                    int left = v.getLeft();
//                    int right = v.getRight();
//                    if (left < l) {
//                        //items on the left
//                        v.setGraditentDirection(AlphaGradientTextView.RIGHT2LEFT);
//                        v.showGradient(50, l - left - v.getPaddingLeft());
//                    } else if (right > l + getWidth()) {
//                        //items on the right
//                        v.setGraditentDirection(AlphaGradientTextView.LEFT2RIGHT);
//                        v.showGradient(50, l + getWidth() - left - v.getPaddingRight());
////                        v.setAlpha(alpha);
//                    } else {
//                        //items can be seen
//                        v.hideGradient();
//                        v.setAlpha(1.0f);
//                    }
//                }
                break;
        }
    }

    /**
     * trigger tab item's Gradient
     * <p/>
     * trigger the onScrollChanged Method to make sure text color changed<br/>
     * cause setTextColor can't change the shader color fo text
     */
    private void invokeGradient() {
        onScrollChanged(getScrollX(), getScrollY(), getScrollX(), getScrollY());
    }

    @Override
    public boolean hasOverlappingRendering() {
        //TODO ....
        return false;
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        if (mHasOverlappingRendering) {
            // draw the selected tab last
            if (selectedPosition == -1) {
                return i;
            } else {
                if (i == childCount - 1) {
                    return selectedPosition;
                } else if (i >= selectedPosition) {
                    return i + 1;
                } else {
                    return i;
                }
            }
        } else {
            return super.getChildDrawingOrder(childCount, i);
        }
    }

    public interface IconTabProvider {
        public int getPageIconResId(int position);
    }

    /**
     * store current TabStrip State..
     * <p/>
     * TODO
     */
    public static class State {
        private int currentState = LAYOUT_STATE_NORMAL;
        private int mCurrentImprotancePosition = -1;
    }

    interface OnShowImportanceListener {
        void onImportanceChanged(boolean hasShow);
    }

    private class TabStripObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            notifyDataSetChanged();
        }

        @Override
        public void onInvalidated() {
            notifyDataSetChanged();
        }
    }

    public void setmOnTabItemFocusChangeListener(OnTabItemFocusChangeListener listener) {
        mOnTabItemFocusChangeListener = listener;
    }

    /**
     * when tabItem get focused this method will be called
     * <p>
     * if user want to do something when TabItem get focused. <b color="red">DO NOT<b/> set {@link android.view.View.OnFocusChangeListener} on TabItem
     * this will cause Chaos in this component;
     * <br>
     * startT {@link #setmOnTabItemFocusChangeListener(OnTabItemFocusChangeListener)} instead.
     */
    public interface OnTabItemFocusChangeListener {
        public void onFocusChange(View v, boolean hasFocus, int position);
    }
}
