package com.example.kartikeya_pc.forum;

import android.content.Context;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.Types.BoomType;
import com.nightonke.boommenu.Types.ButtonType;
import com.nightonke.boommenu.Types.ClickEffectType;
import com.nightonke.boommenu.Types.DimType;
import com.nightonke.boommenu.Types.PlaceType;
import com.nightonke.boommenu.Util;
import java.util.Random;


public class MainActivity extends AppCompatActivity implements
        BoomMenuButton.OnSubButtonClickListener, BoomMenuButton.AnimatorListener{

    private static String TAG = MainActivity.class.getSimpleName();
    private BoomMenuButton boomMenuButton;
    private BoomMenuButton boomMenuButtonInActionBar;
    private BoomMenuButton boomInfo;
    private View mCustomView;
    private Context mContext;
    private ProgressBar animationListener;
    private boolean isInit = false;
    private SessionManager session;

    // int cat_id,user_id,question_id;
   // int userid2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new SessionManager(this);
        if(!session.isLoggedIn()){
            Intent i = new Intent(MainActivity.this,Login.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        }
       else {
            setContentView(R.layout.activity_main);

            mContext = this;

            ActionBar mActionBar = getSupportActionBar();
            if (mActionBar != null) {
                mActionBar.setDisplayShowHomeEnabled(false);
                mActionBar.setDisplayShowTitleEnabled(false);
            }

            LayoutInflater mInflater = LayoutInflater.from(this);

            mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
            TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
            mTitleTextView.setText(R.string.app_name);

            boomMenuButtonInActionBar = (BoomMenuButton) mCustomView.findViewById(R.id.boom);


            if (mActionBar != null) {
                mActionBar.setCustomView(mCustomView);
                mActionBar.setDisplayShowCustomEnabled(true);
            }
            ((Toolbar) mCustomView.getParent()).setContentInsetsAbsolute(0, 0);
            boomMenuButton = (BoomMenuButton) findViewById(R.id.boom);
            boomInfo = (BoomMenuButton) mCustomView.findViewById(R.id.info);
            animationListener = (ProgressBar) findViewById(R.id.animation_listener);
       }

    }



    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (!isInit) {
            try {
                initBoom();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        isInit = true;
    }



    private void initBoom() {

        Drawable[] drawables = new Drawable[3];
        int[] drawablesResource = new int[]{
                R.drawable.mark,
                R.drawable.refresh,
                R.drawable.copy,


        };

        for (int i = 0; i < 3; i++)
            drawables[i] = ContextCompat.getDrawable(mContext, drawablesResource[i]);

        String[] STRINGS = new String[]{
                "Topic",
                "Question",
                "Account",

        };


        String[] strings = new String[3];

        for (int i = 0; i < 3; i++)
            strings[i] = STRINGS[i];
        int[][] colors = new int[3][2];
        for (int i = 0; i < 3; i++) {
            colors[i][1] = GetRandomColor();
            colors[i][0] = Util.getInstance().getPressedColor(colors[i][1]);
        }
        int[] location = new int[3];
        for(int i=0;i<3;i++){
            location[i] = Util.getInstance().getScreenHeight(mContext);
        }

        Log.e(TAG,"end");
       // Now with Builder, you can init BMB more convenient
        new BoomMenuButton.Builder()
                .subButtons(drawables, colors, strings)
                .button(ButtonType.HAM)
                .boom(BoomType.PARABOLA)
                .place(PlaceType.HAM_3_1)
                .boomButtonShadow(Util.getInstance().dp2px(2), Util.getInstance().dp2px(2))
                .subButtonsShadow(Util.getInstance().dp2px(2), Util.getInstance().dp2px(2))
                .animator(this)
                .cancelable(true)
                .autoDismiss(true)
                .dim(DimType.DIM_9)
                .clickEffect(ClickEffectType.NORMAL)
                .onSubButtonClick(this)
                .init(boomMenuButton);


        new BoomMenuButton.Builder()
                .subButtons(drawables, colors, strings)
                .button(ButtonType.HAM)
                .boom(BoomType.PARABOLA)
                .place(PlaceType.HAM_3_1)
                .subButtonsShadow(Util.getInstance().dp2px(2), Util.getInstance().dp2px(2))
                .onSubButtonClick(this)
                .animator(this)
                .cancelable(true)
                .dim(DimType.DIM_9)
                .autoDismiss(true)
                .clickEffect(ClickEffectType.NORMAL)
                .init(boomMenuButtonInActionBar);

    }
    private String[] Colors = {
            "#F44336",
            "#E91E63",
            "#9C27B0",
            "#2196F3",
            "#03A9F4",
            "#00BCD4",
            "#009688",
            "#4CAF50",
            "#8BC34A",
            "#CDDC39",
            "#FFEB3B",
            "#FFC107",
            "#FF9800",
            "#FF5722",
            "#795548",
            "#9E9E9E",
            "#607D8B"};

    public int GetRandomColor() {
        Random random = new Random();
        int p = random.nextInt(Colors.length);
        return Color.parseColor(Colors[p]);
    }

    @Override
    public void onClick(int buttonIndex) {

        switch (buttonIndex) {
            case 0:
                Intent i1 = new Intent(MainActivity.this, categories.class);
                startActivity(i1);
                finish();
                break;
            case 1:
                Intent i2 = new Intent(MainActivity.this, Questions.class);
                startActivity(i2);
                finish();
                break;

            case 2:
                Intent i3 = new Intent(MainActivity.this, Account.class);
                startActivity(i3);
                finish();
                break;
            default:
                break;
        }

    }

    @Override
    public void toShow() {
        animationListener.setProgress(0);
    }

    @Override
    public void showing(float fraction) {
        animationListener.setProgress((int) (fraction * 100));
    }

    @Override
    public void showed() {
        //animationListener.setProgress(100);


    }

    @Override
    public void toHide() {
        animationListener.setProgress(100);
    }

    @Override
    public void hiding(float fraction) {
        animationListener.setProgress((int) ((1 - fraction) * 100));
    }

    @Override
    public void hided() {
        animationListener.setProgress(0);
    }

    @Override
    public void onBackPressed() {
        if (boomMenuButton.isClosed()
                && boomMenuButtonInActionBar.isClosed()
                && boomInfo.isClosed()) {
            super.onBackPressed();
        } else {
            boomMenuButton.dismiss();
            boomMenuButtonInActionBar.dismiss();
            boomInfo.dismiss();
        }
    }



   /* @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.easy_example:
                startActivity(new Intent(this, categories.class));
                break;
            case R.id.list_view_example:
                startActivity(new Intent(this, Questions.class));
                break;
            case R.id.share_example:
                startActivity(new Intent(this, Login.class));
                break;
            case R.id.builder_example:
                startActivity(new Intent(this, Account.class));
                break;
        }
    }*/

}









   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent i = new Intent(MainActivity.this, Login.class);
            startActivity(i);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/




