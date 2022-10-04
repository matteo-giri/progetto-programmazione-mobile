import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:progetto_camilloni_tiseni_giri/CardCorso.dart';
import 'package:progetto_camilloni_tiseni_giri/database_utils.dart';
import 'package:progetto_camilloni_tiseni_giri/models/Utente.dart';

import 'models/Corso.dart';

class Categoria extends StatefulWidget{
  final String categoria;
  const Categoria(this.categoria); //costruttore con parametro la categorie
  _Categoria createState() => _Categoria();
}

class _Categoria extends State<Categoria>{

  List<Widget> corsiCat = [];

  @override
  void initState() { //richiamato solo all'inizio, serve per settare lo stato iniziale
    super.initState();
    drawCategoria(widget.categoria).then((cardsCategoria){ //mette il risultato della funzione drawCategoria nella lista dei corsi della categoria cliccata nelle chips del catalogo
      setState(() {
        corsiCat = cardsCategoria;
      });
    });
  }
  @override
  Widget build(BuildContext context){
    return Scaffold(
        appBar: AppBar(
          automaticallyImplyLeading: false,
          title: Text(widget.categoria),
          leading: IconButton(icon: Icon(Icons.arrow_back_outlined),
          onPressed: (){
            Navigator.pop(context);
          }),
          actions: <Widget>[
            PopupMenuButton<String>(
              onSelected: handleClick,
              itemBuilder: (BuildContext context) {
                return {'Condividi', 'Logout'}.map((String choice) {
                  return PopupMenuItem<String>(
                    value: choice,
                    child: Text(choice),
                  );
                }).toList();
              },
            ),
          ],
        ),
        body: Center(
          child:GridView.count(
            padding: EdgeInsets.all(20.0),
            crossAxisSpacing: 10,
            mainAxisSpacing: 10,
            crossAxisCount: 2,
            childAspectRatio: (150 / 190),
            children: corsiCat
          )
        )
    );
  }
}

//funzione che ritorna la lista dei corsi della categoria specificiata come parametro
Future<List<Widget>> drawCategoria(String categoria) async {
  List<Widget> cardCategoria = [];
  List<Corso> corsi = await DatabaseUtils.getListaCorsi();
  for(var corso in corsi){
    if(corso.categoria == categoria) {
      cardCategoria.add(CardCorso(corso));
    }
  }
  return cardCategoria;
}

//funzione che regola il menu nella topAppBar
handleClick(String value) {
  switch (value) {
    case 'Logout':
    //context.read<AuthenticationService>().signOut(context);
      break;
    case 'Settings':
      break;
  }
}