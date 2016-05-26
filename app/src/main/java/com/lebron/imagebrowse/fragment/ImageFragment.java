package com.lebron.imagebrowse.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lebron.imagebrowse.Config;
import com.lebron.imagebrowse.MyApplication;
import com.lebron.imagebrowse.R;
import com.lebron.imagebrowse.adapter.StaggeredGridAdapter;
import com.lebron.imagebrowse.anim.MyItemAnimator;
import com.lebron.imagebrowse.bean.ImageInfo;
import com.lebron.imagebrowse.imples.VolleyGetJson;
import com.lebron.imagebrowse.interfaces.GetJsonListener;

import java.util.LinkedList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ImageFragmentListener} interface
 * to handle interaction events.
 * Use the {@link ImageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ImageFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, GetJsonListener, StaggeredGridAdapter.OnChildClickListener{
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private ImageFragmentListener mListener;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefresh;
    private VolleyGetJson mVolleyGetJson;
    private static final String TAG = "ImageFragment";
    private LinkedList<ImageInfo> mTotalList = new LinkedList<>();
    private StaggeredGridAdapter mStaggeredGridAdapter;
    private ProgressDialog mDialog;
    private int lastVisibleItem;
    public ImageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ImageFragment.
     */
    public static ImageFragment newInstance(String param1, String param2) {
        ImageFragment fragment = new ImageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public void setImageFragmentListener(ImageFragmentListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mVolleyGetJson = new VolleyGetJson();
        loadData(1);
    }

    private void showProgressDialog(){
        mDialog = new ProgressDialog(getActivity());
        mDialog.setMessage("加载中...");
        mDialog.setCancelable(true);
        mDialog.show();
    }
    /**
     * 从服务器下载json数据
     */
    private void loadData(int page) {
        showProgressDialog();
        mVolleyGetJson.downLoadJson(String.format(Config.imageApiUrl, page), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image, container, false);
//        initRefreshViewOld(view);
        initRefreshViewNew(view);
        initRecyclerView(view);
        return view;
    }

    /**PtrFrameLayout
     * 初始化刷新控件
     * @param view
     */
//    private void initRefreshViewOld(View view){
//        //刷新的提示效果，headerView
//        StoreHouseHeader houseHeader = new StoreHouseHeader(getContext());
//        houseHeader.setPadding(0, PtrLocalDisplay.dp2px(15), 0, 0);
//        houseHeader.initWithString("Alibaba");
//        houseHeader.setTextColor(Color.BLACK);
//        houseHeader.setBackgroundColor(Color.parseColor("#cdcecf"));
//        final PtrFrameLayout ptrFrameLayout = (PtrFrameLayout) view.findViewById(R.id.store_house_ptr_frame);
//        ptrFrameLayout.setHeaderView(houseHeader);
//        ptrFrameLayout.addPtrUIHandler(houseHeader);
//        ptrFrameLayout.setPtrHandler(new PtrHandler() {
//            @Override
//            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
//                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
//            }
//
//            @Override
//            public void onRefreshBegin(PtrFrameLayout frame) {
//                ptrFrameLayout.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        ptrFrameLayout.refreshComplete();
//                    }
//                }, 2000);
//            }
//        });
//
//    }

    /**
     * 用的SwipeRefreshLayout
     * @param view
     */
    private void initRefreshViewNew(View view){
        mSwipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);
        mSwipeRefresh.setOnRefreshListener(this);
        //设置刷新时动画的颜色，可以设置4个，一种颜色持续1s
        mSwipeRefresh.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        // 这句话是为了，第一次进入页面的时候显示加载进度条
        mSwipeRefresh.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
    }
    /**
     * 初始化RecyclerView
     * 使用瀑布流方式
     * @param view
     */
    private void initRecyclerView(View view){
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        mRecyclerView.setHasFixedSize(true);
        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, LinearLayout.VERTICAL);
//        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        mRecyclerView.setLayoutManager(layoutManager);
        mStaggeredGridAdapter = new StaggeredGridAdapter(getContext(), true, mTotalList);
        mStaggeredGridAdapter.setOnChildClickListener(this);
        mRecyclerView.setAdapter(mStaggeredGridAdapter);

        MyItemAnimator itemAnimator = new MyItemAnimator();
        itemAnimator.setRemoveDuration(1000);
        itemAnimator.setMoveDuration(500);
        itemAnimator.setAddDuration(1000);
        mRecyclerView.setItemAnimator(itemAnimator);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ImageFragmentListener) {
            mListener = (ImageFragmentListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        MyApplication.getRequestQueue().cancelAll("volleyGet");
    }

    /**
     * SwipeRefreshLayout.OnRefreshListener的刷新方法
     * 如果服务器更新数据，将更新的数据显示在最前面，用到LinkedList  ,addFirst方法
     */
    @Override
    public void onRefresh() {
        loadData(1);
    }

    /**
     * loadData()完成后的回调函数
     * @param response
     */
    @Override
    public void success(String response) {
        mVolleyGetJson.getImageInfoFromJson(response, mTotalList);
        mStaggeredGridAdapter.notifyDataSetChanged();
        mDialog.dismiss();
        mSwipeRefresh.setRefreshing(false);
    }

    @Override
    public void error() {

    }

    /**
     * 点击后跳转到详情页面
     * 将data传给Activity，然后通过Activity启动ImageDetailFragment
     * @param view
     * @param position
     * @param data
     */
    @Override
    public void onChildClick(View view, int position, ImageInfo data) {
        if (mListener != null){
            mListener.onFragmentInteraction(data);
        }
    }

    /**
     * 长按删除数据
     * @param view
     * @param position
     * @param data
     */
    @Override
    public void onChildLongClick(View view, int position, ImageInfo data) {
        Toast.makeText(getActivity(), data.getName(), Toast.LENGTH_SHORT).show();
        mStaggeredGridAdapter.remove(position);
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
    public interface ImageFragmentListener {
        void onFragmentInteraction(ImageInfo data);
    }
}
