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

# 不被混淆

# 保留实现Parcelable的类
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# 保留序列化类
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# 保留注解
-keepattributes *Annotation*

# 保留Native方法
-keepclasseswithmembernames class * {
    native <methods>;
}
# 保留资源（如有需要）
-keepclassmembers class **.R$* {
    public static <fields>;
}

# 保留自定义View的构造函数
-keepclassmembers public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keep public class com.cx.shopify.util.ShopifyUtil {
    public <methods>;
}

-keep public class com.cx.shopify.view.ShopifyListView {
    public <methods>;
}

-keep public class com.cx.shopify.api.Shopify {
    public <methods>;
}
-keep public class com.cx.shopify.fragment.H5Fragment {
    public <methods>;
}

-keep public class com.cx.shopify.listener.** {
    public *;
}

