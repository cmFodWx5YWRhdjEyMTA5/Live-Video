package com.live.FreeVideo;

import android.graphics.Bitmap;

public interface AsyncResponse {
	void processFinish(String output);
	void processBitmapFinish(Bitmap output);
	void processMessageFinish(String output);
}
