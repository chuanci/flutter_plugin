package cc.chuanci.plugin_demo

import androidx.annotation.NonNull;
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.EventChannel.StreamHandler
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar
import kotlin.concurrent.thread

/** PluginDemoPlugin */
public class PluginDemoPlugin : FlutterPlugin, MethodCallHandler, StreamHandler {


    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        val channel = MethodChannel(flutterPluginBinding.getFlutterEngine().getDartExecutor(), "plugin_demo")
        channel.setMethodCallHandler(PluginDemoPlugin());

        val eventChannel = EventChannel(flutterPluginBinding.getFlutterEngine().getDartExecutor(), "plugin_event_demo")
        eventChannel.setStreamHandler(PluginDemoPlugin())
    }

    // This static function is optional and equivalent to onAttachedToEngine. It supports the old
    // pre-Flutter-1.12 Android projects. You are encouraged to continue supporting
    // plugin registration via this function while apps migrate to use the new Android APIs
    // post-flutter-1.12 via https://flutter.dev/go/android-project-migration.
    //
    // It is encouraged to share logic between onAttachedToEngine and registerWith to keep
    // them functionally equivalent. Only one of onAttachedToEngine or registerWith will be called
    // depending on the user's project. onAttachedToEngine or registerWith must both be defined
    // in the same class.
    companion object {
        var eventSink: EventChannel.EventSink? = null

        @JvmStatic
        fun registerWith(registrar: Registrar) {
            val channel = MethodChannel(registrar.messenger(), "plugin_demo")
            channel.setMethodCallHandler(PluginDemoPlugin())

            val eventChannel = EventChannel(registrar.messenger(), "plugin_event_demo")
            eventChannel.setStreamHandler(PluginDemoPlugin())
        }
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        // 根据MethodChannel指定的方法名匹配
        if (call.method == "getPlatformVersion") {
            // 获取MethodChannel传入的参数
            val args = call.arguments as Map<*, *>
            println(args) // args["msg"]

            // MethodChannel返回
            result.success(hashMapOf("argInt" to 777, "argBool" to true, "argString" to "str","msg" to "Android -》 Flutter by methodChannel"))

            // EventChannel返回
            for (i in 1..4) {
                Thread.sleep(1000)
                eventSink?.success(hashMapOf("argInt" to i, "argBool" to true, "argString" to "str","msg" to "Android -》 Flutter by eventChannel"))
            }
        } else {
            result.notImplemented()
        }
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    }

    override fun onListen(arguments: Any?, events: EventChannel.EventSink?) {
        // 获取EventChannel传入的参数
        val args = arguments as Map<*, *>
        println(args) //args["msg"]

        eventSink = events
    }

    override fun onCancel(arguments: Any?) {
        eventSink = null
    }
}
