class ResultMessage {
  final bool result;
  final String message;
  final dynamic data;

  const ResultMessage({
    this.result,
    this.message,
    this.data
  });

  factory ResultMessage.fromJson(Map<String, dynamic> json) {
    return ResultMessage(
      result: json["result"],
      message: json["message"],
      data: json["data"],
    );
  }

  factory ResultMessage.empty() {
    return const ResultMessage(
      result: false,
      message: "잠시만 기다려주십시요.",
      data: "",
    );
  }
}