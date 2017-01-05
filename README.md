# ExPhotoPicker
[![](https://jitpack.io/v/xoptimal/ExPhotoPicker.svg)](https://jitpack.io/#xoptimal/ExPhotoPicker)


## 演示
![演示图片](https://github.com/xoptimal/ExPhotoPicker/blob/master/photopicker.gif)

<br/>

#### Step 1. 添加依赖

	allprojects {
			repositories {
				...
				maven { url 'https://jitpack.io' }
			}
		}

	dependencies {
		        compile 'com.github.xoptimal:ExPhotoPicker:Latest release'
		}

<br/>

#### Step 2. manifest配置(可选操作)

	<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
    <uses-permission android:name="android.permission.CAMERA"/>

	<application>
		
		<activity
            android:name="com.xoptimal.photopicker.view.act.PhotoExPreviewActivity"
            android:launchMode="singleTop"
            android:screenOrientation="portrait"/>

        <activity
            android:name="com.xoptimal.photopicker.view.act.PhotoPickerActivity"
            android:launchMode="singleTop"
            android:screenOrientation="portrait"/>

        <activity
            android:name="com.xoptimal.photopicker.view.act.PhotoPreviewActivity"
            android:launchMode="singleTop"
            android:screenOrientation="portrait"/>

        <activity
            android:name="com.xoptimal.photopicker.view.act.PhotoPickerListActivity"
            android:launchMode="singleTop"
            android:screenOrientation="portrait"/>

	</application>

<br/>

#### Step 3. 使用(详情参考 Demo -> MainActivity )

* 单图模式
	
		new ExPhotoPicker.Builder()
			.showSingleModel(true)
			.build().start(this, 10);

* 多图模式

		new ExPhotoPicker.Builder()
			.setMaxCount(3)
			.showPreview(true)
			.build().start(this, 10);

* 预览模式

		new ExPhotoPreView.Builder()
			.setPhotos(mPhotoPaths)
			.build().start(MainActivity.this);

<br/>

#### Step 4. 获取图片

	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            if (data != null) {
                ArrayList<String> photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
				...
            }
        }
    }


<br/>

#### Step 5. ExPhotoPicker支持参数

方法名 | 说明
----|-----
setPhotos(ArrayList<String> photos) | 设置默认选中图片
setMaxCount(int maxCount) | 设置可选图片最大数
showSingleModel(boolean showSingleModel) | 开启单图模式
showGif(boolean showGif) | 开启Gif图预览功能
showCamera(boolean showCamera) | 开启相机功能
showPreview(boolean showPreview)  | 开启列表预览功能

<br/>

#### Step 6. 扩展

框架主题色, 根据主项目 -> res -> colors -> `<color name="colorPrimary">#3F51B5</color>`

可以根据APP的主色调, 更改该颜色值, 做统一色调 (注: 如没有`colorPrimary`, 请务必加上)



## 说明

>此库基于 `donglua-PhotoPicker`,  谨此致谢 ! 

PhotoPicker 地址: [https://github.com/donglua/PhotoPicker](https://github.com/donglua/PhotoPicker) 
