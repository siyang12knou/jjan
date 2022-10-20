/*
*  result_screen.dart
*  Kailoslab_jjaann_trend
*
*  Created by .
*  Copyright © 2018 . All rights reserved.
    */

import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:jjan_client/model/result_message.dart';
import 'package:jjan_client/screen/result/mention_chart.dart';
import 'package:jjan_client/service/jjan_service.dart';
import '../../model/code.dart';
import '../../service/code_service.dart';
import '../../widget/hspace_widget.dart';
import '../../widget/vspace_widget.dart';
import '../../utils/constants.dart';
import '../../values/values.dart';
import 'result_channel.dart';
import 'result_floating_button.dart';


class ResultScreen extends StatefulWidget {

  final String searchWord;
  final List<String> snsList;

  const ResultScreen(this.searchWord, this.snsList);

  @override
  State<ResultScreen> createState() => _ResultScreenState();
}

class _ResultScreenState extends State<ResultScreen> {

  final VSpace vSpace = VSpace(defaultSpace);
  final BoxDecoration panelDecoration = BoxDecoration(
    color: AppColors.primaryBackground,
    borderRadius: Radii.k18pxRadius,
  );

  List<Code> snsCodeList = [];
  ResultMessage _resultMessage = ResultMessage.empty();

  @override
  void initState() {
    super.initState();
    CodeService.getInstance().getCodeList("sns").then((value) => setState(() => snsCodeList = value));
    DateTime current = DateTime.now();
    String toYm = DateFormat("yyyyMM").format(current);
    String fromYm = DateFormat("yyyyMM").format(current.subtract(Duration(days: 365)));
    JJanService.getInstance().search(widget.searchWord, fromYm, toYm, widget.snsList).then((value) => setState(() => _resultMessage = value));
  }

  @override
  Widget build(BuildContext context) {

    if(snsCodeList.isNotEmpty) {
      widget.snsList.sort((a, b) {
        Code aCode = CodeService.getInstance().getCode(a, snsCodeList);
        Code bCode = CodeService.getInstance().getCode(b, snsCodeList);
        return aCode.ordinal - bCode.ordinal;
      });
    }

    return Scaffold(
      body: snsCodeList.isEmpty || !_resultMessage.result ?
      Center(child: Text(_resultMessage.message)) :
      Container(
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
              child: Container(
                child: SingleChildScrollView(
                  scrollDirection: Axis.vertical,
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.center,
                    children: [
                      vSpace,
                      Row(
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: [
                          Text(
                            "‘${widget.searchWord}’ 키워드 검색 결과",
                            textAlign: TextAlign.center,
                            style: TextStyle(
                              color: AppColors.primaryText,
                              fontWeight: FontWeight.w400,
                              fontSize: 26,
                              letterSpacing: -1.04,
                            ),
                          ),
                          HSpace(10),
                          Tooltip(
                            height: 84,
                            verticalOffset: 54,
                            message: "최근 1년간 수집한 SNS 데이터 기반으로\n카이로스랩 AI 플랫폼 [AI4X]가\n제공한 결과입니다.",
                            child: Container(
                              width: 22,
                              height: 22,
                              decoration: BoxDecoration(
                                color: Color.fromARGB(255, 26, 255, 145),
                                borderRadius: BorderRadius.all(Radius.circular(11)),
                              ),
                              child: Column(
                                mainAxisAlignment: MainAxisAlignment.center,
                                crossAxisAlignment: CrossAxisAlignment.stretch,
                                children: [
                                  Container(
                                    margin: EdgeInsets.only(left: 7, right: 6),
                                    child: Text(
                                      "?",
                                      textAlign: TextAlign.center,
                                      style: TextStyle(
                                        color: Color.fromARGB(255, 0, 0, 0),
                                        fontWeight: FontWeight.w400,
                                        fontSize: 17,
                                      ),
                                    ),
                                  ),
                                ],
                              ),
                            ),
                          ),
                        ]
                      ),
                      vSpace,
                      Container(
                        width: panelWidth,
                        height: 120,
                        decoration: panelDecoration,
                        child: Row(
                          children: [
                            Container(
                              margin: EdgeInsets.only(left: 110),
                              child: Text(
                                "SNS 채널",
                                textAlign: TextAlign.left,
                                style: TextStyle(
                                  color: AppColors.primaryText,
                                  fontWeight: FontWeight.w400,
                                  fontSize: 26,
                                  letterSpacing: -1.04,
                                ),
                              ),
                            ),
                            Container(
                              margin: EdgeInsets.only(left: 31),
                              child: Row(
                                children: List.generate(widget.snsList.length, (index) {
                                  Code code = CodeService.getInstance().getCode(widget.snsList[index], snsCodeList);
                                    return Row(
                                      children: [
                                        Text(
                                            code.name,
                                            textAlign: TextAlign.left,
                                            style: TextStyle(
                                            color: AppColors.primaryText,
                                            fontWeight: FontWeight.w400,
                                            fontSize: 18,
                                        )),
                                        HSpace(31),
                                      ],
                                    );
                                  }
                                ),
                              )
                            ),
                          ],
                        ),
                      ),
                      vSpace,
                      MentionChart(
                        panelDecoration: panelDecoration,
                        mentionData: _resultMessage.data["mentionList"],
                        snsCodeList: snsCodeList,
                      ),
                      Column(
                        children: List.generate(widget.snsList.length, (index) {
                          Code code = CodeService.getInstance().getCode(widget.snsList[index], snsCodeList);
                          return Column(
                            children: [
                              vSpace,
                              Container(
                                width: panelWidth,
                                height: 380,
                                decoration: panelDecoration,
                                child: ResultChannel(
                                    code: code,
                                    mentionData: _resultMessage.data["mentionList"],
                                    associatedData: _resultMessage.data["associationList"][code.codeId]
                                )
                              )
                            ]
                          );
                        }),
                      ),
                      vSpace,
                    ],
                  ),
                ),
              ),
            ),
          ],
        ),
      ),
      floatingActionButton: snsCodeList.isEmpty || !_resultMessage.result ? null :
      Container(
        width: 100,
        height: 117,
        decoration: BoxDecoration(
          color: AppColors.primaryBackground,
          border: Border.all(color: Color(0xFF1BFE91), width: 2),
          borderRadius: Radii.k18pxRadius,
          boxShadow: [
            BoxShadow(
              color: Color.fromARGB(255, 0, 250, 139),
              offset: Offset(0, 0),
              blurRadius: 12,
           ),
          ],
        ),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.center,
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Icon(Icons.search, size: 50,),
            VSpace(10),
            Text("키워드 재검색")
          ],
        ),
      ),
      floatingActionButtonLocation: ResultFloatingActionButtonLocation(),
    );
  }
}