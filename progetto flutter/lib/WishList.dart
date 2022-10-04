import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:progetto_camilloni_tiseni_giri/CardCorso.dart';
import 'package:progetto_camilloni_tiseni_giri/database_utils.dart';
import 'package:progetto_camilloni_tiseni_giri/models/Utente.dart';

import 'models/Corso.dart';

class WishList extends StatefulWidget{

  _WishList createState() => _WishList();
}

class _WishList extends State<WishList>{

  List<Widget> wishlist = [];

  @override
  void initState() {
    super.initState();
    drawWishList().then((cardWishList){ //richiama la funzione che ritorna la lista di corsi e la mette nella wishlist
      setState(() {
        wishlist = cardWishList;
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
        children: wishlist
    );
  }
}

//funzione che ritorna la lista di corsi che l'utente ha nella wishlist
Future<List<Widget>> drawWishList() async {
  List<Widget> cardWishList = [];
  List<Corso> corsi = await DatabaseUtils.getListaCorsi();
  Utente user = await DatabaseUtils.getUtenteLoggato();
  List<Corso> wishlist = [];
  for(Corso corso in corsi){
    if(user.wishlist.contains(corso.id)){
      cardWishList.add(CardCorso(corso));
    }
  }
  return cardWishList;
}