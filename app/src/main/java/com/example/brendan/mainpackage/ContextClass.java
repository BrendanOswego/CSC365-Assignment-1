package com.example.brendan.mainpackage;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by brendan on 4/10/17.
 */

public class ContextClass {
    private static Application APP = null;

    public static Context getContext(){
        return APP.getApplicationContext();
    }
    static{
        try{
            Class<?> c = Class.forName("android.app.ActivityThread");
            APP = (Application) c.getDeclaredMethod("currentApplication").invoke(null);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
