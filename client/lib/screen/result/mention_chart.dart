import 'package:flutter/material.dart';

import 'package:jjan_client/widget/vspace_widget.dart';
import 'package:syncfusion_flutter_charts/charts.dart';

import '../../model/code.dart';
import '../../service/code_service.dart';
import '../../utils/constants.dart';
import '../../utils/utils.dart';
import '../../values/colors.dart';

class MentionChart extends StatelessWidget {
  final BoxDecoration panelDecoration;
  final List<dynamic> mentionData;
  final List<Code> snsCodeList;
  const MentionChart({key, this.panelDecoration, this.mentionData, this.snsCodeList}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Container(
      width: panelWidth,
      height: 623,
      decoration: panelDecoration,
      child: Column(
        children: [
          Align(
            alignment: Alignment.topLeft,
            child: Container(
              margin: EdgeInsets.only(left: 110, top: 71),
              child: Text(
                "SNS 언급량 추이",
                textAlign: TextAlign.left,
                style: TextStyle(
                  color: AppColors.primaryText,
                  fontWeight: FontWeight.w400,
                  fontSize: 26,
                  letterSpacing: -1.04,
                ),
              ),
            ),
          ),
          VSpace(50),
          Container(
            height: 410,
            child: Padding(
              padding: const EdgeInsets.only(left: 110, right: 110),
              child: SfCartesianChart(
                primaryXAxis: CategoryAxis(
                  majorGridLines: MajorGridLines(width: 0)
                ),
                legend: Legend(
                  width: "120%",
                  isVisible: true,
                  position: LegendPosition.bottom,
                  overflowMode: LegendItemOverflowMode.wrap,
                ),
                tooltipBehavior: TooltipBehavior(
                  enable: true,
                ),
                trackballBehavior: TrackballBehavior(
                  enable: true,
                  lineColor: Colors.white
                ),
                series: getChartSeries(),
              ),
            ),
          ),
        ],
      ),
    );
  }

  List<SplineSeries> getChartSeries() {
    if(mentionData.isEmpty) {
      return [];
    } else {
      Map<String, List<MentionChartData>> dataList = {};
      mentionData.forEach((element) {
        String sns = element["sns"];
        List<MentionChartData> snsDataList = dataList[sns] ?? [];
        snsDataList.add(MentionChartData(sns, element["ym"], element["cnt"]));
        dataList[sns] = snsDataList;
      });

      List<SplineSeries> chartSeriesList = [];
      snsCodeList.forEach((code) {
        if(dataList[code.codeId] != null) {
          chartSeriesList.add(
              SplineSeries<MentionChartData, String> (
                dataSource: dataList[code.codeId],
                xValueMapper: (MentionChartData data, _) => data.ym.substring(2, 4) + "." + data.ym.substring(4),
                yValueMapper: (MentionChartData data, _) => data.cnt,
                color: code.properties["color"] == null ? randomColor() : fromHex(code.properties["color"]),
                name: code.name,
                legendIconType: LegendIconType.rectangle,
              )
          );
        }
      });

      return chartSeriesList;
    }
  }
}

class MentionChartData {
  final String sns;
  final String ym;
  final int cnt;

  MentionChartData(this.sns, this.ym, this.cnt);
}
