/*
*  main.dart
*  Kailoslab_jjaann_trend — 9월 20일 오전 10.40.51
*
*  Created by [Author].
*  Copyright © 2018 [Company]. All rights reserved.
    */

import 'package:flutter/material.dart';
import 'screen/search_screen.dart';

void main() => runApp(App());


class App extends StatelessWidget {
  
  @override
  Widget build(BuildContext context) {
  
    return MaterialApp(
      theme: ThemeData(
        brightness: Brightness.dark,
        fontFamily: "Pretendard",
        checkboxTheme: CheckboxThemeData(
          shape: const RoundedRectangleBorder(borderRadius: BorderRadius.all(Radius.circular(5.0))), // Rounded Checkbox
          side: MaterialStateBorderSide.resolveWith(
                (states) {
              if(states.contains(MaterialState.selected)) {
                return BorderSide(width: 2.0, color: Color(0xFF1BFE91));
              } else {
                return BorderSide(width: 2.0, color: Color(0xFF85858C));
              }
            },
          ),
          fillColor: MaterialStateProperty.all(Color(0xFF1BFE91)),
          checkColor: MaterialStateProperty.all(Colors.black),
        ),
      ),
      // home: ResultScreen("치맥", ["insta", "youtube"]),
      home: SearchScreen()
    );
  }
}