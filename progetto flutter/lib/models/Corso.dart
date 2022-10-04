import 'dart:convert';
import 'dart:ffi';
import 'package:firebase_database/firebase_database.dart';
import 'package:progetto_camilloni_tiseni_giri/models/Documento.dart';
import 'package:progetto_camilloni_tiseni_giri/models/Lezione.dart';

class Corso implements Comparable{
  Corso({
    required this.id,
    required this.categoria,
    required this.descrizione,
    required this.dispense,
    required this.immagine,
    required this.lezioni,
    required this.titolo,
    required this.recensioni,
    required this.avg,
    required this.prezzo,
    required this.docente,
  });

  String id;
  String categoria;
  String descrizione;
  List<Documento> dispense;
  String immagine;
  List<Lezione> lezioni;
  List<num> recensioni;
  num avg;
  String titolo;
  String docente;
  String prezzo;

  //override della funzione compareTo per fare il confronto tra i corsi in base alla media delle recensioni. QUesta funzione viene usata nel sort nella homepage
  @override
  int compareTo(other) {
    if(this.avg < other.avg){
      return 1;
    }
    else if(this.avg > other.avg){
      return -1;
    }
    else return 0;
  }
}
