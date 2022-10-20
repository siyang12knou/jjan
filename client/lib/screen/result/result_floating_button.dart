import 'package:flutter/material.dart';
import 'dart:math' as math;

import '../../utils/constants.dart';

class ResultFloatingActionButtonLocation extends _DockedFloatingActionButtonLocation {
  const ResultFloatingActionButtonLocation();

  @override
  Offset getOffset(ScaffoldPrelayoutGeometry scaffoldGeometry) {
    final double fabX = (scaffoldGeometry.scaffoldSize.width - scaffoldGeometry.floatingActionButtonSize.width) / 2.0 + ((panelWidth + scaffoldGeometry.floatingActionButtonSize.width) /2) + defaultSpace;
    return Offset(fabX, getDockedY(scaffoldGeometry));
  }
}

abstract class _DockedFloatingActionButtonLocation extends FloatingActionButtonLocation {
  const _DockedFloatingActionButtonLocation();
  @protected
  double getDockedY(ScaffoldPrelayoutGeometry scaffoldGeometry) {
    final double contentBottom = scaffoldGeometry.contentBottom;
    final double appBarHeight = scaffoldGeometry.bottomSheetSize.height;
    final double fabHeight = scaffoldGeometry.floatingActionButtonSize.height;
    final double snackBarHeight = scaffoldGeometry.snackBarSize.height;

    double fabY = contentBottom - fabHeight / 2.0;
    if (snackBarHeight > 0.0)
      fabY = math.min(fabY, contentBottom - snackBarHeight - fabHeight - kFloatingActionButtonMargin);
    if (appBarHeight > 0.0)
      fabY = math.min(fabY, contentBottom - appBarHeight - fabHeight / 2.0);

    final double maxFabY = scaffoldGeometry.scaffoldSize.height - fabHeight;
    return math.min(maxFabY, fabY) - defaultSpace;
  }
}