package com.example.sdpc.myapplication.manager;

import android.database.DataSetObserver;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.example.sdpc.myapplication.widget.interfaces.ITabStrip;
import com.example.sdpc.myapplication.widget.interfaces.TabPagerBindStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shaodong on 16-9-29.
 */

public class DefaultStrategy implements TabPagerBindStrategy {
    private ITabStrip tabStrip;
    private ViewPager pager;
    private PagerAdapter mAdapter;
    private TabStripObserver mObserver;
    /**
     * internal listener :used for bind the pager with this tagStrop
     */
    private final PageListener internalPageListener = new PageListener();
    /**
     * outer pagerListener ,developer should listen view pager's event from this listener
     */
    private ViewPager.OnPageChangeListener delegatePageListener;
    private List<ViewPager.OnPageChangeListener> delegatePageListeners;

    public DefaultStrategy(ITabStrip tabStrip, ViewPager viewPager) {
        this.tabStrip = tabStrip;
        setViewPager(viewPager);
    }

    @Override
    public int getCount() {
        return mAdapter.getCount();
    }

    /**
     * set the viewpager witch is bind to this TabStrip
     * should be called after the pager's {@link ViewPager#setAdapter(PagerAdapter)}
     */
    public void setViewPager(ViewPager pager) {
        if (pager == null || pager.getAdapter() == null) {
            throw new IllegalStateException(
                    "ViewPager does not have adapter instance.");
        }
        this.pager = pager;
        //noinspection deprecation
        pager.setOnPageChangeListener(internalPageListener);

        if (mAdapter != null) {
            mAdapter.unregisterDataSetObserver(mObserver);
        }
        mAdapter = pager.getAdapter();

        //add data Observer to viewPager's adapter.when adapter calls notifyStrategyChanged();this TabStrip will change as well
        if (mAdapter != null) {
            if (mObserver == null) {
                mObserver = new TabStripObserver();
            }
            mAdapter.registerDataSetObserver(mObserver);
        }
    }

    @Override
    public void setPagerCurrentItem(int position) {
        pager.setCurrentItem(position);
    }

    @Override
    public void setTabCurrentItem(int position) {
        tabStrip.scrollToChild(position);
    }

    @Override
    public String getPageTitle(int position) {
        return mAdapter.getPageTitle(position).toString();
    }

    /**
     * listener to bind View pager with TabItems
     */
    private class PageListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {
            if (delegatePageListener != null) {
                delegatePageListener.onPageScrolled(position, positionOffset,
                        positionOffsetPixels);
            }
            if (delegatePageListeners != null) {
                for (int i = 0; i < delegatePageListeners.size(); i++) {
                    ViewPager.OnPageChangeListener listener = delegatePageListeners.get(i);
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
                    ViewPager.OnPageChangeListener listener = delegatePageListeners.get(i);
                    if (listener != null) {
                        listener.onPageScrollStateChanged(state);
                    }
                }
            }
        }

        @Override
        public void onPageSelected(int position) {
            setTabCurrentItem(pager.getCurrentItem());

            if (delegatePageListener != null) {
                delegatePageListener.onPageSelected(position);
            }
            if (delegatePageListeners != null) {
                for (int i = 0; i < delegatePageListeners.size(); i++) {
                    ViewPager.OnPageChangeListener listener = delegatePageListeners.get(i);
                    if (listener != null) {
                        listener.onPageSelected(position);
                    }
                }
            }
        }

    }

    @Deprecated
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        this.delegatePageListener = listener;
    }

    public void addOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        if (delegatePageListeners == null) {
            delegatePageListeners = new ArrayList<ViewPager.OnPageChangeListener>();
        }
        delegatePageListeners.add(listener);
    }

    public void removeOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        if (delegatePageListeners != null) {
            delegatePageListeners.remove(listener);
        }
    }

    public void clearOnPageChangeListener() {
        if (delegatePageListeners != null) {
            delegatePageListeners.clear();
        }
    }

    /**
     * observe the adapter's data change event.
     * when viewpager's adapter called notifyStrategyChanged();
     * this Observer will be notified
     */
    private class TabStripObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            tabStrip.notifyStrategyChanged();
        }

        @Override
        public void onInvalidated() {
            tabStrip.notifyStrategyChanged();
        }
    }
}
