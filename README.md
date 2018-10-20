# FlowerStory

[![Download](https://img.shields.io/badge/Download-v1.0.0-ff8080.svg)](https://github.com/challvy/FlowerStory/raw/master/README_RES/app.apk)

## Introduce

Flower Story is an Android App project that provides flower recognition service.

## Environment

### Gradle

```java
gradle: 3.1.4
distributionUrl=http\://services.gradle.org/distributions/gradle-4.4-all.zip
```

### Android SDK

```java
compileSdkVersion: 27
buildToolsVersion '27.0.3'
minSdkVersion 21
targetSdkVersion 27
```

### Dependencies

```java
implementation 'com.android.support:appcompat-v7:27.1.1'
implementation 'com.android.support:design:27.1.1'
implementation 'com.android.support:cardview-v7:27.1.1'
implementation 'com.squareup.okhttp3:okhttp:3.10.0'
```

### Android Version of User's Phone

```java
Android Version >= 6.0.1
```

## Design

### Camera Module

* Use [android.hardware.camera2](https://developer.android.com/reference/android/hardware/camera2/package-summary.html) API for this applications. Please see this [demo](https://github.com/googlesamples/android-Camera2Basic).

* About android.hardware.camera2

![android.hardware.camera2](E:\git\FlowerStory\README_RES\android.hardware.camera2.png)

* Flowchart of Camera2

![CameraFlowchart](E:\git\FlowerStory\README_RES\CameraFlowchart.png)

* Set Brightness

```java
public void setBrightness(double value) {
	minCompensationRange = range.getLower();
	maxCompensationRange = range.getUpper();
	int brightness = (int) (minCompensationRange + (maxCompensationRange - minCompensationRange)*(value/100.0f));
	mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_EXPOSURE_COMPENSATION, brightness);
	mCameraCaptureSession.setRepeatingRequest(mPreviewRequestBuilder.build(), mCaptureCallback, mBackgroundHandler);
}
```

* Zoom

```java
Set mTextureView.setOnTouchListener(textTureOntuchListener);
Override the onTouch() function;
PreviewRequestBuilder.set(CaptureRequest.SCALER_CROP_REGION,Rect);
```

### Network Module

* Get

```java
import okhttp3.*;
import org.json.*;
Request request = new Request.Builder()
.url(URL)
.build();
Call call = new OkHttpClient().newCall(request);
call.enqueue(new Callback() {
@Override
    public void onFailure(Call arg0, IOException e) {…}
    @Override
    public void onResponse(Call call, Response response) throws IOException {
        String jsonData = response.body().string();
Message.obtain(mUIHandler,GET_FROM_URL,jsonData).sendToTarget();
    }
});
```

* Post

```java
import okhttp3.*;
import org.json.*;
OkHttpClient mOkHttpClient = new OkHttpClient();
RequestBody fileBody = RequestBody.create(TYPE, mFile);
    RequestBody requestBody = new MultipartBody.Builder()
        .setType(MultipartBody.FORM)
        .addFormDataPart(KEY_PARA, VALUE_PARA)
        .addFormDataPart(KEY_FILE, VALUE_FILENAME, fileBody)
        .build();
Request request = new Request.Builder()
.url(URL)
.post(requestBody)
    .build();
Call call = mOkHttpClient.newCall(request);
call.enqueue(new Callback() {
@Override
    public void onFailure(Call arg0, IOException e) {…}
    @Override
public void onResponse(Call call, Response response) throws IOException {
Message.obtain(mUIHandler,MESSAGE_URL,response.body().string()).sendToTarget();
}
});
```

* Lazy Load

```java
public class ItemFragment extends Fragment {
    private boolean isFragmentVisible;
    private boolean isFirstVisible;
    private View rootView;
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (rootView == null) return;
        if (isFirstVisible && isVisibleToUser) {
            onFragmentFirstVisible();
            isFirstVisible = false;
        }
        if (isVisibleToUser) {
            onFragmentVisibleChange(true);
            isFragmentVisible = true;
            return;
        }
        if (isFragmentVisible()) {
            isFragmentVisible = false;
            onFragmentVisibleChange(false);
        }
	}
}
```

* Pagination

```java
mRecyclerView.addOnScrollListener(new RecyclerView .OnScrollListener(){
	@Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
super.onScrollStateChanged(recyclerView, newState);
        if (newState==RecyclerView.SCROLL_STATE_IDLE) {
            if(!mAdapter.isFadeTips()&&lastVisibleItem+1== mAdapter.getItemCount()) {
				updateRecyclerView(mAdapter.getRealLastPosition(), mAdapter.getRealLastPosition() + PAGE_COUNT);
            }
            if (mAdapter.isFadeTips() && lastVisibleItem + 2 == mAdapter.getItemCount()) {
                updateRecyclerView(mAdapter.getRealLastPosition(), mAdapter.getRealLastPosition()+PAGE_COUNT);
            }
        }
    }
    
    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
super.onScrolled(recyclerView, dx, dy);
        lastVisibleItem= mLayoutManager.findLastVisibleItemPosition();
    }
});
```

## Reference

- Wikipedia Data

  [百度百科](https://baike.baidu.com/)

- Logo

  [花瓣](http://huaban.com/)

## Demo

<div align=center>
<img src="https://github.com/challvy/FlowerStory/raw/master/README_RES/app.gif" width="40%"/> 
</div>

## End

If you have any problem, please contact challvy.tee@gmail.com.
