# Instacapture 2.0 [![Release](https://jitpack.io/v/tarek360/instacapture.svg)](https://jitpack.io/#tarek360/instacapture)

Android library to capture screenshot from your app


### Features
- Capture all the contents of the screen, includes:
   - Google Maps ([MapView](https://developers.google.com/android/reference/com/google/android/gms/maps/MapView), [SupportMapFragment](https://developers.google.com/android/reference/com/google/android/gms/maps/SupportMapFragment)) 
   - Dialogs, context menus, toasts
   - TextureView
   - GLSurfaceView

- Set a specific view(s) to prevent it from capturing.
- No permissions are required.


### Installation

Add this to your module `build.gradle` file:
```gradle
dependencies {
	...
	 compile "com.github.tarek360:instacapture:2.0.1"
}
```

Add this to your root `build.gradle` file (**not** your module `build.gradle` file) :
```gradle
allprojects {
	repositories {
		...
		maven { url "https://jitpack.io" }
	}
}
```


### How to use Instacapture ?

#### Kotlin

```kotlin
Instacapture.capture(this, object : SimpleScreenCapturingListener() {
    override fun onCaptureComplete(bitmap: Bitmap) {
       //Your code here..

    }
})

// or in Rx way
Instacapture.captureRx(this).subscribe { bitmap ->
   //Your code here..
}
```

#### Java

```java
Instacapture.INSTANCE.capture(activity, new SimpleScreenCapturingListener() {
    @Override
    public void onCaptureComplete(Bitmap bitmap) {
       //Your code here..
    }
}, ignoredViews);

// or in Rx way
Instacapture.INSTANCE.captureRx(this, ignoredViews).subscribe(new Action1<Bitmap>() {
    @Override
    public void call(Bitmap bitmap) {
        //Your code here..
     }
});

```
    
### How to ignore view(s) from the screenshot?

#### Kotlin

```kotlin
Instacapture.capture(.., .., ignoredViews)
//or
Instacapture.captureRx(.., ignoredViews)
```
#### Java

```java
Instacapture.INSTANCE.capture(.., .., ignoredViews);
//or
Instacapture.INSTANCE.captureRx(.., ignoredViews);
```


- To enable Instacapture logging.

#### Kotlin

```kotlin
Instacapture.enableLogging(true);
```
#### Java

```java
Instacapture.INSTANCE.enableLogging(true);
```


## License

>Copyright 2017 Tarek360

>Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

>   http://www.apache.org/licenses/LICENSE-2.0

>Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
