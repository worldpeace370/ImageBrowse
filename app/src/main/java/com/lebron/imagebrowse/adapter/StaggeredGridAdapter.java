package com.lebron.imagebrowse.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.lebron.imagebrowse.Config;
import com.lebron.imagebrowse.MyApplication;
import com.lebron.imagebrowse.R;
import com.lebron.imagebrowse.bean.ImageInfo;
import com.lebron.imagebrowse.imples.MyBitmapCache;

import java.util.List;

public class StaggeredGridAdapter extends RecyclerView.Adapter<StaggeredGridAdapter.ListHolder> implements View.OnClickListener,
View.OnLongClickListener{

    private Context context;
    private boolean flag;
    private List<ImageInfo> mTotalList;
    //通过recyclerView来获取recyclerView.getChildAdapterPosition(v);
    private RecyclerView recyclerView;
    private OnChildClickListener listener;

    int iconsV[] = {R.mipmap.p1,R.mipmap.p2,R.mipmap.p3,R.mipmap.p4,R.mipmap.p5,R.mipmap.p6,R.mipmap.p7,R.mipmap.p9,
            R.mipmap.p10,R.mipmap.p11,R.mipmap.p12,R.mipmap.p13,R.mipmap.p14,R.mipmap.p15,R.mipmap.p16,R.mipmap.p17,R.mipmap.p18,R.mipmap.p19,
            R.mipmap.p20,R.mipmap.p21,R.mipmap.p22,R.mipmap.p23,R.mipmap.p24,R.mipmap.p25,R.mipmap.p26,R.mipmap.p27,R.mipmap.p28,R.mipmap.p29,
            R.mipmap.p30,R.mipmap.p31,R.mipmap.p32,R.mipmap.p33,R.mipmap.p34,R.mipmap.p35,R.mipmap.p36,R.mipmap.p37,R.mipmap.p38,R.mipmap.p39,
            R.mipmap.p40,R.mipmap.p41,R.mipmap.p42,R.mipmap.p43,R.mipmap.p44};

    int iconsH[] = {R.mipmap.h1,R.mipmap.h2,R.mipmap.h3,R.mipmap.h4,R.mipmap.h5,R.mipmap.h6,R.mipmap.h7,R.mipmap.h9,
            R.mipmap.h10,R.mipmap.h11,R.mipmap.h12,R.mipmap.h13,R.mipmap.h14,R.mipmap.h15,R.mipmap.h16,R.mipmap.h17,R.mipmap.h18,R.mipmap.h19,
            R.mipmap.h20,R.mipmap.h21,R.mipmap.h22,R.mipmap.h23,R.mipmap.h24,R.mipmap.h25,R.mipmap.h26,R.mipmap.h27,R.mipmap.h28,R.mipmap.h29,
            R.mipmap.h30,R.mipmap.h31,R.mipmap.h32,R.mipmap.h33,R.mipmap.h34,R.mipmap.h35,R.mipmap.h36,R.mipmap.h37,R.mipmap.h38,R.mipmap.h39,
            R.mipmap.h40,R.mipmap.h41,R.mipmap.h42,R.mipmap.h43,R.mipmap.h44};

    public StaggeredGridAdapter(Context context, boolean flag, List<ImageInfo> list){
        this.context = context;
        this.flag = flag;
        this.mTotalList = list;
    }


    /**得到添加该适配器的RecyclerView
     * Called by RecyclerView when it starts observing this Adapter.
     * Keep in mind that same adapter may be observed by multiple RecyclerViews.
     * @param recyclerView
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    public void setOnChildClickListener(OnChildClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(flag ? R.layout.staggered_grid_item_v : R.layout.staggered_grid_item_h, parent, false);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        return new ListHolder(view);
    }


    @Override
    public void onBindViewHolder(ListHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return mTotalList.size();
    }

    public void remove(int position){
        if (mTotalList != null){
            mTotalList.remove(position);
            notifyItemRemoved(position);
        }
    }
    public void add(int position, ImageInfo data){
        if (mTotalList != null){
            mTotalList.add(position, data);
            notifyItemInserted(position);
        }
    }

    /**
     * 点击每个Item的监听事件，跳转到详情又调用自定义的监听方法
     * 需要在用到该StaggeredGridAdapter的地方实现
     * OnChildClickListener接口。
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (listener != null){
            int position = recyclerView.getChildAdapterPosition(v);
            listener.onChildClick(v, position, mTotalList.get(position));
        }
    }

    /**
     * 长按每个Item进行删除操作的接口
     * @param v
     * @return true，避免和onClick()同时响应
     */
    @Override
    public boolean onLongClick(View v) {
        if (listener != null){
            int position = recyclerView.getChildAdapterPosition(v);
            listener.onChildLongClick(v, position, mTotalList.get(position));
        }
        return true;
    }

    class ListHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView name;

        public ListHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.pic);
            name = (TextView) itemView.findViewById(R.id.name);
        }

        public void setData(int position){
//======   ====== ====== ImageLoader方法，带缓存====== ====== ====== ====== ====== ====== ====== ======
            //1.实例化ImageLoader
            ImageLoader imageLoader = new ImageLoader(MyApplication.getRequestQueue(),
                    MyBitmapCache.getInstance());
            //2.设置监听器
            ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(icon,
                    R.mipmap.ic_launcher, R.mipmap.ic_launcher);
            //3.获取图片
            imageLoader.get(Config.bigImageAddress + mTotalList.get(position).getBig_img(), imageListener);
            name.setText(mTotalList.get(position).getName());


//======   ====== ====== ImageRequest方法====== ====== ====== ====== ====== ====== ====== ======
//            ImageRequest imageRequest = null;
//            if (flag) {
//                Log.i("StaggeredGridAdapter", "image url: " + Config.smallImageAddress +
//                        mTotalList.get(position).getSmall_img());
//                imageRequest = new ImageRequest(Config.bigImageAddress +
//                        mTotalList.get(position).getBig_img(), new Response.Listener<Bitmap>() {
//                    @Override
//                    public void onResponse(Bitmap response) {
//                        icon.setImageBitmap(response);
//                    }
//                }, 0, 0, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.RGB_565, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        //设置一张错误的图片，临时用ic_launcher代替
//                        icon.setImageResource(R.mipmap.ic_launcher);
//                    }
//                });
////                icon.setImageResource(iconsV[position % iconsV.length]);
//            } else {
//                icon.setImageResource(iconsH[position % iconsH.length]);
//            }
//            name.setText(mTotalList.get(position).getName());
//
//            MyApplication.getRequestQueue().add(imageRequest);
        }

    }

    /**
     * RecyclerView的每个Item的点击事件
     * onChildClick()跳转到详情
     * onChildLongClick()删除该条目
     */
    public interface OnChildClickListener{
        void onChildClick(View view, int position, ImageInfo data);
        void onChildLongClick(View view, int position, ImageInfo data);
    }
}
