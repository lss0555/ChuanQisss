# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
#指定代码的压缩级别
-optimizationpasses 5
#包明不混合大小写
-dontusemixedcaseclassnames
#不去忽略非公共的库类
-dontskipnonpubliclibraryclasses
 #优化  不优化输入的类文件
-dontoptimize
 #预校验
-dontpreverify
 #混淆时是否记录日志
-verbose
 # 混淆时所采用的算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

# 保持哪些类不被混淆
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
#如果有引用v4包可以添加下面这行
-keep public class * extends android.support.v4.app.Fragment
#忽略警告
-ignorewarning
##记录生成的日志数据,gradle build时在本项目根目录输出##
#apk 包内所有 class 的内部结构
-dump proguard/class_files.txt
#未混淆的类和成员
-printseeds proguard/seeds.txt
#列出从 apk 中删除的代码
-printusage proguard/unused.txt
#混淆前后的映射
-printmapping proguard/mapping.txt
########记录生成的日志数据，gradle build时 在本项目根目录输出-end######

#如果引用了v4或者v7包
-dontwarn android.support.**

####混淆保护自己项目的部分代码以及引用的第三方jar包library-end####

#保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}
#保持自定义控件类不被混淆
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}

#保持 Parcelable 不被混淆
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

#保持 Serializable 不被混淆
-keepnames class * implements java.io.Serializable

#保持 Serializable 不被混淆并且enum 类也不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

#保持枚举 enum 类不被混淆
-keepclassmembers enum * {
  public static **[] values();
  public static ** valueOf(java.lang.String);
}

-keepclassmembers class * {
    public void *ButtonClicked(android.view.View);
}

#不混淆资源类
-keepclassmembers class **.R$* {
    public static <fields>;
}


# 不混淆getset及字段
-keepclassmembers class com.chuanqi.yz.model.* {
	!final <fields>;
	void set*(***);
	*** get*();
	*** is*();
}
#避免混淆泛型 如果混淆报错建议关掉
#-keepattributes Signature
#移除Log类打印各个等级日志的代码，打正式包的时候可以做为禁log使用，这里可以作为禁止log打印的功能使用，另外的一种实现方案是通过BuildConfig.DEBUG的变量来控制
#-assumenosideeffects class android.util.Log {
#    public static *** v(...);
#    public static *** i(...);
#    public static *** d(...);
#    public static *** w(...);
#    public static *** e(...);
#}
#混淆前后的映射
-printmapping mapping.txt
#未混淆的类和成员
-printseeds seeds.txt
#列出从 apk 中删除的代码
-printusage unused.txt
#############################################################################################
########################                 以上通用           ##################################
-dontwarn com.autonavi.**
-keep class com.autonavi.** { *; }
-dontwarn org.simalliance.**
-keep class org.simalliance.** { *; }
-dontwarn sun.net.**
-keep class sun.net.** { *; }
-dontwarn java.nio.**
-keep class java.nio.** { *; }
-dontwarn org.codehaus.**
-keep class org.codehaus.** { *; }
-dontwarn  org.ietf.**
-keep class  org.ietf.** { *; }
-dontwarn android.util.**
-keep class android.util.** { *; }
-dontwarn android.net.**
-keep class android.net.** { *; }
-dontwarn android.os.**
-keep class android.os.** { *; }
-dontwarn android.support.**
-keep class android.support.** { *; }
-dontwarn org.jivesoftware.**
-keep class org.jivesoftware.** { *; }
-dontwarn com.mob.**
-keep class com.mob.** { *; }
-dontwarn com.google.**
-keep class com.google.** { *; }
-dontwarn com.android.**
-keep class com.android.** { *; }
-dontwarn de.measite.**
-keep class de.measite.** { *; }
-dontwarn uk.co.**
-keep class uk.co.** { *; }
-dontwarn com.antgroup.**
-keep class com.antgroup.** { *; }
-dontwarn com.alipayzhima.**
-keep class com.alipayzhima.** { *; }
-dontwarn libcore.icu.**
-keep class libcore.icu.** { *; }
-dontwarn android.graphics.**
-keep class android.graphics.** { *; }
-dontwarn android.app.**
-keep class android.app.** { *; }
-dontwarn android.telephony.**
-keep class android.telephony.** { *; }
-dontwarn sun.security.**
-keep class sun.security.** { *; }
-dontwarn com.alipay.**
-keep class com.alipay.** { *; }
-dontwarn com.ut.**
-keep class com.ut.** { *; }
-dontwarn org.json.**
-keep class org.json.** { *; }
-dontwarn com.tencent.**
-keep class com.tencent.** { *; }
-dontwarn cn.sharesdk.**
-keep class cn.sharesdk.** { *; }

#okhttp3
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *;}
-keep class okhttp3.** { *;}
-keep class okio.** { *;}
-dontwarn sun.security.**
-keep class sun.security.** { *;}
-dontwarn okio.**
-dontwarn okhttp3.**

##gson
#-keep class com.google.gson.** {*;}
##-keep class com.google.**{*;}
#-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }
##-keep class com.google.gson.examples.android.model.** { *; }
#-keep class com.google.** {
#    <fields>;
#    <methods>;
#}
##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature
# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }
# Application classes that will be serialized/deserialized over Gson
#-keep class com.google.gson.examples.android.model.** { *; }  ##这里需要改成解析到哪个  javabean
#过滤掉自己编写的实体类
-keep class com.chuanqi.yz.model.**{*;}
##---------------End: proguard configuration for Gson  ----------
 #定位
  -keep class com.amap.api.location.**{*;}
  -keep class com.amap.api.fence.**{*;}
  -keep class com.autonavi.aps.amapapi.model.**{*;}