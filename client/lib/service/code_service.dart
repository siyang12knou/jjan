import 'dart:convert';

import '../utils/utils.dart';
import '../model/code.dart';
import '../utils/constants.dart';

class CodeService {
  static final CodeService _instance = CodeService._privateConstructor();

  CodeService._privateConstructor();

  factory CodeService.getInstance() {
    return _instance;
  }

  Map<String, List<Code>> codeCache = Map();

  Future<List<Code>> getCodeList(String groupId, {bool refreshForce = false}) async {
    if(!refreshForce) {
      List<Code> codeList = codeCache[groupId] ?? [];
      if(codeList.isNotEmpty) {
        return codeList;
      }
    }

    try {
      final response = await httpClient.get(
          Uri.parse('${getHost()}$pathApi/code/$groupId'));
      if (response.statusCode ~/ 100 == 2) {
        List<dynamic> parsed = jsonDecode(
            utf8.decode(response.bodyBytes));
        List<Code> codeList = parsed.map((data) => Code.fromJson(data))
            .toList();
        codeCache[groupId] = codeList;
        return codeList;
      } else {
        return [];
      }
    } catch (e) {
      return [];
    }
  }

  Code getCode(String codeId, List<Code> codeList) {
    Code code = null;
    codeList.forEach((element) {
      if(element.codeId == codeId) {
        code = element;
      }
    });

    return code;
  }
}