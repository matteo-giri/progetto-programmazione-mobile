/* Questa classe identifica la bottom navigation bar, nella quale si inseriscono
le varie destinazioni
 */

import 'package:flutter/material.dart';
import 'package:progetto_camilloni_tiseni_giri/WishList.dart';
import 'package:progetto_camilloni_tiseni_giri/YourCourses.dart';
//import 'package:progetto_camilloni_tiseni_giri/area_utente.dart';
import 'package:progetto_camilloni_tiseni_giri/Home.dart';
import 'package:progetto_camilloni_tiseni_giri/Catalogo.dart';
import 'area_utente.dart';
import 'package:share_plus/share_plus.dart';
import 'authentication_service.dart';
import 'package:provider/provider.dart';

class Nav extends StatefulWidget {
  @override
  _NavState createState() => _NavState();
}

class _NavState extends State<Nav> {
  int _selectedIndex = 0; //l'indice selezionato è inizializzato con quello della homepage
  List<Widget> _widgetOptions = <Widget>[ //rotte della navbar
    Home(), //rotta dell' homepage dell'applicazione
    Catalogo(), //rotta del catalogo dei corsi
    YourCourses(), //rotta dei corsi a cui l'utente è iscritto
    WishList(), //rotta della wishlist
    AreaUtente(), //rotta dell'area utente
  ];

  void _onItemTap(int index){ //funzione attivata quando clicco su un elemento della bottom navigation bar, tramite il setState cambio il valore dell'index selezionato
    setState((){ //la funzione setState notifica il framework che lo stato interno dell'oggetto viene cambiato
      _selectedIndex = index;
    });
  }
  @override
  Widget build(BuildContext context){
    return Scaffold(
      appBar: AppBar(
        automaticallyImplyLeading: false, //rimuove il backbutton

        title: Text('Corsi per tutti'),
        actions: <Widget>[
          PopupMenuButton<String>(
            onSelected: handleClick,
            itemBuilder: (BuildContext context) {
              return {'Condividi'}.map((String choice) {
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
        child: _widgetOptions.elementAt(_selectedIndex)
      ),
      bottomNavigationBar:BottomNavigationBar(
        type: BottomNavigationBarType.fixed,
        backgroundColor: Color(0xFF6200EE),
        selectedItemColor: Colors.white,
        unselectedItemColor: Colors.white.withOpacity(.60),
        selectedFontSize: 14,
        unselectedFontSize: 14,
        items: [
          BottomNavigationBarItem(
            title: Text('Home'),
            icon: Icon(Icons.home),
          ),
          BottomNavigationBarItem(
            title: Text('Catalogo'),
            icon: Icon(Icons.menu),
          ),
          BottomNavigationBarItem(
            title: Text('I tuoi Corsi'),
            icon: Icon(Icons.move_to_inbox_sharp),
          ),
          BottomNavigationBarItem(
            title: Text('Wishlist'),
            icon: Icon(Icons.favorite),
          ),
          BottomNavigationBarItem(
            title: Text('Utente'),
            icon: Icon(Icons.person),
          ),
        ],
        currentIndex: _selectedIndex,
        onTap: _onItemTap,
      )
    );
  }
}

//funzione che regola il menu nella topAppBar
handleClick(String values) {
  Share.share('check out my website https://example.com', subject: 'Look what I made!');
}