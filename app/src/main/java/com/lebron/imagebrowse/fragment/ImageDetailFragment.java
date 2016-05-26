package com.lebron.imagebrowse.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.lebron.imagebrowse.Config;
import com.lebron.imagebrowse.MyApplication;
import com.lebron.imagebrowse.R;
import com.lebron.imagebrowse.imples.MyBitmapCache;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.StoreHouseHeader;
import in.srain.cube.views.ptr.util.PtrLocalDisplay;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ImageDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ImageDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ImageDetailFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String bigImageUrl;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private PtrClassicFrameLayout mPtrFrame;
    private ImageView imageDetail;
    private ProgressDialog mDialog;

    public ImageDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ImageDetailFragment.
     */
    public static ImageDetailFragment newInstance(String param1, String param2) {
        ImageDetailFragment fragment = new ImageDetailFragment();
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
            bigImageUrl = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_image_detail, container, false);
        mPtrFrame = (PtrClassicFrameLayout) contentView.findViewById(R.id.rotate_header_grid_view_frame);
        mPtrFrame.setLastUpdateTimeRelateObject(this);
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mPtrFrame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPtrFrame.refreshComplete();
                    }
                }, 2000);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });
        //刷新的提示效果，headerView
        StoreHouseHeader houseHeader = new StoreHouseHeader(getContext());
        houseHeader.setPadding(0, PtrLocalDisplay.dp2px(15), 0, 0);
        houseHeader.initWithString("Alibaba");
        houseHeader.setTextColor(Color.BLACK);
        houseHeader.setBackgroundColor(Color.parseColor("#cdcecf"));
        mPtrFrame.setHeaderView(houseHeader);
        mPtrFrame.addPtrUIHandler(houseHeader);
        imageDetail = (ImageView) contentView.findViewById(R.id.imageDetail);
        return contentView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showProgressDialog();
        loadImageDetail2();
    }

    private void showProgressDialog(){
        mDialog = new ProgressDialog(getActivity());
        mDialog.setMessage("加载中...");
        mDialog.setCancelable(true);
        mDialog.show();
    }

    /**
     * ImageRequest方法
     */
    private void loadImageDetail() {
        //构建ImageRequest 实例
        final ImageRequest imageRequest = new ImageRequest(Config.bigImageAddress +
                bigImageUrl, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    //给imageView设置图片
                    imageDetail.setImageBitmap(response);
                    mDialog.dismiss();
                }
            }, 0, 0, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //设置一张错误的图片，临时用ic_launcher代替
                    imageDetail.setImageResource(R.mipmap.ic_launcher);
                }
            });

        MyApplication.getRequestQueue().add(imageRequest);
    }

    /**
     * ImageLoader方法
     */
    private void loadImageDetail2(){
        //1.实例化ImageLoader
        ImageLoader imageLoader = new ImageLoader(MyApplication.getRequestQueue(),
                MyBitmapCache.getInstance());
        //2.设置监听器
        ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(imageDetail,
                R.mipmap.ic_launcher, R.mipmap.ic_launcher);
        //3.获取图片
        imageLoader.get(Config.bigImageAddress + bigImageUrl, imageListener);
        mDialog.dismiss();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
