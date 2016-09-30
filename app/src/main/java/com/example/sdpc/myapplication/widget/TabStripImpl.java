package com.example.sdpc.myapplication.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.example.sdpc.myapplication.R;
import com.example.sdpc.myapplication.widget.interfaces.ITabStrip;
import com.example.sdpc.myapplication.widget.interfaces.TabPagerBindStrategy;

import java.util.ArrayList;

public class TabStripImpl extends HorizontalScrollView implements ITabStrip{

    private static final String TAG = TabStripImpl.class.getSimpleName();
    private static final int LAYOUT_STATE_NORMAL = 0;
    private static final int LAYOUT_STATE_IMPORTANCE = 1;
    private static final int DEFAULT_FADING_EDGE_LENGTH = 100;


    private static final boolean mHasOverlappingRendering = false;
    // @formatter:off
    private static final int[] ATTRS = new int[]{android.R.attr.textSize,
            android.R.attr.textColor};

    // @formatter:on
    /**
     * LayoutParams for tabItems
     */
    private LinearLayout.LayoutParams defaultTabLayoutParams;


    /**
     * developer should listen TabItem's focus event from this listener
     * should <b color=red> NOT <b/> use original OnFocusChangeListener
     */
    private OnTabItemFocusChangeListener mOnTabItemFocusChangeListener;
    private LinearLayout tabsContainer;

    private TabPagerBindStrategy bindStrategy;

    private ArrayList<String> tabTitles;
    private int tabCount;

    /**
     * current selected position
     */
    private int selectedPosition = 0;
    private int tabPadding = 20;
    private int tabTextSize = 12;
    private int tabTextColor = 0x8fffffff;
    private int selectedTabTextColor = 0x0fffffff;
    private Typeface tabTypeface = null;
    private int tabTypefaceStyle = Typeface.NORMAL;

    /**
     * 上次滚动位置，用于在{@link #scrollToChild(int)}中判断滚动方向
     */
    private int lastScrollX = 0;

    private int tabBackgroundResId = android.R.color.transparent;

    private final State mState = new State();

    public TabStripImpl(Context context) {
        this(context, null);
    }

