package com.groupsale.Ecomm.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.groupsale.Ecomm.R;
import com.groupsale.Ecomm.adapters.CategoryAdapter;
import com.groupsale.Ecomm.adapters.PostAdapter;
import com.groupsale.Ecomm.models.PostModel;

import java.util.ArrayList;
import java.util.List;

public class UserActivity extends AppCompatActivity {


    RecyclerView postRecyclerView;
    PostAdapter postAdapter;



    List<String> categoryList = new ArrayList<>();
    List<PostModel> postList = new ArrayList<>();

    String name[] = {"Heema","Rekha","Jaya","Sushma","Gaurav","Vicky","Rao"};
    String postImageUrl[] = {"https://static.jaypore.com/tr:w-302,h-527,e-sharpen/media/cms_component/1908202106422320210819_Magnificence_in_every_stitch_Desktop.jpg",
            "https://static.jaypore.com/tr:w-302,h-527,e-sharpen/media/cms_component/1908202106513020210819_Silken_treasures_more_Desktop.jpg",
            "https://static.jaypore.com/tr:w-307,h-363,e-sharpen/media/events/120821CUR090_SILVER_TRADITIONAL_JW_moodshot.jpg",
            "https://static.jaypore.com/media/events/307X363/7557_102451049_Karomi_moodshot.jpg","https://static.jaypore.com/tr:w-500,h-662,e-sharpen/media/catalog/product/8/4/840008466-1_2.jpg",
            "https://static.jaypore.com/tr:w-313,h-415,e-sharpen/media/catalog/product/8/4/840008704-2_10.jpg","https://static.jaypore.com/tr:w-313,h-415,e-sharpen/media/catalog/product/8/4/840008443-2_1.jpg"};
    String text[] ={"Please buy this white and blue apparel, looks cool!","Omg! This bag is probably the best out there","This is just out of the world!!",
            "My Casual Looks","I think I am gonna wear this for the office party tonight.","Chilling in Kerela, this shirt is amazing!","Abki baar, hamari sarkar!"};
    int likes[] = {2345,5674,8896,1178,567,377,10987};
    String profileImage[] = {"https://www.iconsdb.com/icons/preview/soylent-red/contacts-xxl.png","https://www.iconsdb.com/icons/preview/soylent-red/contacts-xxl.png," +
            "https://www.iconsdb.com/icons/preview/soylent-red/contacts-xxl.png","https://www.iconsdb.com/icons/preview/soylent-red/contacts-xxl.png","https://www.iconsdb.com/icons/preview/soylent-red/contacts-xxl.png",
            "https://www.iconsdb.com/icons/preview/soylent-red/contacts-xxl.png","https://www.iconsdb.com/icons/preview/soylent-red/contacts-xxl.png","https://www.iconsdb.com/icons/preview/soylent-red/contacts-xxl.png"};

    TextView userToolBarName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        userToolBarName=findViewById(R.id.toolbarUsername);
        findViewById(R.id.btnBackUser).setOnClickListener(v -> onBackPressed());
        addValues(name,postImageUrl,text,likes,profileImage);


        Bundle bundle = getIntent().getExtras();
        String usename = bundle.getString("userName");
        userToolBarName.setText(usename);


        postRecyclerView=findViewById(R.id.userCollectionRecycleView);
        postAdapter = new PostAdapter();

        postAdapter.SetData(postList);

        postRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        postRecyclerView.setAdapter(postAdapter);
    }

    public void addValues(String name[], String imageUrl[],
                          String text[],int like[],String profileImage[])
    {
        int n=7;

        for (int i = 0; i < n; i++)
        {

            postList.add(new PostModel(name[i], imageUrl[i], text[i],
                    like[i],profileImage[i]));
        }

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}