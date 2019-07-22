package site.nihaoa.smarttrash.tools;

import android.app.Activity;
import android.util.Log;
import android.view.ActionMode;
import android.view.View;

import java.lang.reflect.Field;

public class ViewTools {

    public static void register(Object thisClass, View parent){
        Class cla = thisClass.getClass();
        Field[] fields = cla.getDeclaredFields();
        for (Field field:fields){
            if(field.isAnnotationPresent(ViewBean.class)){
                ViewBean viewBean = field.getAnnotation(ViewBean.class);
                try {
                    field.setAccessible(true);
                    field.set(thisClass,parent.findViewById(viewBean.id()));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static void register(Object thisClass, Activity parent){
        Class cla = thisClass.getClass();
        Field[] fields = cla.getDeclaredFields();
        for (Field field:fields){
            if(field.isAnnotationPresent(ViewBean.class)){
                ViewBean viewBean = field.getAnnotation(ViewBean.class);
                try {
                    field.setAccessible(true);
                    field.set(thisClass,parent.findViewById(viewBean.id()));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
