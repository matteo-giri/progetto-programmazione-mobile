import 'dart:core';
import 'dart:convert';
import 'package:firebase_database/firebase_database.dart';

Utente utenteFromJson(String str) => Utente.fromJson(json.decode(str));
String utenteToJson(Utente data) => json.encode(data.toJson());

class Utente {
  String firstName;
  String lastName;
  List<String> iscrizioni;
  List<String> categoriePref;
  List<String> wishlist;

  Utente({
    required this.firstName,
    required this.lastName,
    required this.iscrizioni,
    required this.categoriePref,
    required this.wishlist,
  });

  Utente.fromSnapshot(DataSnapshot snapshot) :
    firstName = snapshot.value["firstName"],
    lastName = snapshot.value["lastName"],
    iscrizioni = snapshot.value["iscrizioni"],
    categoriePref = snapshot.value["categoriePref"],
    wishlist = snapshot.value["wishlist"];

  factory Utente.fromJson(Map<String, dynamic> json) => Utente(
    categoriePref: List<String>.from(json["categoriePref"].map((x) => x)),
    firstName: json["firstName"],
    iscrizioni: List<String>.from(json["iscrizioni"].map((x) => x)),
    lastName: json["lastName"],
    wishlist: List<String>.from(json["wishlist"].map((x) => x)),
  );

  Map<String, dynamic> toJson() => {
      "firstName": firstName,
      "lastName": lastName,
      "iscrizioni": List<dynamic>.from(iscrizioni.map((x) => x)),
      "categoriePref": List<dynamic>.from(categoriePref.map((x) => x)),
      "wishlist" : List<dynamic>.from(wishlist.map((x) => x)),
  };
}