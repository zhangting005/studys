package com.sl.quartz.util;

import java.lang.reflect.Method;

import com.sl.quartz.entity.ScheduleJob;
import org.apache.commons.lang3.StringUtils;


public class TaskUtils {

    /**
     * 反射执行ScheduleJob设置的task方法
     *
     * @param scheduleJob
     */
    public static void invokMethod(ScheduleJob scheduleJob) {
        Object object = null;
        Class<?> clazz = null;
        if (StringUtils.isNotBlank(scheduleJob.getSpringid())) {
            object = SpringUtils.getBean(scheduleJob.getSpringid());
        } else if (StringUtils.isNotBlank(scheduleJob.getBeanclass())) {
            try {
                clazz = Class.forName(scheduleJob.getBeanclass());
                object = clazz.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (object == null) {
            System.out.println("task任务类加载错误");
            return;
        }
        clazz = object.getClass();
        Method method = null;
        try {
            method = clazz.getDeclaredMethod(scheduleJob.getMethodname());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (method != null) {
            try {
                method.invoke(object);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("任务名称 = [" + scheduleJob.getJobname() + "]----------正在运行");
    }

}
