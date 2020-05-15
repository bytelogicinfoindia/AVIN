package com.avin.avin.allactivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;

import com.avin.avin.R;
import com.avin.avin.otherclass.BaseActivity;
import com.avin.avin.server.SessionManager;
import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.AppUpdaterUtils;
import com.github.javiersantos.appupdater.enums.AppUpdaterError;
import com.github.javiersantos.appupdater.enums.Display;
import com.github.javiersantos.appupdater.enums.UpdateFrom;
import com.github.javiersantos.appupdater.objects.Update;

public class SplashActivity extends BaseActivity {

    private final int SPLASH_DISPLAY_LENGTH = 4000;
    SharedPreferences settings;
    boolean firstRun;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().hide();

        AppUpdaterUtils appUpdaterUtils = new AppUpdaterUtils(this)
                .setUpdateFrom(UpdateFrom.GOOGLE_PLAY)
                .withListener(new AppUpdaterUtils.UpdateListener() {
                    @Override
                    public void onSuccess(Update update, Boolean isUpdateAvailable) {
                      /*  Log.d("Latest Version", update.getLatestVersion());
                        Log.d("Latest Version Code", update.getLatestVersionCode());
                        Log.d("Release notes", update.getReleaseNotes());
                        Log.d("URL", update.getUrlToDownload());*/
                        Log.d("Is update available?", Boolean.toString(isUpdateAvailable));
                        if (isUpdateAvailable) {
                            new AppUpdater(SplashActivity.this)
                                    .setUpdateFrom(UpdateFrom.GOOGLE_PLAY)
                                    .setDisplay(Display.DIALOG)
                                    .showAppUpdated(true)
                                    .start();
                        }
                    }

                    @Override
                    public void onFailed(AppUpdaterError error) {
                        Log.d("AppUpdater Error", "Something went wrong");
                    }
                });
        appUpdaterUtils.start();

        StartAnimations();

        settings = getSharedPreferences("prefs", 0);
        firstRun = settings.getBoolean("firstRun", false);
    }
    private void StartAnimations() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (firstRun == false) //if running for first time
                {
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putBoolean("firstRun", true);
                    editor.commit();
                    Intent intent_mainpage = new Intent(SplashActivity.this, LoginActivity.class);
                    intent_mainpage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent_mainpage.putExtra("company_logo", "");
                    startActivity(intent_mainpage);
                    overridePendingTransition(R.anim.translation2, R.anim.translation);

                    SplashActivity.this.finish();
                } else {
                    session = new SessionManager(getApplicationContext());
                    if (!session.isLoggedIn()) {
                        Intent intent_mainpage = new Intent(SplashActivity.this, LoginActivity.class);
                        intent_mainpage.putExtra("company_logo", "");
                        intent_mainpage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent_mainpage);
                        overridePendingTransition(R.anim.translation2, R.anim.translation);
                    } else {
                        Intent intent_mainpage = new Intent(SplashActivity.this, MainActivity.class);
                        intent_mainpage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent_mainpage);
                        overridePendingTransition(R.anim.translation2, R.anim.translation);
                    }
                }
                finish();

            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
