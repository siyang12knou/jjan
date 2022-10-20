import 'package:flutter/material.dart';

import '../model/code.dart';
import '../service/code_service.dart';

class SnsCheckbox extends StatefulWidget {
  SnsCheckbox();

  @override
  State<SnsCheckbox> createState() => _SnsCheckboxState();

  final List<String> _checkedSnsList = [];
  List<String> get values => _checkedSnsList;
}

class _SnsCheckboxState extends State<SnsCheckbox> {

  List<Code> snsCodeList = [];

  @override
  void initState() {
    super.initState();
    CodeService.getInstance().getCodeList("sns").then((value) => setState(() {
      snsCodeList = value;
      widget._checkedSnsList.clear();
      snsCodeList.forEach((code) {
        widget._checkedSnsList.add(code.codeId);
      });
    }));
  }

  setValue(String id, bool value) {
    setState(() {
      if(value) {
        if(!widget._checkedSnsList.contains(id)) {
          widget._checkedSnsList.add(id);
        }
      } else {
        if(widget._checkedSnsList.contains(id)) {
          widget._checkedSnsList.remove(id);
        }
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    List<Widget> checkboxList = snsCodeList.map((code) => Row(
      children: [
        Checkbox(
          value: widget._checkedSnsList.contains(code.codeId),
          onChanged: (newValue) {
            setValue(code.codeId, newValue);
          }),
        Text(code.name),
      ]
    )).toList();

    return Row(
      mainAxisAlignment: MainAxisAlignment.spaceAround,
      children: checkboxList,
    );
  }
}
