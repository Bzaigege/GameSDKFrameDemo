
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





