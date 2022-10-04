import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/material.dart';
import 'package:progetto_camilloni_tiseni_giri/database_utils.dart';
import 'package:progetto_camilloni_tiseni_giri/signInPage.dart';
import 'package:flutter_test/src/widget_tester.dart';
import 'package:flutter_test/flutter_test.dart';
import 'authentication_service.dart';
import 'package:provider/provider.dart';
import 'package:progetto_camilloni_tiseni_giri/nav.dart';


import 'models/Utente.dart';

class AreaUtente extends StatefulWidget{

  _AreaUtente createState() => _AreaUtente();
}
Set<String> categoriePreferite = Set();
class _AreaUtente extends State<AreaUtente>{
  final TextEditingController firstnameController = TextEditingController();
  final TextEditingController lastnameController = TextEditingController();
  List<Widget> listOfChips1 = [];
  List<Widget> listOfChips2 = [];

  initState() { //richiamato solo all'inizio, serve per settare lo stato iniziale
    DatabaseUtils.getUtenteLoggato().then((utente){
      firstnameController.text = utente.firstName;
      lastnameController.text = utente.lastName;
      populateChips(utente).then((chips){ //richiama la funzione che ritorna la lista di chips delle categorie
        setState(() {
        listOfChips1 = [];
        listOfChips2 = [];
        for(int i=0; i< chips.length; i++){
          if(i%2 == 0) {
            listOfChips1.add(chips.elementAt(i));
          }
          else {
            listOfChips2.add(chips.elementAt(i));
          }
        }
        print(listOfChips1.length);
        print(listOfChips2.length);
        });
      });
    });
  }
  @override
  Widget build(BuildContext context){




    return Scaffold(
      body: Container(
        margin: const EdgeInsets.only(top: 10.00, left: 10.00, right:10.00),
        child: SingleChildScrollView(
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text("I tuoi dati:",
                style: TextStyle(fontSize: 30),
              ),
              SizedBox(height:10),
              TextFormField(
                controller: firstnameController,
                decoration: InputDecoration(
                  labelText: "Nome",
                  border: OutlineInputBorder(),
                ),
              ),
              SizedBox(height: 10),
              TextFormField(
                controller: lastnameController,
                decoration: InputDecoration(
                  labelText: 'Cognome',
                  border: OutlineInputBorder(),
                ),
              ),
              SizedBox(height:10),
              Text("Le tue categorie preferite:",
                style: TextStyle(fontSize: 30),
              ),
              SizedBox(height:10),
              Container(
                height: 50,
                child:ListView(
                    scrollDirection: Axis.horizontal,
                    children: listOfChips1,
                ),
              ),
              Container(
                height: 50,
                child:ListView(
                    scrollDirection: Axis.horizontal,
                    children: listOfChips2,
                ),
              ),
              SizedBox(height:40),
              Row(
                  crossAxisAlignment: CrossAxisAlignment.center,
                  mainAxisAlignment: MainAxisAlignment.center,
                  children:[
                    ElevatedButton(
                      onPressed: () {
                        context.read<AuthenticationService>().signOut(context); //richiama la classe AuthenticationService per utilizzarne il metodo di logout
                      },
                      child: Text("Logout"),
                    ),
                    SizedBox(width:50),
                    ElevatedButton(
                      onPressed: () {
                        DatabaseUtils.updateUser(firstnameController.text, lastnameController.text, categoriePreferite); //chiama la funzione che aggiorna i dati dell'utente
                      },
                      child: Text("Salva le modifiche"),
                    ),
                  ]
              )
            ],
          ),
        )
      )
    );
  }

  //questa funzione popola le chips con le categorie prese dal db, e "checka" quelle preferite dell'utente
  Future<List<Widget>> populateChips(Utente utente) async{
    List<Widget> chips = [];
    List<String> categoriePref = utente.categoriePref;
    bool checked = false;


    await DatabaseUtils.getAllCategories().then((categorie) {
      for (int i = 0; i < categorie.length; i++) {
        checked = false;
        if (categoriePref != [])
          for (var cat in categoriePref) {
            if (categorie.elementAt(i) == cat) {
              categoriePreferite.add(categorie.elementAt(i));
              checked = true;
            }
          }
        chips.add(filterChipWidget(categorie.elementAt(i), checked)); //aggiunge alla lista delle chips un nuovo elemento della filterChipWidget, passandogli il nome e il bool per capire se la chip deve essere checkata
      }
    }
    );
    return chips;
  }
}

//classe per la strutturazione delle chips checkabili
class filterChipWidget extends StatefulWidget{
  final String chipName;
  bool checked;
  filterChipWidget(this.chipName, this.checked);

  @override
  _filterChipWidgetState createState() => _filterChipWidgetState();
}

class _filterChipWidgetState extends State<filterChipWidget> {


  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.all(2.0),
      child: FilterChip(
        label: Text(widget.chipName),
        selected: widget.checked,
        onSelected: (isSelected) { //se viene selezionata, viene aggiunta la categoria corrispondente alla lista che tiene conto di tutte le categorie preferite dell'utente
          if(isSelected)
            categoriePreferite.add(widget.chipName);
          else
            categoriePreferite.remove(widget.chipName);
          setState(() { //cambia la variabile checked per aggiornare lo stato della chip, in modo da visualizzare la spunta o meno dinamicamente
            widget.checked = isSelected;
          });
        },
      )
    );
  }
}

