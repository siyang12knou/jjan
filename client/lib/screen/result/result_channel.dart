import 'package:auto_size_text/auto_size_text.dart';
import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:jjan_client/screen/result/result_channel_item.dart';
import 'package:jjan_client/utils/utils.dart';
import 'package:jjan_client/widget/vspace_widget.dart';

import '../../model/code.dart';
import '../../values/values.dart';

class ResultChannel extends StatefulWidget {
  final Code code;
  final List<dynamic> mentionData;
  final List<dynamic> associatedData;

  const ResultChannel({Key key, this.code, this.associatedData, this.mentionData}) : super(key: key);

  @override
  State<ResultChannel> createState() => _ResultChannelState();
}

class _ResultChannelState extends State<ResultChannel> {
  @override
  Widget build(BuildContext context) {
    int sum = 0;
    widget.mentionData.forEach((element) {
      String sns = element["sns"];
      if(widget.code.codeId == sns) {
        sum += element["cnt"];
      }
    });

    List<ResultChannelItem> items = [];
    if(widget.associatedData != null) {
      for(int i = 0; i < widget.associatedData.length && i < 10; i++) {
        var element = widget.associatedData[i];
        element["ordinal"] = i + 1;
        items.add(ResultChannelItem(itemData: element));
      }
    }

    List<Widget> lines = [];
    for (int i = 0; i < 5; i++) {
      if(items.length > i) {
        var item1 = items[i];
        var item2 = items.length > (i + 5) ? items[i + 5] : Container();
        lines.add(
            Container(
              height: 21,
              child: Row(
                crossAxisAlignment: CrossAxisAlignment.stretch,
                children: [
                  item1,
                  Spacer(),
                  item2,
                ],
              ),
            )
        );
      }
    }

    return Row(
      children: [
        Align(
          alignment: Alignment.topLeft,
          child: Container(
            width: 231,
            height: 237,
            margin: EdgeInsets.only(left: 76, top: 70),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.stretch,
              children: [
                Align(
                  alignment: Alignment.topLeft,
                  child: Container(
                    width: 142,
                    height: 31,
                    child: Row(
                      children: [
                        Container(
                          width: 14,
                          height: 14,
                          decoration: BoxDecoration(
                            color: getColor(widget.code.properties["color"]),
                            borderRadius: BorderRadius.all(Radius.circular(7)),
                          ),
                          child: Container(),
                        ),
                        Spacer(),
                        Text(
                          widget.code.name,
                          textAlign: TextAlign.left,
                          style: TextStyle(
                            color: AppColors.primaryText,
                            fontWeight: FontWeight.w400,
                            fontSize: 26,
                            letterSpacing: -1.04,
                          ),
                        ),
                      ],
                    ),
                  ),
                ),
                Align(
                  alignment: Alignment.topLeft,
                  child: Container(
                    width: 197,
                    height: 101,
                    margin: EdgeInsets.only(left: 34, top: 105),
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.stretch,
                      children: [
                        Align(
                          alignment: Alignment.topLeft,
                          child: Container(
                            margin: EdgeInsets.only(left: 2),
                            child: Text(
                              "총 언급량",
                              textAlign: TextAlign.left,
                              style: TextStyle(
                                color: AppColors.primaryText,
                                fontFamily: "Pretendard",
                                fontWeight: FontWeight.w400,
                                fontSize: 20,
                                letterSpacing: -0.8,
                              ),
                            ),
                          ),
                        ),
                        Spacer(),
                        AutoSizeText(
                          NumberFormat("###,###,###,###").format(sum),
                          textAlign: TextAlign.left,
                          style: TextStyle(
                            color: AppColors.primaryText,
                            fontFamily: "Pretendard",
                            fontWeight: FontWeight.w400,
                            fontSize: 40,
                            letterSpacing: -1.6,
                          ),
                          minFontSize: 12,
                          maxLines: 1,
                        ),
                      ],
                    ),
                  ),
                ),
              ],
            ),
          ),
        ),
        Container(
          width: 2,
          height: 236,
          margin: EdgeInsets.only(left: 74),
          decoration: BoxDecoration(
            color: AppColors.secondaryElement,
          ),
          child: Container(),
        ),
        Spacer(),
        Container(
          width: 713,
          height: 237,
          margin: EdgeInsets.only(right: 107),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.spaceAround,
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [
              Align(
                alignment: Alignment.topLeft,
                child: Container(
                  width: 247,
                  height: 24,
                  child: Row(
                    crossAxisAlignment: CrossAxisAlignment.stretch,
                    children: lines.isNotEmpty ? [
                      Align(
                        alignment: Alignment.topLeft,
                        child: Text(
                          "연관 키워드 추천 TOP 10",
                          textAlign: TextAlign.left,
                          style: TextStyle(
                            color: AppColors.primaryText,
                            fontFamily: "Pretendard",
                            fontWeight: FontWeight.w400,
                            fontSize: 20,
                            letterSpacing: -0.8,
                          ),
                        ),
                      ),
                      Align(
                        alignment: Alignment.topLeft,
                        child: Container(
                          margin: EdgeInsets.only(left: 10, top: 6),
                          child: Text(
                            "(단위: 건)",
                            textAlign: TextAlign.left,
                            style: TextStyle(
                              color: AppColors.secondaryText,
                              fontFamily: "Pretendard",
                              fontWeight: FontWeight.w400,
                              fontSize: 14,
                              letterSpacing: -0.56,
                            ),
                          ),
                        ),
                      ),
                    ] : [
                      Align(
                        alignment: Alignment.topLeft,
                        child: Text(
                          "연관 키워드가 없습니다.",
                          textAlign: TextAlign.left,
                          style: TextStyle(
                            color: AppColors.primaryText,
                            fontFamily: "Pretendard",
                            fontWeight: FontWeight.w400,
                            fontSize: 20,
                            letterSpacing: -0.8,
                          ),
                        ),
                      ),
                    ],
                  ),
                ),
              ),
              VSpace(15),
              ...lines,
            ],
          ),
        ),
      ],
    );
  }
}
