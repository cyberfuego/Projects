package com.example.android.android_me.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import com.example.android.android_me.R;
import com.example.android.android_me.data.AndroidImageAssets;

public class MainActivity extends AppCompatActivity implements MasterListFragment.ImageClickListener {
    int headposition, bodyposition, legposition, absoluteposiotion;
    Button button;
    Boolean mTwoPaneLayout = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AndroidMeActivity.class);
                Bundle bundle = new Bundle();
                Log.d("Position", " " + headposition + bodyposition + legposition);
                bundle.putInt("head", headposition);
                bundle.putInt("body", bodyposition);
                bundle.putInt("leg", legposition);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        if (findViewById(R.id.android_me_linear_layout) != null) {
           // button.setVisibility(View.GONE);

            mTwoPaneLayout = true;
            GridView grid = (GridView) findViewById(R.id.grid);
            grid.setNumColumns(2);
            button.setVisibility(View.GONE);
            if(savedInstanceState == null) {
                Log.d("Ankush", "Hi I am inside Tab View");
                // Create a new head BodyPartFragment
                BodyPartFragment headFragment = new BodyPartFragment();

                // Set the list of image id's for the head fragment and set the position to the second image in the list
                headFragment.setImageIds(AndroidImageAssets.getHeads());
                headFragment.setListIndex(headposition);

                // Add the fragment to its container using a FragmentManager and a Transaction
                FragmentManager fragmentManager = getSupportFragmentManager();

                fragmentManager.beginTransaction()
                        .add(R.id.head_container, headFragment)
                        .commit();

                // Create and display the body and leg BodyPartFragments

                BodyPartFragment bodyFragment = new BodyPartFragment();
                bodyFragment.setImageIds(AndroidImageAssets.getBodies());
                bodyFragment.setListIndex(bodyposition);
                fragmentManager.beginTransaction()
                        .add(R.id.body_container, bodyFragment)
                        .commit();

                BodyPartFragment legFragment = new BodyPartFragment();
                legFragment.setImageIds(AndroidImageAssets.getLegs());
                legFragment.setListIndex(legposition);
                fragmentManager.beginTransaction()
                        .add(R.id.leg_container, legFragment)
                        .commit();
            }

        }

    }

    @Override
    public void imageClicked(int position) {
        absoluteposiotion = position;
        int elementchanged = absoluteposiotion / 12;
        int sendposition = absoluteposiotion - elementchanged * 12;
        switch (elementchanged) {
            case 0:
                headposition = sendposition;
                if(mTwoPaneLayout) {
                    BodyPartFragment fragment = new BodyPartFragment();
                    FragmentManager manager = getSupportFragmentManager();
                    fragment.setImageIds(AndroidImageAssets.getHeads());
                    fragment.setListIndex(headposition);
                    manager.beginTransaction().replace(R.id.head_container, fragment).commit();
                }
                break;
            case 1:
                bodyposition = sendposition;
                if(mTwoPaneLayout) {
                    BodyPartFragment fragment2 = new BodyPartFragment();
                    FragmentManager manager2 = getSupportFragmentManager();
                    fragment2.setImageIds(AndroidImageAssets.getBodies());
                    fragment2.setListIndex(bodyposition);
                    manager2.beginTransaction().replace(R.id.body_container, fragment2).commit();
                }
                break;
            case 2:
                legposition = sendposition;
                if(mTwoPaneLayout) {
                    BodyPartFragment fragment3 = new BodyPartFragment();
                    FragmentManager manager3 = getSupportFragmentManager();
                    fragment3.setImageIds(AndroidImageAssets.getLegs());
                    fragment3.setListIndex(legposition);
                    manager3.beginTransaction().replace(R.id.leg_container, fragment3).commit();
                }
                break;
        }
        Log.d("Position", " " + headposition + bodyposition + legposition);
    }
}
