package com.groupsale.Ecomm;

import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.groupsale.Ecomm.customDialogs.PaymentBottomDialogFragment;
import com.groupsale.Ecomm.fragment.StoreFragment;
import com.groupsale.Ecomm.models.items;
import com.groupsale.Ecomm.models.products;
import com.groupsale.Ecomm.retrofit.ApiInterface;
import com.groupsale.Ecomm.retrofit.RetrofitClientInstance;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.groupsale.Ecomm.customDialogs.PaymentBottomDialogFragment.newInstance;

public class WebAppInterface extends Fragment {
    Context mContext;
    String link;
    String key = "Bearer uaakmqwdf0lv08ry66zbwkirgp2jrx9w";  //

    WebAppInterface(Context c) {
        mContext = c;
    }

    @JavascriptInterface
    public void showToast(String sku) {

        Retrofit retrofit2 = RetrofitClientInstance.getRetrofitInstance();

        Toast.makeText(mContext, "Setting up your deal!...", Toast.LENGTH_LONG).show();

        ApiInterface jsonPlaceHolderApi = retrofit2.create(ApiInterface.class);

        jsonPlaceHolderApi.getskuitem(key,sku).enqueue(new Callback<items>() {

            @Override
            public void onResponse(Call<items> call, Response<items> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "Code: "+response.code(), Toast.LENGTH_SHORT).show();
                    Log.d("Tag",response.code()+" ;"+response.message()+" ;"+response.errorBody());
                    return;
                }
                List<products> products = response.body().getProducts();

                PaymentBottomDialogFragment dialogFragment = newInstance(products);

                dialogFragment.getContext(mContext);

                dialogFragment.show(((FragmentActivity)mContext).getSupportFragmentManager(), "paymentDialog");


            }

            @Override
            public void onFailure(Call<items> call, Throwable t)
            {
                Toast.makeText(mContext, "Failed: "+t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("Tag",t.getMessage()+ " "+ t.getLocalizedMessage()+" "+t.getCause());
            }

        });

    }



    public WebAppInterface(StoreFragment deal) {
    }

    public void sharelink(String link1) {
        link = link1;
        Toast.makeText(mContext, link1, Toast.LENGTH_SHORT).show();


    }

    public void datapass(int a, int b, int c) {

        Toast.makeText(mContext, String.valueOf(a), Toast.LENGTH_SHORT).show();
        Toast.makeText(mContext, String.valueOf(b), Toast.LENGTH_SHORT).show();
        Toast.makeText(mContext, String.valueOf(c), Toast.LENGTH_SHORT).show();


    }

}



