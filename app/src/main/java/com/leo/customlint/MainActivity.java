package com.leo.customlint;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import top.omooo.customlint.R;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";
    private ImageView mIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mIv = findViewById(R.id.iv);

    }

    /**
     * 注意：
     * LagDetector
     * 这是一个 Error 级别的 Lint 警告
     * 编译时会终止编译
     * 运行时可以注释掉，或者配置 LintOptions
     */
    private void testLogUsage() {

        //Android 自带的 ToastDetector
        Toast.makeText(this, "", Toast.LENGTH_SHORT);

        //SampleCodeDetector
        System.out.println("Omooo");

        //ParseCheckDetector
        Integer integer = ToolNumber.toInt("1232");
        try {
            int j = Integer.parseInt("111");
        } catch (Exception e) {
            int i = Integer.parseInt("111");
        }

        Integer integer1 = ToolNumber.parseInt("1232");

        //PngDetector
        mIv.setImageResource(R.drawable.google);

        //LogDetector
        Log.i(TAG, "啊啊啊啊，我被发现了！");

        //ThreadDetector
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();
    }
}