    public TabStripImpl(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabStripImpl(Context context, AttributeSet attrs,
                        int defStyle) {
        super(context, attrs, defStyle);
        setClipChildren(false);
        setClipToPadding(false);
        setFillViewport(true);
        setWillNotDraw(false);// ensure onDraw() will be called
        setChildrenDrawingOrderEnabled(mHasOverlappingRendering);
        setSmoothScrollingEnabled(false);
        //set left and right edge to indicate that this line have more items
        setHorizontalFadingEdgeEnabled(true);
        setFadingEdgeLength(DEFAULT_FADING_EDGE_LENGTH);

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

        tabTitles = new ArrayList<String>();

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
                R.styleable.TabStripImpl);

        tabPadding = a.getDimensionPixelSize(
                R.styleable.TabStripImpl_pstsTabPaddingLeftRight,
                tabPadding);
        tabBackgroundResId = a.getResourceId(
                R.styleable.TabStripImpl_pstsTabBackground,
                tabBackgroundResId);
        a.recycle();

        defaultTabLayoutParams = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);

    }


    private OnShowImportanceListener mImportanceListener;

    /**
     * set bind strategy to this TabStrip.
     * then refresh the hole UI
     * @param strategy
     */
    @Override
    public void setBindStrategy(TabPagerBindStrategy strategy) {
        if (checkNotNull(strategy)) {
            return;
        }
        if (strategy.getCount() <= 0) {
            //nothing to show on this tab
            return;
        }
        tabTitles.clear();
        this.bindStrategy = strategy;
        updateTabTitles();
        notifyStrategyChanged();
    }

    /**
     * sync tabTitles with bindStrategy
     */
    private void updateTabTitles(){
        for (int i = 0; i < bindStrategy.getCount(); i++) {
            tabTitles.add(i,bindStrategy.getPageTitle(i));
        }
    }

    private boolean checkNotNull(Object o) {
        return o == null;
    }

    /**
     * notify that the pager's data set has been changed
     * TabStrip should update its UI;
     * //TODO
     */
    @Override
    public void notifyStrategyChanged() {

        if(checkNotNull(bindStrategy)){
            throw new IllegalStateException(
                    "bindStrategy has not been set.");
        }
        updateTabTitles();
        tabsContainer.removeAllViews();
        tabCount = bindStrategy.getCount();

        for (int i = 0; i < tabCount; i++) {
            addTextTab(i, tabTitles.get(i));
        }
        //if current the tabs is less than tabCount set the last to be focused
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

    @Override
    public void setTabText(String newText,int position){
        if(!checkIndex(position)){
            //index out of bound
            return ;
        }
        tabTitles.set(position,newText);
        getTabItem(position).setText(newText);
        scrollToChild(selectedPosition);
    }

    /**
     * add a {@link TabItem} with {@link AlphaGradientTextView} in it to this TabStrip
     *
     * @param position
     * @param title
     */
    private void addTextTab(final int position, String title) {
        TabItem tabItem = new TabItem(getContext());
        AlphaGradientTextView tabText = tabItem.getTabTextView();
        tabText.setText(title);
        tabText.setGravity(Gravity.CENTER);
        tabText.setTextSize(TypedValue.COMPLEX_UNIT_PX, tabTextSize);
        addTab(position, tabItem);
    }

    /**
     * add tab to this TabStrip and bind it with ViewPager
     *
     * @param position
     * @param tab      item to be add to this component :must be a child of {@link TabItem}
     */
    @Override
    public <T extends TabItem> void addTab(final int position, T tab) {
        tab.setFocusable(true);
        tab.setFocusableInTouchMode(true);
        tab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                bindStrategy.setPagerCurrentItem(position);
            }
        });
        tab.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    bindStrategy.setPagerCurrentItem(position);
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
     * @return TabItem at the position or null if not found;
     */
    @Override
    public <T extends TabItem> T getTabItem(int position) {
        if (!checkIndex(position)) {
            return null;
        }
        //noinspection unchecked
        return (T) tabsContainer.getChildAt(position);
    }

    public void startInTabItemAnimation(int position) {
        if (!checkIndex(position)) {
            return;
        }
        if (getTabItem(position) != null) {
            getTabItem(position).in();
        }
    }

    public void startOutTabItemAnimation(int position) {
        if (!checkIndex(position)) {
            return;
        }
        if (getTabItem(position) != null) {
            getTabItem(position).out();
        }
    }

    /**
     * cancel all TabAnimations that this TabItem is playing
     *
     * @param position TabItem's position
     */
    public void clearTabItemAnimation(int position) {
        if (!checkIndex(position)) {
            return;
        }
        getTabItem(position).clearTabAnimation();
    }

    /**
     * @return number of currently showing items
     */
    @Override
    public int getTabCount() {
        return tabsContainer == null ? 0 : tabsContainer.getChildCount();
    }

    /**
     * @return true if the position is legal
     */
    private boolean checkIndex(int position) {
        return position >= 0 && position < tabCount;
    }

    /**
     * traverse and update all TabItem's style based on current selected position
     */
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

                //current viewpager's selected position. should be marked with selectedTabTextColor NOT focus color
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
     * <br/>
     * the logic is copied from {@link HorizontalScrollView#arrowScroll(int)}
     */
    @Override
    public void scrollToChild(int position) {
        //按照父类 arrowScroll的方法，滚动ScrollView
        if (tabCount == 0) {
            return;
        }
        //Always trigger scroll. because the selectionPosition may be out of this view.
        selectedPosition = position;
        View nextFocused = tabsContainer.getChildAt(position);
        final Rect mTempRect = new Rect();
//        final int maxJump = getMaxScrollAmount();
        //in this mode the maxJump is the hole scrollView' length
        //so that the pager's setCurrentItem(int item) can work well;
        final int maxJump = getScrollX();
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
            //noinspection ResourceType
            scrollBy(direction == View.FOCUS_RIGHT ? scrollDelta : -scrollDelta, 0);
        }
        updateTabStyles();
        invokeGradient();
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


    /**
     * show a special effect at a specific position
     */
    public void showImportance(int position) {
        mState.currentState = LAYOUT_STATE_IMPORTANCE;
        mState.mCurrentImprotancePosition = position;

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

    /**
     * @param fadingEdgeLength the length to show fading at both sides
     */
    public void setFadingEdgeLength(int fadingEdgeLength) {
        super.setFadingEdgeLength(fadingEdgeLength);
        invalidate();
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

    /**
     * mainly save the current selected position when this activity has been brought to backward
     */
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
        // in some condition we need to maintain is state ...Like we are focusing in Live tab;
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

    public void setOnTabItemFocusChangeListener(OnTabItemFocusChangeListener listener) {
        mOnTabItemFocusChangeListener = listener;
    }

    /**
     * when tabItem get focused this method will be called
     * <p>
     * if user want to do something when TabItem get focused. <b color="red">DO NOT<b/> set {@link android.view.View.OnFocusChangeListener} on TabItem
     * this will cause Chaos in this component;
     * <br>
     * set {@link #setOnTabItemFocusChangeListener(OnTabItemFocusChangeListener)} instead.
     */
    public interface OnTabItemFocusChangeListener {
        public void onFocusChange(View v, boolean hasFocus, int position);
    }
}
