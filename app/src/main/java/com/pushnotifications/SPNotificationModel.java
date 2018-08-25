package com.pushnotifications;

import android.app.Activity;
import android.content.Context;

/**
 * Created by Relevant-20 on 9/18/2017.
 */

public class SPNotificationModel {
    private Context context;
    private String bodyMessage;
    private String title;
    public SPNotificationModel(Context context, String bodyMessage, String title) {
        this.context = context;
        this.bodyMessage = bodyMessage;
        this.title = title;
    }
    public Activity getActivity(){
        if(context instanceof Activity){
            return (Activity)context;
        }
        return null;
    }

    public Context getContext() {
        return context;
    }

    public String getBodyMessage() {
        return bodyMessage;
    }

    public String getTitle() {
        return title;
    }

}
