/*
  Classe che imposta la bottom navigation bar per il singolo corso
*/

import 'package:flutter/material.dart';
import 'package:progetto_camilloni_tiseni_giri/YourCourses.dart';
//import 'package:progetto_camilloni_tiseni_giri/prova.dart';
import 'package:progetto_camilloni_tiseni_giri/Home.dart';
import 'package:progetto_camilloni_tiseni_giri/Catalogo.dart';

import 'Dispense.dart';
import 'InfoCorso.dart';
import 'Lezioni.dart';
import 'models/Corso.dart';

class NavCorso extends StatefulWidget {
  final Corso corso;
  const NavCorso(this.corso);
  @override
  _NavCorsoState createState() => _NavCorsoState();
}

class _NavCorsoState extends State<NavCorso> {
  int _selectedIndex = 0;


  void _onItemTap(int index){
    setState((){
      _selectedIndex = index;
    });
  }
  @override
  Widget build(BuildContext context){

    List<Widget> _widgetOptions = <Widget>[
      InfoCorso(widget.corso),
      Lezioni(widget.corso),
      Dispense(widget.corso),
    ];

    return Scaffold(
        appBar: AppBar(
          title: Text(widget.corso.titolo),
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
              title: Text('Info'),
              icon: Icon(Icons.info_outline),
            ),
            BottomNavigationBarItem(
              title: Text('Lezioni'),
              icon: Icon(Icons.menu_book_outlined),
            ),
            BottomNavigationBarItem(
              title: Text('Dispense'),
              icon: Icon(Icons.insert_drive_file),
            ),
          ],
          currentIndex: _selectedIndex,
          onTap: _onItemTap,
        )
    );
  }
  handleClick(String value) {
    switch (value) {
      case 'Logout':
      //context.read<AuthenticationService>().signOut(context);
        break;
      case 'Settings':
        break;
    }
  }
}