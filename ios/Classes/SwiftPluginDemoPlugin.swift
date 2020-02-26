import Flutter
import UIKit

public class SwiftPluginDemoPlugin: NSObject, FlutterPlugin ,FlutterStreamHandler {
    public func onListen(withArguments arguments: Any?, eventSink events: @escaping FlutterEventSink) -> FlutterError? {
        
        // 获取EventChannel传入的参数
        let args = arguments as! [String:Any]
        print(args)// args["msg"]
        
        
        _eventSink = events
        return nil
    }
    
    public func onCancel(withArguments arguments: Any?) -> FlutterError? {
        _eventSink = nil
        return nil
    }
    
    private var _eventSink: FlutterEventSink?
    
    public static func register(with registrar: FlutterPluginRegistrar) {
        let channel = FlutterMethodChannel(name: "plugin_demo", binaryMessenger: registrar.messenger())
        
        let instance = SwiftPluginDemoPlugin()
        registrar.addMethodCallDelegate(instance, channel: channel)
        
        
        // 注册eventChannel
        let eventChannel = FlutterEventChannel(name:"plugin_event_demo",binaryMessenger: registrar.messenger())
        eventChannel.setStreamHandler(instance)
    }
    
    public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
        // 根据MethodChannel指定的方法名匹配
        if(call.method == "getPlatformVersion"){
            
            // 获取MethodChannel传入的参数
            let args = call.arguments as! [String:Any]
            _eventSink?(["argInt":args["arg"],"argBool":true ,"argString":"str","msg":"IOS -》 Flutter by methodChannel"])
            
            // 返回集合
            result(["argInt":777,"argBool":true ,"argString":"str","msg":"IOS -》 Flutter by methodChannel"])
            
            // 通过eventChannel延时返回数据
            for i in 1...4{
                sleep(1)
                _eventSink?(["argInt":i,"argBool":true ,"argString":"str","msg":"IOS -》 Flutter by eventChannel"])
            }
            
        }
    }
}
