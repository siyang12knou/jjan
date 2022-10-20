class Code {
  final String groupId;
  final String codeId;
  final String name;
  final int ordinal;
  final Map<String, dynamic> properties;

  Code({this.groupId, this.codeId, this.name, this.ordinal, this.properties});

  factory Code.fromJson(Map<String, dynamic> json) {
    return Code(
      groupId: json["groupId"],
      codeId: json["codeId"],
      name: json["name"],
      ordinal: json["ordinal"],
      properties: json["properties"],
    );
  }

  @override
  String toString() {
    return name;
  }
}