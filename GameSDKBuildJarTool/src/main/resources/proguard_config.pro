
-dontskipnonpubliclibraryclassmembers
-dontshrink
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*,!code/allocation/variable
-dontusemixedcaseclassnames
-keepattributes SourceFile,LineNumberTable,*Annotation*,Signature,Deprecated,InnerClasses
-keepparameternames
-renamesourcefileattribute SourceFile
-verbose

-dontwarn android.support.v4.**

# Keep names - Native method names. Keep all native class/method names.
-keepclasseswithmembers,allowshrinking class * {
    native <methods>;
}

# Also keep - Serialization code. Keep all fields and methods that are used for
# serialization.
-keepclassmembers class * extends java.io.Serializable {
    static final long serialVersionUID;
    static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

#保持 Parcelable 不被混淆
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}


# 四大组件
-keep class * extends android.app.Activity {
    public protected <fields>;
    <methods>;
}
-keep class * extends android.app.Application {
    public protected <fields>;
    <methods>;
}
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver

# 对外接口不参与混淆
-keep class com.bzai.gamesdk.GameInfoSetting{ *; }
-keep class com.bzai.gamesdk.api.** { *; }
-keep class com.bzai.gamesdk.bean.** { *; }
-keep class com.bzai.gamesdk.listener.** { *; }
# SDK基础不混淆类
-keep class com.bzai.gamesdk.common.utils_base.proguard.** { *; }
-keep class * extends com.bzai.gamesdk.common.utils_base.proguard.** { *; }

