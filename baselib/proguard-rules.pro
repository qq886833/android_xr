# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile


##################### common ######################
#如果引用了v4或者v7包
-dontwarn android.support.**

-keep class * implements java.lang.annotation.Annotation {*;}

#忽略警告
-ignorewarnings

#这句能解决List<T>在混淆后变成List的问题
-keepattributes Deprecated

##################### 标识不用混淆的部分 ######################
# keep annotated by NotProguard
-keep @com.bsoft.baselib.util.NotProguard class * {*;}
-keep class * {
@com.yjhealth.libs.corelib.utils.NotProguard <fields>;
}
-keepclassmembers class * {
@com.yjhealth.libs.corelib.utils.NotProguard <methods>;
}

##################### bean ######################
-keepclasseswithmembers class * extends java.io.Serializable{
    <fields>;
    <methods>;
}

##################### Glide ######################
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

##################### Retrofit ######################
-dontnote retrofit2.Platform
-dontnote retrofit2.Platform$IOS$MainThreadExecutor
-dontwarn retrofit2.Platform$Java8
-keepattributes Signature
-keepattributes Exceptions
-dontwarn okio.**


##################### Rxjava ######################
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}
-dontnote rx.internal.util.PlatformDependent


##################### EventBus ######################
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }


