import 'package:flutter/material.dart';
import 'package:intl/intl.dart';

import '../../values/colors.dart';

class ResultChannelItem extends StatelessWidget {
  final Map<String, dynamic> itemData;
  
  const ResultChannelItem({Key key, this.itemData}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Align(
      alignment: Alignment.topLeft,
      child: Container(
        width: 334,
        height: 21,
        child: Row(
          children: [
            Container(
              width: 30,
              child: Text(
                itemData["ordinal"].toString(),
                textAlign: TextAlign.left,
                style: TextStyle(
                  color: AppColors.primaryText,
                  fontFamily: "Pretendard",
                  fontWeight: FontWeight.w400,
                  fontSize: 18,
                ),
              ),
            ),
            Container(
              margin: EdgeInsets.only(left: 33),
              child: Text(
                itemData["associatedWord"],
                textAlign: TextAlign.left,
                style: TextStyle(
                  color: AppColors.primaryText,
                  fontFamily: "Pretendard",
                  fontWeight: FontWeight.w400,
                  fontSize: 18,
                  letterSpacing: -0.72,
                ),
              ),
            ),
            Spacer(),
            Text(
              NumberFormat("###,###,###,###").format(itemData["cnt"]),
              textAlign: TextAlign.right,
              style: TextStyle(
                color: AppColors.secondaryText,
                fontFamily: "Pretendard",
                fontWeight: FontWeight.w400,
                fontSize: 18,
              ),
            ),
          ],
        ),
      ),
    );
  }
}
