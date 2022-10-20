class Word {
  int id;
  String word;

  Word({this.id, this.word});

  factory Word.fromJson(Map<String, dynamic> json) {
    return Word(
        id: json["id"],
        word: json["word"]
    );
  }

  @override
  String toString() {
    return word;
  }
}