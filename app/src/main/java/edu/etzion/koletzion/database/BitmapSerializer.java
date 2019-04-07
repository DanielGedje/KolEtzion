package edu.etzion.koletzion.database;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

public class BitmapSerializer {
	public static Bitmap decodeStringToBitmap(String encodedBitmap) {
		byte[] decodedBytes = Base64.decode(encodedBitmap, 0);
		return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
	}
	
	public static String encodeBitmapToString(Bitmap bitmap) {
		ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
		float i = 50;
		bitmap.compress(Bitmap.CompressFormat.JPEG, (int)i, byteArrayOS);
		for (int j = 1; j < 10; j++) {
			if (bitmap.getAllocationByteCount() == 700000) {
				bitmap.compress(Bitmap.CompressFormat.JPEG, (int) Math.ceil(i), byteArrayOS);
				System.out.println("equals");
			} else if (bitmap.getAllocationByteCount() > 700000) {
				bitmap.compress(Bitmap.CompressFormat.JPEG, (int) Math.ceil(i /= (2f * j)), byteArrayOS);
				System.out.println("large");
			} else if (bitmap.getAllocationByteCount() < 700000) {
				bitmap.compress(Bitmap.CompressFormat.JPEG, (int) Math.ceil(i *= (2f / j)), byteArrayOS);
				System.out.println("small");
			}
			System.out.println(bitmap.getAllocationByteCount());
		}
		return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
	}
	
	public static Bitmap getBitmapFromImageView(ImageView imageView) {
		return ((BitmapDrawable) imageView.getDrawable()).getBitmap();
	}
}
