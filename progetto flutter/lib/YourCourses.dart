import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:progetto_camilloni_tiseni_giri/CardCorso.dart';
import 'package:progetto_camilloni_tiseni_giri/database_utils.dart';
import 'package:progetto_camilloni_tiseni_giri/models/Utente.dart';

import 'models/Corso.dart';

class YourCourses extends StatefulWidget{

  _YourCourses createState() => _YourCourses();
}

class _YourCourses extends State<YourCourses>{

  List<Widget> iscrizioni = [];

  @override
  void initState() {
    super.initState();
    drawIscrizioni().then((cardIscrizioni){
        setState(() {
          iscrizioni = cardIscrizioni;
        });
    });
  }
  @override
  Widget build(BuildContext context){
    return GridView.count(
        padding: EdgeInsets.all(20.0),
        crossAxisSpacing: 10,
        mainAxisSpacing: 10,
        crossAxisCount: 2,
        childAspectRatio: (150 / 190),
        children: iscrizioni
    );
  }
}

//funzione che ritorna la lista di corsi presenti nelle iscrizioni dell'utente
Future<List<Widget>> drawIscrizioni() async {
  List<Widget> cardIscrizioni = [];
  List<Corso> iscrizioni = await DatabaseUtils.getIscrizioni();
  for(var iscrizione in iscrizioni){
    cardIscrizioni.add(CardCorso(iscrizione));
  }
  return cardIscrizioni;
}