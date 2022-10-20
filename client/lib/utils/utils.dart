import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'dart:math' as math;
import 'dart:ui' as ui;

import 'package:universal_html/html.dart' as html;
import 'package:universal_html/js.dart' as js;
import 'constants.dart';
import 'http_client.dart';

String formatDate(DateTime date) {
  return DateFormat.yMMMd().format(date);
}

String getHost() {
  return kDebugMode ? devHost : '';
}

typedef CallbackFunction = void Function();

void showAlert(BuildContext context, {title, CallbackFunction okCallback = null, String message}) {
  title ??= "Alert";
  showDialog<String>(context: context, builder: (BuildContext context) =>
      AlertDialog(
        title: Text(title),
        content: Text(message),
        actions: <Widget>[
          TextButton(
            onPressed: () {
              Navigator.pop(context, 'OK');
              if(okCallback != null) {
                okCallback();
              }
            },
            child: const Text('OK'),
          ),
        ],
      )
  );
}

void showConfirm(BuildContext context, {title, CallbackFunction okCallback = null, CallbackFunction cancelCallback = null, String message}) {
  title ??= "Confirm";
  showDialog<String>(context: context, builder: (BuildContext context) =>
      AlertDialog(
        title: Text(title),
        content: Text(message),
        actions: <Widget>[
          TextButton(
            onPressed: () {
              Navigator.pop(context, 'OK');
              if(okCallback != null) {
                okCallback();
              }
            },
            child: const Text('OK'),
          ),
          TextButton(
            onPressed: () {
              Navigator.pop(context, 'Cancel');
              if(cancelCallback != null) {
                cancelCallback();
              }
            },
            child: const Text('Cancel'),
          ),
        ],
      )
  );
}

void goTo(BuildContext context, Widget screen) async {
  Future.delayed(Duration.zero, () {
    Navigator.pushAndRemoveUntil(
        context, PageRouteBuilder(
        pageBuilder: (context, animation1, animation2) => screen,
        transitionDuration: const Duration(seconds: 0),
      ), (route) => false
    );
  });
}

dynamic getValueOfKeyInListMap(String key, String whereKey, dynamic whereValue, List<Map> listMap) {
  var result = null;
  listMap.forEach((element) {
    if(element[whereKey] == whereValue) {
      result = element[key];
    }
  });

  return result ?? "";
}

Future<bool> download(String url, {String savedDir = ""}) async {
  return downloadUri(Uri.parse(url), savedDir: savedDir);
}

Future<bool> downloadUri(Uri uri, {String savedDir = ""}) async {
  final response = await httpClient.head(uri);
  if (response.statusCode ~/ 100 == 2) {
    if(kIsWeb) {
      html.window.open(uri.toString(), "_self");
    } else {
      return false;
    }
    return true;
  } else {
    return false;
  }
}

void showLoading(bool isShow) {
  if(kIsWeb) {
    js.context.callMethod("showLoading", [isShow]);
  }
}

Color fromHex(String hexString) {
  final buffer = StringBuffer();
  if (hexString.length == 6 || hexString.length == 7) buffer.write('ff');
  buffer.write(hexString.replaceFirst('#', ''));
  return Color(int.parse(buffer.toString(), radix: 16));
}

Color randomColor() {
  return Color((math.Random().nextDouble() * 0xFFFFFF).toInt()).withOpacity(1.0);
}

Color getColor(String hexString) {
  if(hexString.isNotEmpty) {
    return fromHex(hexString);
  } else {
    return randomColor();
  }
}
Size textSize(String text, TextStyle style) {
  final TextPainter textPainter = TextPainter(
      text: TextSpan(text: text, style: style), maxLines: 1, textDirection: ui.TextDirection.ltr)
    ..layout(minWidth: 0, maxWidth: double.infinity);
  return textPainter.size;
}

HttpClient httpClient = HttpClient();
