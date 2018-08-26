package com.why.project.viewpagerwithimagedemo;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.why.project.viewpagerwithimagedemo.bean.PictureBean;
import com.why.project.viewpagerwithimagedemo.viewpager.MyCustomViewPager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

	private Context mContext;

	private EditText mPicTitleEdt;
	private TextView mPageTv;

	/**中间viewpager区域*/
	private MyCustomViewPager mViewPager;
	/**ViewPager适配器*/
	private MyViewPagerAdapter mViewPageAdapter;
	//viewpager的数据集合
	private ArrayList<PictureBean> mPictureBeanList;
	/**viewpager中当前页面的下标值*/
	private int currentItemIndex = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mContext = this;

		//初始化控件以及设置
		initView();
		//初始化数据
		initData();
		//初始化控件的点击事件
		initEvent();
	}

	@Override
	public void onDestroy() {
		mViewPager.removeAllViews();//防止内存泄漏
		System.gc();//回收

		super.onDestroy();
	}

	private void initView() {
		mPicTitleEdt = findViewById(R.id.edt_title);
		mPageTv = findViewById(R.id.tv_page);

		mViewPager = (MyCustomViewPager) findViewById(R.id.view_pager);
		mViewPager.setOffscreenPageLimit(3);//设置预加载的页数，之前是3【这个值指的是，当前view的左右两边的预加载的页面的个数。也就是说，如果这个值mOffscreenPageLimit　＝　3，那么任何一个页面的左边可以预加载3个页面，右边也可以加载3页面。】
	}

	private void initData() {
		//初始化数据
		mPictureBeanList = new ArrayList<PictureBean>();
		PictureBean bean0 = new PictureBean();
		bean0.setPicId("pic0");
		bean0.setPicTitle("图片1");
		bean0.setPicResId(R.drawable.pic_0);
		mPictureBeanList.add(bean0);
		//
		PictureBean bean1 = new PictureBean();
		bean1.setPicId("pic1");
		bean1.setPicTitle("图片2");
		bean1.setPicResId(R.drawable.pic_1);
		mPictureBeanList.add(bean1);
		//
		PictureBean bean2 = new PictureBean();
		bean2.setPicId("pic0");
		bean2.setPicTitle("图片3");
		bean2.setPicResId(R.drawable.pic_2);
		mPictureBeanList.add(bean2);
		//
		PictureBean bean3 = new PictureBean();
		bean3.setPicId("pic3");
		bean3.setPicTitle("图片4");
		bean3.setPicResId(R.drawable.pic_3);
		mPictureBeanList.add(bean3);

		//设置页码
		if(mPictureBeanList.size() > 0){
			showPageNum();
		}
		//填充viewpager数据
		initViewPage();
	}

	private void initEvent() {

	}

	//设置页码和标题输入框
	private void showPageNum() {
		mPageTv.setText((currentItemIndex+1) + "/" + mPictureBeanList.size());

		String picTitle = mPictureBeanList.get(currentItemIndex).getPicTitle();
		mPicTitleEdt.setText(picTitle);
	}

	/**初始化viewpager配置*/
	private void initViewPage(){
		if(mViewPageAdapter == null){
			mViewPageAdapter = new MyViewPagerAdapter();
			mViewPager.setAdapter(mViewPageAdapter);

			mViewPager.addOnPageChangeListener(new MyOnPageChangeListener());//设置页面切换监听事件
			mViewPager.setIsCanScroll(true);//允许滑动
		}else{
			mViewPageAdapter.notifyDataSetChanged();
		}
		mViewPager.setCurrentItem(currentItemIndex);
	}

	/**ViewPager适配器*/
	public class MyViewPagerAdapter extends PagerAdapter
	{
		/**这个方法，是从ViewGroup中移出当前View*/
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(((View)object));
		}

		/**这个方法，是获取viewpager的界面数*/
		@Override
		public int getCount() {
			return mPictureBeanList.size();
		}


		public int getItemPosition(Object object) {
			return POSITION_NONE;//-2
		}

		/**这个方法，return一个对象，这个对象表明了PagerAdapter将选择将这个对象填充到在当前ViewPager里*/
		@Override
		public Object instantiateItem(ViewGroup container, int position){
			View layout = LayoutInflater.from(mContext).inflate(R.layout.view_pager_layout, null);//将布局文件view添加到viewpager中
			container.addView((View)layout);

			ImageView mPicImgView = layout.findViewById(R.id.img_pic);
			mPicImgView.setImageResource(mPictureBeanList.get(position).getPicResId());

			return layout;
		}

		/**这个方法，在帮助文档中原文是could be implemented as return view == object,也就是用于判断是否由对象生成界面*/
		@Override
		public boolean isViewFromObject(View view, Object object) {
			// TODO Auto-generated method stub
			return view == object ? true : false;//官方提示这样写
		}
		@Override
		public void notifyDataSetChanged()
		{
			super.notifyDataSetChanged();
		}
	}

	/**ViewPage切换的事件监听
	 * http://blog.csdn.net/zhengxiaoyao0716/article/details/48805703*/
	public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener
	{
		private boolean isNormalChange = false;//标明是否普通的上翻页和下翻页【默认为false，标明需要判断然后弹出对话框】
		private int resetXPoint = 0;//获取弹出对话框，还原的scrollTo的X坐标值，默认是0.打开的是第几页，比如，如果是第二页，那么第二页的X坐标是0 + (2-1)*viwepager的宽度

		/* 这个方法在手指操作屏幕的时候发生变化。有三个值：0（END）,1(PRESS) , 2(UP) 。
		 * arg0 ==1的时辰默示正在滑动，arg0==2的时辰默示滑动完毕了，arg0==0的时辰默示什么都没做。
		 */
		@Override
		public void onPageScrollStateChanged(int state) {
			if(state == 1){
				isNormalChange = false;
			}
		}

		/* 用户一次滑动，这个方法会持续调用N多次，直至某个View充满视图并且稳定住！（但具体调用次数也不确定，尤其在首末位置向边界滑动，如果Log一下，会看到出现调用不确定次数的打印，且positionOffset都为0.
		 * position 当前页面，及你点击滑动的页面【position为当前屏幕上所露出的所有View的Item取下限。比如，当前Item为3，轻轻向右滑动一下，2露出了一点点，那么position就是2；而如果向左滑动，露出的4比3大，那么只要3没完全隐匿，那么position就一直按照3算。】
		 * positionOffset 当前页面偏移的百分比【positionOffset是当前Item较大的那个View占视图的百分比，0-1，没有负数！当滑动结束时，onPageScrolled();最后一次调用，positionOffset为0。】
		 * positionOffsetPixels 当前页面偏移的像素位置
		 */
		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

			//这里获取第一个值，用来判断是左滑还是右滑（如果viewpager的宽度 - positionOffsetPixels <= viewpager的宽度的一半，则代表往左滑动（上一页），反之右滑（下一页））
            /*int positionOffsetPixelsTemp =  - positionOffsetPixels;//获取需要偏移的数值（默认获取的是往右滑（下一页）的情况下的偏移值）
            int viewPagerWidth = mViewPager.getWidth();
            if(viewPagerWidth - positionOffsetPixels <= viewPagerWidth / 2){
                positionOffsetPixelsTemp = viewPagerWidth - positionOffsetPixels;
            }*/


            /*
             * 当第一页上翻页时：position = 0；currentItemIndex=0；positionOffset=0.0；positionOffsetPixels=0
             * 普通上翻页时：position = 0；currentItemIndex=1-->0；positionOffset==0.99224806-->0.0；positionOffsetPixels=1024-->0
             * 当最后一页下翻页时：position = 10；currentItemIndex=10；positionOffset=0.0；positionOffsetPixels=0
             * 普通下翻页时：position = 9；currentItemIndex=9-->10；positionOffset=0.041666985-->0.9-->0.0；positionOffsetPixels=43-->1024-->0
             * */

			if(! isNormalChange){

				if(position == mPictureBeanList.size() - 1 && position ==currentItemIndex && positionOffset == 0.0 && positionOffsetPixels == 0){
					//在最后一页进行滑动
				}else if(position == 0 && position ==currentItemIndex && positionOffset == 0.0 && positionOffsetPixels == 0){
					//在第一页进行滑动
				}else {
					//判断是否数据（例如，标题）发生了改变，如果发生了改变，那么弹出对话框
					PictureBean picBean = mPictureBeanList.get(currentItemIndex);
					String oldPicTitle = picBean.getPicTitle();//旧的图片标题
					String newPicTitle = mPicTitleEdt.getText().toString();//新的图片标题

					boolean changed = newPicTitle.equals(oldPicTitle);

					if (changed) {
						isNormalChange = true;//【如果数据没有发生改变，则代表是正常滑动】
					}else{
						mViewPager.setIsCanScroll(false);//禁止滑动
						//弹出提示对话框
						// 创建构建器
						AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
						// 设置参数
						builder.setTitle("提示")
								.setMessage("标题发生了改变，是否保存？")
								.setPositiveButton("保存", new DialogInterface.OnClickListener() {// 积极
									@Override
									public void onClick(DialogInterface dialog, int which) {
										// TODO Auto-generated method stub
										mViewPager.setIsCanScroll(true);//允许滑动
										mViewPager.scrollTo(resetXPoint,0);
										uploadImgTitle();
									}
								}).setNegativeButton("不保存", new DialogInterface.OnClickListener() {// 消极
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								mViewPager.setIsCanScroll(true);//允许滑动
								mViewPager.scrollTo(resetXPoint,0);
								//还原标题
								PictureBean pictureBean = mPictureBeanList.get(currentItemIndex);
								mPicTitleEdt.setText(pictureBean.getPicTitle());
							}
						}).setOnDismissListener(new DialogInterface.OnDismissListener() {
							@Override
							public void onDismiss(DialogInterface dialogInterface) {
								mViewPager.setIsCanScroll(true);//允许滑动
								mViewPager.scrollTo(resetXPoint,0);
							}
						});
						builder.create().show();
					}
				}
			}
		}

		/* 这个方法有一个参数position，代表哪个页面被选中。
		 * 当用手指滑动翻页的时候，如果翻动成功了（滑动的距离够长），手指抬起来就会立即执行这个方法
		 * position就是当前滑动到的页面。
		 * 如果直接setCurrentItem翻页，那position就和setCurrentItem的参数一致，这种情况在onPageScrolled执行方法前就会立即执行。
		 */
		@Override
		public void onPageSelected(int position) {
			Log.w("why", "{MyOnPageChangeListener}{onPageSelected}position="+position);
			Log.w("why", "{MyOnPageChangeListener}{onPageSelected}currentItemIndex="+currentItemIndex);
			resetXPoint = resetXPoint + (position - currentItemIndex) * mViewPager.getWidth();
			currentItemIndex = position;
			showPageNum();//设置页码
		}
	}

	//更新图片标题
	private void uploadImgTitle(){
		PictureBean picBean = mPictureBeanList.get(currentItemIndex);
		picBean.setPicTitle(mPicTitleEdt.getText().toString());
	}
}
