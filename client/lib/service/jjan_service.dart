import 'dart:convert';

import '../model/result_message.dart';
import '../utils/utils.dart';
import '../model/code.dart';
import '../model/word.dart';
import '../utils/constants.dart';

class JJanService {
  static final JJanService _instance = JJanService._privateConstructor();

  JJanService._privateConstructor();

  factory JJanService.getInstance() {
    return _instance;
  }

  Future<List<Word>> getWordList(String prefix) async {
    final response = await httpClient.get(
        Uri.parse('${getHost()}$pathApi/word?prefix=$prefix'));
    if (response.statusCode ~/ 100 == 2) {
      List<dynamic> parsed = jsonDecode(
          utf8.decode(response.bodyBytes));
      return parsed.map((data) => Word.fromJson(data)).toList();
    } else {
      throw dataErrorMessage;
    }
  }

  Future<List<Code>> getCodeList(String groupId) async {
    final response = await httpClient.get(
        Uri.parse('${getHost()}$pathApi/code/$groupId'));
    if (response.statusCode ~/ 100 == 2) {
      List<dynamic> parsed = jsonDecode(
          utf8.decode(response.bodyBytes));
      return parsed.map((data) => Code.fromJson(data)).toList();
    } else {
      throw dataErrorMessage;
    }
  }

  Future<ResultMessage> search(String word, String fromYm, String toYm, List<String> sns) async {
    String snsQuery = sns.join("&sns=");
    final response = await httpClient.get(
        Uri.parse('${getHost()}$pathApi/search?word=$word&fromYm=$fromYm&toYm=$toYm&sns=$snsQuery'));
    if (response.statusCode ~/ 100 == 2) {
      Map<String, dynamic> parsed = jsonDecode(
          utf8.decode(response.bodyBytes));
      return ResultMessage.fromJson(parsed);
    } else {
      throw dataErrorMessage;
    }
  }
}