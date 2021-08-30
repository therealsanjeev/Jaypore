package com.groupsale.Ecomm.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.groupsale.Ecomm.R;
import com.groupsale.Ecomm.WebAppInterface;
import com.groupsale.Ecomm.activities.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StoreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StoreFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private WebView webView;
    


    public StoreFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Deal.
     */
    // TODO: Rename and change types and number of parameters
    public static StoreFragment newInstance(String param1, String param2) {
        StoreFragment fragment = new StoreFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    private Context mContext;
    private Activity mActivity;

    private RelativeLayout mRelativeLayout;
    private WebView mWebView;
    private Button mButtonBack;
    private Button mButtonForward;

    private String mUrl="https://www.jaypore.com/";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_deal, container, false);

        // Get the application context
        mContext = requireActivity();
        // Get the activity

//        mActivity = MainActivity.this;
        mWebView =  v.findViewById(R.id.web_home);

// Request to render the web page
        renderWebPage(mUrl);


//        WebView webView = (WebView) v.findViewById(R.id.web_home);
//
//        webView.loadUrl("https://www.jaypore.com/");
//        WebSettings webSettings = webView.getSettings();
//        webSettings.setJavaScriptEnabled(true);
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.setWebViewClient(new WebViewClient());
//        webView.addJavascriptInterface(new WebAppInterface(this), "Android");

//          webView.canGoBack() ;
//          webView.setOnKeyListener(new View.OnKeyListener() {
//              @Override
//              public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (keyCode == event.KEYCODE_BACK
//                && event.getAction() == MotionEvent.ACTION_UP
//                        && webView.canGoBack()){
//                          webView.goBack();
//                       return true;
//                  }
//                  return false;
//              }
//          });
        return v;

    }

    // Custom method to render a web page
    protected void renderWebPage(String urlToRender) {
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                Toast.makeText(requireContext(), "Got Error! $error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // Do something on page loading started
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // Do something when page loading finished
                // Check web view back history availability
                if (mWebView.canGoBack()) {
                    mButtonBack.setEnabled(true);
                } else {
                    mButtonBack.setEnabled(false);
                }

                // Check web view forward history availability
                if (mWebView.canGoForward()) {
                    mButtonForward.setEnabled(true);
                } else {
                    mButtonForward.setEnabled(false);
                }
                if (view.getContentHeight() == 0)
                view.reload();
                else{
                    super.onPageFinished(view, url);
                }

            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int newProgress) {
            }
        });

        /*
            public WebSettings getSettings ()
                Gets the WebSettings object used to control the settings for this WebView.

            Returns
                a WebSettings object that can be used to control this WebView's settings
        */
        /*
            WebSettings
                Manages settings state for a WebView. When a WebView is first created, it obtains a
                set of default settings. These default settings will be returned from any getter
                call. A WebSettings object obtained from WebView.getSettings() is tied to the life
                of the WebView. If a WebView has been destroyed, any method call on WebSettings will
                throw an IllegalStateException.
        */

        // Enable the javascript
        mWebView.getSettings().setJavaScriptEnabled(true);

        /*
            public abstract void setAppCacheEnabled (boolean flag)
                Sets whether the Application Caches API should be enabled. The default is false.
                Note that in order for the Application Caches API to be enabled, a valid database
                path must also be supplied to setAppCachePath(String).

            Parameters
                flag : true if the WebView should enable Application Caches
        */
        // Enable the caching for web view
        mWebView.getSettings().setAppCacheEnabled(true);

        /*
            public abstract void setAppCachePath (String appCachePath)
                Sets the path to the Application Caches files. In order for the Application Caches
                API to be enabled, this method must be called with a path to which the application
                can write. This method should only be called once: repeated calls are ignored.

            Parameters
                appCachePath : a String path to the directory containing Application Caches files.
        */
        /*
            public abstract File getCacheDir ()
                Returns the absolute path to the application specific cache directory on the
                filesystem. These files will be ones that get deleted first when the device runs
                low on storage. There is no guarantee when these files will be deleted.

                Note: you should not rely on the system deleting these files for you; you should
                always have a reasonable maximum, such as 1 MB, for the amount of space you consume
                with cache files, and prune those files when exceeding that space.

                The returned path may change over time if the calling app is moved to an adopted
                storage device, so only relative paths should be persisted.

                Apps require no extra permissions to read or write to the returned path,
                since this path lives in their private storage.

            Returns
                The path of the directory holding application cache files.
        */
        /*
            public String getPath ()
                Returns the path of this file.
        */
        // Specify the app cache path
        mWebView.getSettings().setAppCachePath(mContext.getCacheDir().getPath());

        /*
            public abstract void setCacheMode (int mode)
                Overrides the way the cache is used. The way the cache is used is based on the
                navigation type. For a normal page load, the cache is checked and content is
                re-validated as needed. When navigating back, content is not re-validated, instead
                the content is just retrieved from the cache. This method allows the client to
                override this behavior by specifying one of
                    LOAD_DEFAULT,
                    LOAD_CACHE_ELSE_NETWORK,
                    LOAD_NO_CACHE or
                    LOAD_CACHE_ONLY.
                The default value is LOAD_DEFAULT.

            Parameters
                mode : the mode to use
        */
        /*
            public static final int LOAD_DEFAULT
                Default cache usage mode. If the navigation type doesn't impose any specific
                behavior, use cached resources when they are available and not expired, otherwise
                load resources from the network. Use with setCacheMode(int).

            Constant Value: -1 (0xffffffff)
        */
        /*
            public static final int LOAD_CACHE_ELSE_NETWORK
                Use cached resources when they are available, even if they have expired. Otherwise
                load resources from the network. Use with setCacheMode(int).

            Constant Value: 1 (0x00000001)
        */
        /*
            public static final int LOAD_NO_CACHE
                Don't use the cache, load from the network. Use with setCacheMode(int).

            Constant Value: 2 (0x00000002)
        */
        /*
            public static final int LOAD_CACHE_ONLY
                Don't use the network, load from the cache. Use with setCacheMode(int).

            Constant Value: 3 (0x00000003)
        */
        // Set the cache mode
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);

        // Render the web page
        mWebView.loadUrl(urlToRender);
    }
//    @Override
//    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
//        super.onViewStateRestored(savedInstanceState);
//    }
}
