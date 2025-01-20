
-keep class android.** { *; }
-keep interface android.** { *; }


-keep class java.net.** { *; }
-keep class org.xmlpull.v1.** { *; }

-keep class com.coara.smilezemidownloader.MainActivity { *; }

-keepclassmembers class com.coara.smilezemidownloader.MainActivity {
    public void onCreate(android.os.Bundle);
    public void onRequestPermissionsResult(int, java.lang.String[], int[]);
}

-keep class android.content.Intent { *; }
-keep class android.net.Uri { *; }

-keep class java.util.ArrayList { *; }
-keep class java.lang.String { *; }

-keep class android.Manifest$permission { *; }
-keep class * extends java.lang.Exception { *; }

-keepclassmembers class org.xmlpull.v1.XmlPullParser {
    public int getEventType();
    public String getName();
    public String getText();
    public int next();
}


-keep class android.widget.Toast { *; }


-keep class java.net.HttpURLConnection { *; }

-keep class android.widget.Spinner { *; }
-keep class android.widget.ListView { *; }
-keep class android.widget.TextView { *; }
-keep class android.widget.Button { *; }

-dontwarn java.lang.**
-dontwarn android.**

-keepclassmembers class * {
    public void set*(...);
    public void get*(...);
    void lambda*(...);
}

-keepattributes Exceptions, InnerClasses, Signature, Deprecated, EnclosingMethod

-adaptresourcefilenames **.xml
-adaptresourcefilenames **.png

-optimizationpasses 3
-mergeinterfacesaggressively
-dontshrink
-dontoptimize

-repackageclasses ''
-classobfuscationdictionary obfuscation-dictionary.txt
-renamesourcefileattribute SourceFile

-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
    public static *** w(...);
    public static *** e(...);
}

-keep class android.app.Activity { *; }
-keep class android.content.pm.PackageManager { *; }

-dontpreverify
-allowaccessmodification
