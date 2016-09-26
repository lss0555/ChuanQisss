package Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.util.ArrayList;


import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chuanqi.yz.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import Dialogs.SelectPicPop;
import Utis.SharePre;
import Utis.UILUtils;
import Utis.Utis;
import model.Photos;
/**
 * A simple {@link Fragment} subclass.
 *
 */
@SuppressLint("ValidFragment")
public class ImgShowFragment extends BaseFragment {
	private String savePath = Utis.getAppFolder();
	private ImageView mImgShow;
	private ImageView mImgShow1;
	private ArrayList<Photos> mDate;
	private int positon;
	private TextView mTvYqm;
	private Bitmap tvBitmap;

	public ImgShowFragment() {
	}

	public ImgShowFragment(int positon, ArrayList<Photos> mDate) {
		this.positon = positon;
		this.mDate = mDate;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.activity_img_show, null);
		initview(layout);
		return layout;
	}

	private void initview(View layout) {
		mTvYqm = (TextView) layout.findViewById(R.id.tv_yqm);
		mTvYqm.setText("我的邀请码:" + SharePre.getUserId(getActivity()));
		mImgShow = (ImageView) layout.findViewById(R.id.img_show);
		mImgShow1 = (ImageView) layout.findViewById(R.id.img_show);
//		drawText2Pic(mImgShow,"我的邀请码:"+SharePre.getUserId(getActivity()),mDate.get(positon).getImgUrl());
		UILUtils.displayImageNoAnim(mDate.get(positon).getImgUrl(), mImgShow);
		mImgShow.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View view) {
				SelectPicPop selectPicPop = new SelectPicPop(getActivity());
				selectPicPop.showAtLocation(view, Gravity.BOTTOM, 0, 0);
				selectPicPop.setOnSelectPicListner(new SelectPicPop.OnSelecListner() {
					@Override
					public void onSelect(String Type) {
						switch (Type) {
							case "Save":
								Bitmap bitmap = ((BitmapDrawable) mImgShow.getDrawable()).getBitmap();
								mImgShow.setDrawingCacheEnabled(true);
								Bitmap drawingCache = mImgShow.getDrawingCache();
								saveImgs(drawingCache);
								mImgShow.setDrawingCacheEnabled(false);
								break;
						}
					}
				});
				return false;
			}
		});
	}

	/**
	 * 保存图片
	 *
	 * @param bigImg
	 */
	private void saveImgs(Bitmap bigImg) {
		if (Utis.getSDPath() == null) {
			Toast.makeText(getActivity(), "没有内存卡", Toast.LENGTH_SHORT).show();
			return;
		}
		File file = new File(savePath);
		if (!file.exists()) {
			file.mkdir();
		}
		File imageFile = new File(file, System.currentTimeMillis() + ".jpg");
		try {
			imageFile.getParentFile().mkdirs();
			imageFile.createNewFile();
			FileOutputStream fos = new FileOutputStream(imageFile);
			bigImg.compress(Bitmap.CompressFormat.PNG, 100, fos);
			fos.flush();
			fos.close();
			Toast.makeText(getActivity(), "图片已保存到本地文件夹" + savePath, Toast.LENGTH_LONG).show();
			MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bigImg, "title", "description");
			Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
			Uri uri = Uri.fromFile(file);
			intent.setData(uri);
			getActivity().sendBroadcast(intent);

		} catch (FileNotFoundException e) {
			Toast.makeText(getActivity(), "图片保存失败", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		} catch (IOException e) {
			Toast.makeText(getActivity(), "图片保存失败", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}
	/**
	 * 在图片上面写字
	 *
	 * @param imageView
	 * @param str
	 */
	private void drawText2Pic(ImageView imageView, String str, String imgurl) {
//		Bitmap photo = BitmapFactory.decodeResource(this.getResources(),
//				R.mipmap.sunqun);                             发
//		Bitmap photo = getBitmap(mDate.get(positon).getImgUrl());
		UILUtils.displayImageNoAnim(mDate.get(positon).getImgUrl(),mImgShow1);
		Bitmap photo = ((BitmapDrawable) mImgShow1.getDrawable()).getBitmap();
//		Bitmap photo = getbitmap(imgurl);
		int width = photo.getWidth();
		int hight = photo.getHeight();
		System.out.println("宽" + width + "高" + hight);
		Bitmap icon = Bitmap
				.createBitmap(width, hight, Bitmap.Config.ARGB_4444); // 建立一个空的BItMap

		Canvas canvas = new Canvas(icon);// 初始化画布绘制的图像到icon上
		Paint photoPaint = new Paint(); // 建立画笔
		photoPaint.setDither(true); // 获取跟清晰的图像采样
		photoPaint.setFilterBitmap(true);// 过滤一些

		Rect src = new Rect(0, 0, photo.getWidth(), photo.getHeight());// 创建一个指定的新矩形的坐标
		Rect dst = new Rect(0, 0, width, hight);// 创建一个指定的新矩形的坐标
		canvas.drawBitmap(photo, src, dst, photoPaint);// 将photo 缩放或则扩大到
		// dst使用的填充区photoPaint
		Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG
				| Paint.DEV_KERN_TEXT_FLAG);// 设置画笔
		textPaint.setTextSize(55);// 字体大小
		textPaint.setTypeface(Typeface.DEFAULT_BOLD);// 采用默认的宽度
		textPaint.setColor(Color.BLACK);// 采用的颜色
		canvas.drawText(str, 50, 1100, textPaint);// 绘制上去字，开始未知x,y采用那只笔绘制
		canvas.save(Canvas.ALL_SAVE_FLAG);
		canvas.restore();
		imageView.setImageBitmap(icon);
//        saveMyBitmap(icon,"test");
	}
}
