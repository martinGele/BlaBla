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
-renamesourcefileattribute SourceFile

-optimizations !code/simplification/cast,!field/*,!class/merging/*,!code/allocation/variable,!method/marking/private, class/merging/vertical, class/merging/horizontal
-optimizationpasses 5
-allowaccessmodification
-dontpreverify
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-verbose

-dontwarn org.xbill.DNS.**
-dontwarn org.apache.cordova.**
-dontwarn com.google.common.primitives.**
-dontwarn javax.annotation.**

-dontwarn android.support.**
-dontwarn android.app.**
-dontwarn android.view.**
-dontwarn android.widget.**

-dontwarn **CompatHoneycomb
-dontwarn **CompatHoneycombMR2
-dontwarn **CompatCreatorHoneycombMR2

-keep class com.facebook.** { *; }
-keepattributes Signature
-keepattributes Exceptions, Signature, InnerClasses

-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

-keep class com.ak.app.cb.util.** { *;}
-keepclassmembers class com.ak.app.cb.model.** { *; }

-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.app.Fragment

-keep public class * extends android.content.BroadcastReceiver
-keepattributes *Annotation*
-renamesourcefileattribute SourceFile
-keep public class com.google.vending.licensing.ILicensingService
-keep public class com.android.vending.licensing.ILicensingService

# For native methods, see http://proguard.sourceforge.net/manual/examples.html#native
-keepclasseswithmembernames class * {
    native <methods>;
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keep public class com.ak.app.cb.activity.MainActivity
-keepclassmembers class com.ak.app.cb.activity.MainActivity { *; }

-dontwarn org.jivesoftware.**

# Preserve the serializable classes
-keep class * implements java.io.Serializable
-keepclassmembers class * implements java.io.Serializable {*;}

# keep setters in Views so that animations can still work.
# see http://proguard.sourceforge.net/manual/examples.html#beans
-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}

# We want to keep methods in Activity that could be used in the XML attribute onClick
#-keepclassmembers class * extends android.app.Activity {
#   public void *(android.view.View);
#}

# For enumeration classes, see http://proguard.sourceforge.net/manual/examples.html#enumerations
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}


-keepclassmembers class **.R$* {
    public static <fields>;
}
-keep class **.R$*
-ignorewarnings

-keep class * {
    public private *;
}
