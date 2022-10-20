/*
*  search_screen.dart
*  Kailoslab_jjaann_trend
*
*  Created by .
*  Copyright © 2018 . All rights reserved.
    */

import 'package:flutter/material.dart';
import 'result/result_screen.dart';
import '../utils/utils.dart';
import '../widget/channel_checkbox.dart';
import '../values/values.dart';

class SearchScreen extends StatefulWidget {
  const SearchScreen();


  @override
  State<SearchScreen> createState() => _SearchScreenState();
}

class _SearchScreenState extends State<SearchScreen> {

  final _formKey = GlobalKey<FormState>();
  final TextEditingController _searchTextController = TextEditingController();
  FocusNode focusNode = FocusNode();
  String hintText = "검색할 키워드를 입력해 주세요";
  final SnsCheckbox snsCheckbox = SnsCheckbox();

  @override
  void initState() {
    super.initState();
    focusNode.addListener(() {
      if (focusNode.hasFocus) {
        hintText = '';
      } else {
        hintText = "검색할 키워드를 입력해 주세요";
      }
      setState(() {});
    });
  }

  @override
  Widget build(BuildContext context) {

    return Scaffold(
      body: Container(
        constraints: BoxConstraints.expand(),
        decoration: BoxDecoration(
          color: Color.fromARGB(255, 0, 0, 0),
        ),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            Container(
              height: 82,
              child: Stack(
                alignment: Alignment.center,
                children: [
                  Positioned(
                    left: 0,
                    top: 0,
                    right: 2,
                    child: Container(
                      height: 80,
                      decoration: BoxDecoration(
                        color: AppColors.secondaryBackground,
                      ),
                      child: Row(
                        children: [
                          Container(
                            width: 200,
                            height: 80,
                            margin: EdgeInsets.only(left: 320),
                            decoration: BoxDecoration(
                              color: AppColors.secondaryBackground,
                            ),
                            child: Row(
                              children: [
                                Container(
                                  width: 136,
                                  height: 21,
                                  child: Image.asset(
                                    "assets/images/image-4.png",
                                    fit: BoxFit.none,
                                  ),
                                ),
                                Spacer(),
                                Container(
                                  width: 14,
                                  height: 14,
                                  margin: EdgeInsets.only(right: 12),
                                  child: Image.asset(
                                    "assets/images/x.png",
                                    fit: BoxFit.none,
                                  ),
                                ),
                                Container(
                                  width: 24,
                                  height: 21,
                                  margin: EdgeInsets.only(right: 3),
                                  child: Image.asset(
                                    "assets/images/image-3.png",
                                    fit: BoxFit.none,
                                  ),
                                ),
                              ],
                            ),
                          ),
                          Spacer(),
                          Container(
                            margin: EdgeInsets.only(right: 323),
                            child: Text(
                              "Worldwide Viral Trend",
                              textAlign: TextAlign.center,
                              style: TextStyle(
                                color: AppColors.accentText,
                                fontWeight: FontWeight.w400,
                                fontSize: 20,
                              ),
                            ),
                          ),
                        ],
                      ),
                    ),
                  ),
                  Positioned(
                    left: 0,
                    top: 79,
                    right: 0,
                    child: Image.asset(
                      "assets/images/-.png",
                      fit: BoxFit.cover,
                    ),
                  ),
                ],
              ),
            ),
            Expanded(
              child: Center(
                child: Container(
                  width: 660,
                  height: 408,
                  // margin: EdgeInsets.only(top: 240),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.stretch,
                    children: [
                      Align(
                        alignment: Alignment.topCenter,
                        child: Column(
                          children: [
                            Text(
                              "키워드 추천받고",
                              textAlign: TextAlign.center,
                              style: TextStyle(
                                color: AppColors.primaryText,
                                fontWeight: FontWeight.w400,
                                fontSize: 50,
                                letterSpacing: -2,
                                height: 1.44,
                              ),
                            ),
                            Row(
                              mainAxisAlignment: MainAxisAlignment.center,
                              children: [
                                Text(
                                  "세계적인 트렌드",
                                  textAlign: TextAlign.center,
                                  style: TextStyle(
                                    color: Color(0xFF1BFE91),
                                    fontWeight: FontWeight.w400,
                                    fontSize: 50,
                                    letterSpacing: -2,
                                    height: 1.44,
                                  ),
                                ),
                                Text(
                                  " 알아보세요",
                                  textAlign: TextAlign.center,
                                  style: TextStyle(
                                    color: AppColors.primaryText,
                                    fontWeight: FontWeight.w400,
                                    fontSize: 50,
                                    letterSpacing: -2,
                                    height: 1.44,
                                  ),
                                ),
                              ],
                            ),
                          ]
                        ),
                      ),
                      Container(
                        height: 56,
                        margin: EdgeInsets.only(top: 60),
                        child: Form(
                          key: _formKey,
                          child: TextFormField(
                            style: TextStyle(color: Colors.black),
                            autofocus: true,
                            controller: _searchTextController,
                            focusNode: focusNode,
                            textAlign: TextAlign.center,
                            onFieldSubmitted: (_) {
                              if(_formKey.currentState.validate()) {
                                _formKey.currentState.save();
                                goToResult();
                              }
                            },
                            decoration: InputDecoration(
                              filled: true,
                              fillColor: const Color.fromARGB(255, 248, 248, 248),
                              contentPadding: const EdgeInsets.symmetric(vertical: 22.5),
                              prefixIcon: Padding(
                                padding: const EdgeInsets.all(10.0),
                                child: Image.asset(
                                  "assets/images/image.png",
                                  width: 20,
                                  height: 20,
                                  fit: BoxFit.fill,
                                ),
                              ),
                              focusedBorder: OutlineInputBorder(
                                borderRadius: BorderRadius.all(Radius.circular(12)),
                                borderSide: const BorderSide(color: AppColors.textBorder, width: 0)),
                              enabledBorder: OutlineInputBorder(
                                  borderRadius: BorderRadius.all(Radius.circular(12)),
                                  borderSide: const BorderSide(color: AppColors.textBorder, width: 0)),
                              errorBorder: OutlineInputBorder(
                                  borderRadius: BorderRadius.all(Radius.circular(12)),
                                  borderSide: const BorderSide(color: AppColors.textBorder, width: 0)),
                              focusedErrorBorder: OutlineInputBorder(
                                  borderRadius: BorderRadius.all(Radius.circular(12)),
                                  borderSide: const BorderSide(color: AppColors.textBorder, width: 0)),

                              hintText: hintText,
                              hintStyle: const TextStyle(
                                color: AppColors.accentText,
                                fontWeight: FontWeight.w400,
                                fontSize: 18,
                              )
                            ),
                          ),
                        )
                      ),
                      Align(
                        alignment: Alignment.topCenter,
                        child: Container(
                          width: 402,
                          height: 22,
                          margin: EdgeInsets.only(top: 30),
                          child: snsCheckbox,
                        ),
                      ),
                      Spacer(),
                      Align(
                        alignment: Alignment.topCenter,
                        child: MouseRegion(
                          cursor: SystemMouseCursors.click,
                          child: GestureDetector(
                            child: Image.asset(
                              "assets/images/btn_search_trend.png",
                            ),
                            onTap: goToResult,
                          ),
                        ),
                      ),
                    ],
                  ),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }

  @override
  void dispose() {
    focusNode.dispose();
    super.dispose();
  }

  void goToResult() {
    String searchWord = _searchTextController.text;
    if(searchWord.isEmpty) {
      showAlert(context, title: "", message: "검색할 키워드를 입력해주세요.");
      return;
    }

    List<String> snsList = snsCheckbox.values;
    if(snsList.isEmpty) {
      showAlert(context, title: "", message: "채널을 선택해주세요.");
      return;
    }

    goTo(context, ResultScreen(searchWord, snsList));
  }
}