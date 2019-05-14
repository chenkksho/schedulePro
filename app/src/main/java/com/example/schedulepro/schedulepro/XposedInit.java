package com.example.schedulepro.schedulepro;

import android.os.Bundle;
import android.widget.Button;

import java.lang.reflect.Field;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

public class XposedInit implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) {
        if (lpparam.packageName.equals("com.example.schedulepro.schedulepro")) {
            XposedHelpers.findAndHookMethod("com.example.schedulepro.schedulepro.MainActivity", lpparam.classLoader, "onCreate", Bundle.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                    //不能通过Class.forName()来获取Class ，在跨应用时会失效
                    Class c = lpparam.classLoader.loadClass("com.example.schedulepro.schedulepro.MainActivity");
                    Field field = c.getDeclaredField("btnCommit");
                    field.setAccessible(true);
                    //param.thisObject 为执行该方法的对象，在这里指MainActivity
                    Button textView = (Button) field.get(param.thisObject);
                    textView.setText("Hello Xposed!");
                }
            });
        }
    }
}