package com.jbh.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;


public class BitmapCache {
	Context mContext;
	private Bitmap mBitmap;
	private static int IMG_DATE = 7;
	private static int IMG_CNT = 100;
	private static int IMG_DEL_CNT = 20;

	public BitmapCache(Context con) {
		mContext = con;
	}

	public void SaveBitmap(String name, Bitmap bmap) {
		try {
			new Thread(new SaveBitmapThread(name, bmap)).start();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public boolean isBitmapImg(String name) {
		try {
		if (mContext.getFileStreamPath(name).exists()) {
			return true;
		}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

	public Bitmap loadBitmap(String name) {
		mBitmap = BitmapFactory.decodeFile(mContext.getFileStreamPath(name)
				.toString(), null);

		return mBitmap;
	}

	public void deleteAllBitmapFile() {
		try {
			new Thread(new DeleteBitmapThread()).start();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public void deleteBitmapFile(String fileName) {
		if (mContext.getFileStreamPath(fileName).exists()) {
			mContext.deleteFile(fileName);
		}
	}

	private class DeleteBitmapThread implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			String[] fileList = mContext.fileList();
			for (String fileName : fileList) {
				if (fileName.indexOf("jpg") != -1) {
					if (mContext.getFileStreamPath(fileName).exists()) {
						mContext.deleteFile(fileName);
					}
				}
			}
		}
	}

	public void deleteOldBitmapFile() {
		try {
			new Thread(new DeleteOldBitmapThread()).start();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	private class DeleteOldBitmapThread implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Calendar calendar = Calendar.getInstance();
			Date baseDate = null;
			try {
				calendar.add(Calendar.DATE, -IMG_DATE);
				baseDate = calendar.getTime();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			File[] arrFiles = new File(mContext.getFilesDir().toString())
					.listFiles();
			int cnt = 0;
			if (IMG_DATE == 0) {
				for (File file : arrFiles) {
					Date fileDate = new Date(file.lastModified());
					if (fileDate.before(baseDate)) {
						mContext.deleteFile(file.getName());
					}
				}
			} else if (arrFiles.length > IMG_CNT) {
				for (File file : arrFiles) {
					if (cnt > IMG_DEL_CNT) {
						return;
					}
					Date fileDate = new Date(file.lastModified());
					if (fileDate.before(baseDate)) {
						mContext.deleteFile(file.getName());
						cnt++;
					}
				}
			}

		}

	}

	private class SaveBitmapThread implements Runnable {
		String mName;
		Bitmap mBmap;

		public SaveBitmapThread(String name, Bitmap bmap) {
			// TODO Auto-generated constructor stub
			this.mName = name;
			this.mBmap = bmap;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub

			FileOutputStream fos = null;
			try {
				if (mContext.getFileStreamPath(mName).exists()) {
					mContext.deleteFile(mName);
					return;
				}
				fos = mContext.openFileOutput(mName, Context.MODE_PRIVATE);
				if (mBmap != null) {
					mBmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
				}
				fos.flush();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			} finally {
				try {
					if (fos != null)
						fos.close();
				} catch (Exception e2) {
					// TODO: handle exception
					e2.printStackTrace();
				}
			}
		}

	}

	private Bitmap scaleBitmap(Bitmap src, int w, int h) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inDither = true; // 
		opt.inPurgeable = true;// 
		opt.inSampleSize = 1;

		byte[] byteimg = BitmapToByteArray(src);
		Bitmap org = BitmapFactory.decodeByteArray(byteimg, 0, byteimg.length,
				opt);
		Bitmap resize = Bitmap.createScaledBitmap(src, w, h, true);
		src.recycle();
		return resize;

	}

	// Bitmap -> ByteArray
	private byte[] BitmapToByteArray(Bitmap bitmap) {

		ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.JPEG, 100, byteArray);
		return byteArray.toByteArray();

	}


}
