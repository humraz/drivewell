/* Copyright 2015 The TensorFlow Authors. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
==============================================================================*/

package org.tensorflow.demo;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;
import org.tensorflow.demo.Classifier.Recognition;

import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class RecognitionScoreView extends View implements ResultsView {
  private static final float TEXT_SIZE_DIP = 24;
  private List<Recognition> results;
 public static double score =100;
  private final float textSizePx;
  private final Paint fgPaint;
    public static  String v="";
  private final Paint bgPaint;
ClassifierActivity b;
  public RecognitionScoreView(final Context context, final AttributeSet set) {
    super(context, set);

    textSizePx =
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, TEXT_SIZE_DIP, getResources().getDisplayMetrics());
    fgPaint = new Paint();
    fgPaint.setTextSize(textSizePx);

    bgPaint = new Paint();
    bgPaint.setColor(0xcc4285f4);
  }

  @Override
  public void setResults(final List<Recognition> results) {
    this.results = results;
    postInvalidate();
  }
  public void checkroute(String text,float confi) {


    final String URL = "http://192.168.43.30:8000/snippets/";
// Post params to be sent to the server


    HashMap<String, String> params = new HashMap<String, String>();
    params.put("title", text);
    params.put("code", Float.toString(confi));

    JsonObjectRequest request_json = new JsonObjectRequest(URL, new JSONObject(params),
            new Response.Listener<JSONObject>() {
              @Override
              public void onResponse(JSONObject response) {

                try {
                  //Process os success response
                 // Toast.makeText(makenotication.this, "sent data",Toast.LENGTH_LONG).show();


                } catch (Exception e) {

                }
              }
            }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        VolleyLog.e("Error: ", error.getMessage());
      }
    });

// add the request object to the queue to be executed
    AppController.getInstance().addToRequestQueue(request_json);
  }

  @Override
  public void onDraw(final Canvas canvas) {
    final int x = 10;

    int y = (int) (fgPaint.getTextSize() * 1.5f);

    canvas.drawPaint(bgPaint);

    if (results != null) {
      for (final Recognition recog : results) {
        float a=0;
        if (recog.getConfidence()>0.95) {

          if (ClassifierActivity.flag==1)
          {
            switch(recog.getTitle())
            {
              case "normal": break;
              case  "drinking" : score=score-1;
                            if (!v.contains("drinking"))
                                v=v+"drinking";
                      break;
              case "phoneright" : score=score-0.5;
                  if (!v.contains("phone"))
                      v=v+"phone";
                  break;
              default:
            }
          }

         canvas.drawText("\n"+recog.getTitle() + ": " + recog.getConfidence(), x, y, fgPaint);
         // checkroute(String.valueOf(ClassifierActivity.flag), recog.getConfidence());
          if (recog.getTitle().equals("normal"))
          {

          }
          else {
      //      checkroute(String.valueOf(ClassifierActivity.flag), recog.getConfidence());

          }
        }
        y += fgPaint.getTextSize() * 1.5f;
      }
    }
  }
}
