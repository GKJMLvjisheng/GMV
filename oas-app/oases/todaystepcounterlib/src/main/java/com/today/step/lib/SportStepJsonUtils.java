package com.today.step.lib;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * @author :  jiahongfei
 * @email : jiahongfeinew@163.com
 * @date : 2018/1/31
 * @desc : 用于解析和生成运动步数Json字符串
 */

public class SportStepJsonUtils {

   // public static final String SPORT_DATE = "date";
    public static final String STEP_NUM = "stepNum";
    //public static final String DISTANCE = "km";
   // public static final String CALORIE = "kaluli";
    public static final String TODAY = "date";

    static JSONArray getSportStepJsonArray(List<TodayStepData> todayStepDataArrayList) {
        JSONArray jsonArray = new JSONArray();
        if (null == todayStepDataArrayList || 0 == todayStepDataArrayList.size()) {
            return jsonArray;
        }

        for(int i = todayStepDataArrayList.size() - 1; i >= 0; ){
            TodayStepData today = todayStepDataArrayList.get(i);
            while (--i >=0 ){
                TodayStepData prevDay = todayStepDataArrayList.get(i);
                Log.d("zbb", "getSportStepJsonArray: check " + prevDay.toString() + today.toString());
               // Log.d("zbb", "prev date " + prevDay.getToday() + ", today " + today.getToday());
                if (prevDay.getToday().equals(today.getToday())){
                    Log.d("zbb", "getSportStepJsonArray: remove i");
                    todayStepDataArrayList.remove(prevDay);
                }
                else{
                    break;
                }
            }
        }

        for (int i = 0; i < todayStepDataArrayList.size(); i++) {
            TodayStepData todayStepData = todayStepDataArrayList.get(i);

            try {
                JSONObject subObject = new JSONObject();
                subObject.put(TODAY, todayStepData.getToday());
                subObject.put(STEP_NUM, todayStepData.getStep());
                jsonArray.put(subObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonArray;
    }

    // 公里计算公式
    static String getDistanceByStep(long steps) {
        return String.format("%.2f", steps * 0.6f / 1000);
    }

    // 千卡路里计算公式
    static String getCalorieByStep(long steps) {
        return String.format("%.1f", steps * 0.6f * 60 * 1.036f / 1000);
    }


}
