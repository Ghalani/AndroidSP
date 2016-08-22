package com.ghalani.ghalani.activity;

import android.os.Bundle;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.ghalani.ghalani.R;
import com.ghalani.ghalani.db.FarmerDBHelper;
import com.ghalani.ghalani.helper.TextLogHelper;
import com.ghalani.ghalani.qreader.QRDataListener;
import com.ghalani.ghalani.qreader.QREader;
import com.google.android.gms.vision.text.Line;

import org.json.JSONException;
import org.json.JSONObject;

public class FarmerQRActivity extends AppCompatActivity {

    private SurfaceView surfaceView;
    private TextView textView_qrcode_info;
    QREader qrEader;
    FarmerDBHelper fdb;
    JSONObject fmr = new JSONObject();
    String oldData = "";
    LinearLayout fmrCard;
    TextView name, phone;
    NetworkImageView fmrThumb;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_qr);
        fdb = new FarmerDBHelper(this);

        fmrCard = (LinearLayout) findViewById(R.id.fmr_card);
        name = (TextView) findViewById(R.id.fmr_name);
        phone = (TextView) findViewById(R.id.fmr_phone);
        fmrThumb = (NetworkImageView) findViewById(R.id.fmr_thumb);

        surfaceView = (SurfaceView) findViewById(R.id.camera_view);
        textView_qrcode_info = (TextView) findViewById(R.id.code_info);

        qrEader = new QREader.Builder(this, surfaceView, new QRDataListener() {
            @Override public void onDetected(final String data) {
                Log.d("QREader", "Value : " + data);
                textView_qrcode_info.post(new Runnable() {
                    @Override public void run() {
                        if (!data.equals(oldData)){
                            oldData = data;
                            textView_qrcode_info.setText(data);
                            fmr = fdb.getFarmer(data);
                            if (fmr != null) {
                                try {
                                    TextLogHelper.toast(FarmerQRActivity.this, "Success: "+ fmr.getString("fname"), false);
                                    name.setText(fmr.getString("fname") +" "+fmr.getString("lname"));
                                    phone.setText(fmr.getString("phone"));
                                    fmrThumb.setImageUrl(fmr.getString("image_url"), );
                                    fmrCard.setVisibility(View.VISIBLE);
                                } catch (JSONException e) {e.printStackTrace();   }
                            }else{
                                TextLogHelper.toast(FarmerQRActivity.this, "Unknown QRcode", false);
                                //TextLogHelper.log( "Unknown QRcode");
                            }
                        }

                    }
                });
            }
        }).facing(QREader.BACK_CAM)
                .enableAutofocus(true)
                .height(800)
                .width(800)
                .build();
    /*}).facing(QREader.BACK_CAM)
        .enableAutofocus(true)
        .height(800)
        .width(800)
        .build();*/

        qrEader.init();
    }

    @Override protected void onStart() {
        super.onStart();

        // Call in onStart
        qrEader.start();
    }

    @Override protected void onDestroy() {
        super.onDestroy();

        // Call in onDestroy
        qrEader.stop();
        qrEader.releaseAndCleanup();
    }
}
