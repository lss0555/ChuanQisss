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
import com.google.zxing.WriterException;
import com.nostra13.universalimageloader.core.ImageLoader;

import Dialogs.SelectPicPop;
import Utis.SharePre;
import Utis.UILUtils;
import Utis.Utis;
import Utis.MakeQRCodeUtil;
import Views.DrawTextImageView;
import model.Photos;
/**
 * A simple {@link Fragment} subclass.
 *
 */
@SuppressLint("ValidFragment")
public class ImgShowFragment extends BaseFragment {
	private String savePath = Utis.getAppFolder();
	private DrawTextImageView mImgShow;
	private ImageView mImgShow1;
	private ArrayList<Photos> mDate;
	private int positon;
	private int imgId;
	//	private TextView mTvYqm;
	private Bitmap tvBitmap;
	private ImageView mImgQrCode;
	private Bitmap makeQRImage;
	public ImgShowFragment() {

	}
	public ImgShowFragment(int positon, int ImgId) {
		this.positon = positon;
		this.imgId = ImgId;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.activity_img_show, null);
		initview(layout);
		return layout;
	}
	private void initview(View layout) {
		mImgQrCode = (ImageView) layout.findViewById(R.id.img_qrcode);
		mImgShow = (DrawTextImageView) layout.findViewById(R.id.img_show);
		try {
			makeQRImage =MakeQRCodeUtil.makeQRImage("http://jk.qingyiyou.cn/wx/UniqueCode/invite.html?userid="+SharePre.getUserId(getActivity()), 300, 300);
//			Bitmap makeQRImage=MakeQRCodeUtil.makeQRImage(drawingCache,"http://jk.qingyiyou.cn/wx/UniqueCode/invite.html?userid=100087", 200, 200);
			mImgQrCode.setImageBitmap(makeQRImage);
		} catch (WriterException e) {
			e.printStackTrace();
		}
		mImgShow.setImageResource(imgId);
//		UILUtils.displayImageNoAnim(mDate.get(positon).getImgUrl(), mImgShow);
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
//								Bitmap bitmap = ((BitmapDrawable) mImgShow.getDrawable()).getBitmap();
								mImgShow.setDrawingCacheEnabled(true);
								Bitmap drawingCache = mImgShow.getDrawingCache();
								saveImgs(MakeQRCodeUtil.addBackground(makeQRImage,drawingCache));
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
//			Toast.makeText(getActivity(), "图片已保存到本地文件夹" + savePath, Toast.LENGTH_LONG).show();
			Toast.makeText(getActivity(), "保存成功", Toast.LENGTH_LONG).show();
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
}
