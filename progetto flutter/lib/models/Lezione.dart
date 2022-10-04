class Lezione {
  Lezione({
    required this.descrizione,
    required this.id,
    required this.titolo,
    required this.url,
  });
  String descrizione;
  String id;
  String titolo;
  String url;
  factory Lezione.fromJson(Map<String, dynamic> json) => Lezione(
    descrizione: json["descrizione"],
    id: json["id"],
    titolo: json["titolo"],
    url: json["url"],
  );
  Map<String, dynamic> toJson() => {
    "descrizione": descrizione,
    "id": id,
    "titolo": titolo,
    "url": url,
  };
}