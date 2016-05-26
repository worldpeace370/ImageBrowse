package com.lebron.imagebrowse;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.lebron.imagebrowse.bean.ImageInfo;
import com.lebron.imagebrowse.fragment.ImageDetailFragment;
import com.lebron.imagebrowse.fragment.ImageFragment;

public class MainActivity extends AppCompatActivity implements ImageFragment.ImageFragmentListener{

    private ImageFragment mImageFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        initContainer(savedInstanceState);
    }

    /**默认的savedInstanceState会存储一些数据，包括Fragment的实例
     * 只有在savedInstanceState==null时，才进行创建Fragment实例
     * @param savedInstanceState
     */
    private void initContainer(Bundle savedInstanceState){
        if (savedInstanceState == null){
            Log.i("initContainer", "initContainer: 执行了");//不论屏幕翻转如何，只执行一次
            mImageFragment = ImageFragment.newInstance(null, null);
            mImageFragment.setImageFragmentListener(this);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.container, mImageFragment, "image");
            //transaction.addToBackStack(null);如果加入回退栈，则container里是当前ImageFragment时，按下返回键会出现白屏
            transaction.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(ImageInfo data) {
        //将点击对应图片的url地址作为参数传送到ImageDetailFragment
        ImageDetailFragment fragment = ImageDetailFragment.newInstance(data.getBig_img(), null);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        //不希望mImageFragment的视图在按下返回键时重新绘制，需要hide
        transaction.hide(mImageFragment);
        transaction.add(R.id.container, fragment, "imageDetail");
        //加入回退栈，当按下返回键时，则显示刚才隐藏了的mImageFragment
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
