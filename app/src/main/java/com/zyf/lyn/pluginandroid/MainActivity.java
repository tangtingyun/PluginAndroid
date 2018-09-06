package com.zyf.lyn.pluginandroid;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zyf.lyn.pluginandroid.utils.AMSHookHelper;
import com.zyf.lyn.pluginandroid.utils.Utils;

import java.io.File;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

public class MainActivity extends AppCompatActivity {

    private AssetManager mAssetManager;
    private Resources mResources;
    private Resources.Theme mTheme;
    private String dexpath = null;    //apk文件地址
    private File fileRelease = null;  //释放目录
    private DexClassLoader classLoader = null;

    private String apkName = "app-debug.apk";    //apk名称

    private TextView mHook;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);

        try {
            AMSHookHelper.hookAMN();
        } catch (Throwable throwable) {
            throw new RuntimeException("hook failed", throwable);
        }


        try {
            Utils.extractAssets(newBase, apkName);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHook = (TextView) findViewById(R.id.tv_hook);


        File extractFile = this.getFileStreamPath(apkName);
        dexpath = extractFile.getPath();

        fileRelease = getDir("dex", 0); //0 表示Context.MODE_PRIVATE

        Log.d("DEMO", "dexpath:" + dexpath);
        Log.d("DEMO", "fileRelease.getAbsolutePath():" +
                fileRelease.getAbsolutePath());

        classLoader = new DexClassLoader(dexpath,
                fileRelease.getAbsolutePath(), null, getClassLoader());


        mHook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, TargetActivity.class);
//                startActivity(intent);


                Class mLoadClassBean;
                try {
                    mLoadClassBean = classLoader.loadClass("com.zyf.lyn.plugin_one.Bean");
                    Object beanObject = mLoadClassBean.newInstance();

                    Method getNameMethod = mLoadClassBean.getMethod("getName");
                    getNameMethod.setAccessible(true);
                    String name = (String) getNameMethod.invoke(beanObject);

                    mHook.setText(name);
                    Toast.makeText(getApplicationContext(), name, Toast.LENGTH_LONG).show();

                } catch (Exception e) {
                    Log.e("DEMO", "msg:" + e.getMessage());
                }
            }
        });
    }
}
