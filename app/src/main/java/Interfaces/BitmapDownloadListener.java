package Interfaces;

import android.graphics.Bitmap;

public interface BitmapDownloadListener {

	void onLoadBitmap(String url, Bitmap bm);
	// void onLoadBitmapFailed(String url, Bitmap defaultBm);
}