import 'dart:async';

import 'package:flutter/services.dart';

class PluginDemo {
  // 构造函数， 使用EventChannel时， 需要先开启监听
  PluginDemo() {
    subscription = EventChannel("plugin_event_demo").receiveBroadcastStream({
      "msg": "Flutter -> Platform  by eventChannel",
    }).listen((event) {
      print(event); //event["msg"]
    }, onError: (e) {
      print("eventChannel onError, e:  $e");
    });
  }

  StreamSubscription<dynamic> subscription;

  static const MethodChannel _channel = const MethodChannel('plugin_demo');

  static Future<String> get platformVersion async {
    var res = await _channel.invokeMethod('getPlatformVersion', {"msg": "Flutter -> Platform  by methodChannel"});
    print(res); //res["msg"]

    return res["argString"];
  }
}
