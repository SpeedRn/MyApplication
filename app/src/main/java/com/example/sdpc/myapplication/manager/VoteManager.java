package com.example.sdpc.myapplication.manager;

import android.content.Context;

import com.MApplication;

import java.io.IOException;
import java.util.Calendar;

/**
 * Created by sdpc on 16-9-6.
 */
public class VoteManager {
    private static VoteManager INSTANCE = new VoteManager(MApplication.INSTANCE);
    private static String TAG = VoteManager.class.getSimpleName();
    private static String VOTE_STATE = "vot_state";
    private static String SUFFIX_TIME = "time";
    private static String SUFFIX_VOTE = "voted";
//    private Gson mGson;
//    private Bean voteData;
    private Context mContext;

    private VoteManager(Context context) {
        mContext = context;
    }

    public static VoteManager getINSTANCE() {
        return INSTANCE;
    }

//    public void fetch(String url) {
//        HttpRequest request = new HttpRequest.Builder().url(url).build();
//        final Bean result = null;
//        HttpManager.enqueue(request, new HttpCallback() {
//            @Override
//            public void onHttpFailure(HttpRequest request, IOException e) {
//
//            }
//
//            @Override
//            public void onHttpResponse(HttpResponse response) throws IOException {
//                LetvLog.d(TAG, "getData onResponse=" + response);
//                if (response != null) {
//                    LetvLog.d(TAG, "onResponse body=" + response.body());
//                    Bean imgModel = null;
//                    try {
//                        imgModel = mGson.fromJson(response.body().string(), Bean.class);
//                    } catch (Exception e) {
//                        LetvLog.d(TAG, "e=" + e);
//                    }
//                    if (imgModel != null) {
//                        setVoteData(imgModel);
//                    }
//                }
//            }
//        });
//    }
//
//    public Bean getVoteData() {
//        return voteData;
//    }
//
//    public void setVoteData(Bean voteData) {
//        this.voteData = voteData;
//    }

    /**
     * store the vote state of specific desktop to xml
     * time key isVoted
     *
     * @param key
     */
    public void recordVote(String key, boolean isVoted) {
        mContext.getSharedPreferences(VOTE_STATE, Context.MODE_PRIVATE).edit()
                .putBoolean(key + SUFFIX_VOTE, isVoted)// store vote state
                .putLong(key + SUFFIX_TIME, System.currentTimeMillis())//store time
                .apply();
    }

    /**
     * judge whether user has voted the desktop by analyzing local SharedPreferences.
     * <p>
     * when enter vote page we should check every item to decide the vote button's state;
     *
     * @param key
     * @return false :user has not voted for this key today;
     */
    public boolean checkIsVotedLocal(String key) {
        boolean voteSateLocal = mContext.getSharedPreferences(VOTE_STATE, Context.MODE_PRIVATE).getBoolean(key + SUFFIX_VOTE, false);
        if (!voteSateLocal) {
            //local voteState is false we consider that user has not voted yet
            return false;
        }

        long dateLong = mContext.getSharedPreferences(VOTE_STATE, Context.MODE_PRIVATE).getLong(key + SUFFIX_TIME, 0);
        Calendar voteDate = Calendar.getInstance();
        voteDate.setTimeInMillis(dateLong);
        Calendar now = Calendar.getInstance();

        //if now is another day return false otherwise return true;
        if (voteDate.get(Calendar.DATE) != now.get(Calendar.DATE)
                || voteDate.get(Calendar.MONTH) != now.get(Calendar.MONTH)
                || voteDate.get(Calendar.YEAR) != now.get(Calendar.YEAR)) {
            recordVote(key, false);
            return false;
        }
        return true;
    }

}
