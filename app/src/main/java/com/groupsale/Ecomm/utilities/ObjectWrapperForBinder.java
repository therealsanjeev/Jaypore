package com.groupsale.Ecomm.utilities;

import android.os.Binder;

import com.groupsale.Ecomm.models.DealModel;


/*This helps in passing whole deal object between activities, as intent bundles
    To send
     final Bundle bundle = new Bundle();
     bundle.putBinder("KEY_NAME", new ObjectWrapperForBinder(dealObject);
     context.startActivity(new Intent(SendingActivity.this, SecondActivity.class).putExtras(bundle));

    To receive
    deal = ((ObjectWrapperForBinder)getIntent().getExtras().getBinder("KEY_NAME")).getData();

 */



public class ObjectWrapperForBinder extends Binder {

    private final DealModel mData;

    public ObjectWrapperForBinder(DealModel data) {
        mData = data;
    }

    public DealModel getData() {
        return mData;
    }
}