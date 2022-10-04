import 'package:flutter/material.dart';
import 'package:progetto_camilloni_tiseni_giri/Lezione.dart';
import 'Video_screen.dart';
import 'models/Corso.dart';
import 'models/Lezione.dart';

class Lezioni extends StatefulWidget {
  final Corso corso;

  const Lezioni(this.corso);

  _Lezioni createState() => _Lezioni();
}
class _Lezioni extends State<Lezioni>{

  @override
  Widget build(BuildContext context) {
    return ListView(
      children: getLezioni(widget.corso)
      );
  }

  //funzione che ritorna la lista delle lezioni del corso come widget
  List<Widget> getLezioni(Corso corso){
    List<Widget> listaLezioni = [];
    for(Lezione lezione in corso.lezioni) {
      listaLezioni.add(LezioneLista(lezione));
    }
    return listaLezioni;
  }

}