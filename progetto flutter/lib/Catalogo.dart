import 'dart:collection';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:progetto_camilloni_tiseni_giri/CardCorso.dart';
import 'package:progetto_camilloni_tiseni_giri/Categoria.dart';
import 'package:progetto_camilloni_tiseni_giri/Ricerca.dart';
import 'package:progetto_camilloni_tiseni_giri/database_utils.dart';

import 'models/Corso.dart';
import 'nav.dart';

class Catalogo extends StatefulWidget{

  @override
  _Catalogo createState() => _Catalogo();
}

class _Catalogo extends State<Catalogo>{
  final TextEditingController searchBarController = TextEditingController(); //controller della search bar

  HashMap<String,List<Widget>> mapCorsi = HashMap<String,List<Widget>>();
  Set<String> listCategorie = Set();
  List<Widget> listOfChips1 = [];
  List<Widget> listOfChips2 = [];

  initState() { //richiamato solo all'inizio, serve per settare lo stato iniziale

    DatabaseUtils.getAllCategories().then((categorie){
      setState(() {
        listCategorie.addAll(categorie);
      });
    });
    corsiByCat().then((corsiByCat){ //richiama la funzione che crea una mappa di corsi in base alla loro categoria e la inserisce nella mapCorsi
      setState(() {
        mapCorsi.addAll(corsiByCat);
      });
    });
    populateChips(context).then((chips){ //qui si richiama la funzione che popola le chips dinamicamente in base alle varie categorie presenti nel db
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
      });
    });
  }

  @override
  Widget build(BuildContext context){
    corsiByCat().then((corsiByCat){
      mapCorsi = corsiByCat;
    });
    return ListView.builder(
        padding: EdgeInsets.only(top:10, left:10, bottom:10),
        itemCount: listCategorie.length+1,
        itemBuilder: (context,index) {
          if (index == 0) {
            return Column(
              children: <Widget>[
                Padding(
                  padding: const EdgeInsets.all(8.0),
                  child: TextFormField(
                    textInputAction: TextInputAction.search,
                    controller: searchBarController,
                    onFieldSubmitted:(value){
                      Navigator.push(context, MaterialPageRoute(builder: (context) { //se submitto la query di ricerca mi manda alla pagina di ricerca passandogli la query come "value"
                        return Ricerca(value);
                      }));
                    } ,
                    decoration: InputDecoration(
                      labelText: 'Cerca',
                      border: OutlineInputBorder(),
                      prefixIcon: Icon(Icons.search)
                    ),
                  ),
                ),
                Container(
                  height: 100,
                  child:Column(
                    children:[
                      Container(
                      height: 50,
                      child:ListView(
                          scrollDirection: Axis.horizontal,
                          children: listOfChips1
                        ),
                      ),
                      Container(
                        height: 50,
                        child:ListView(
                            scrollDirection: Axis.horizontal,
                            children: listOfChips2
                        ),
                      ),
                    ]
                  ),
                ),
              ],
            );
          }
          return Column(
            children:[
              SizedBox(height: 20),
              Align(
                  alignment: Alignment.centerLeft,
                  child:Text("Top in " + listCategorie.elementAt(index-1), style: TextStyle(fontSize: 24))
              ),
              Container(
                height:200.0,
                child:ListView(
                  scrollDirection: Axis.horizontal,
                  children: mapCorsi[listCategorie.elementAt(index-1)] != null ? mapCorsi[listCategorie.elementAt(index-1)]! : new List.empty()
                ),
              )
            ],
          );
        }
    );
  }
}
//funzione che popola la lista di chip
Future<List<Widget>> populateChips(BuildContext context) async{
  List<Widget> chips = [];

  await DatabaseUtils.getAllCategories().then((categorie) {
    for (int i = 0; i < categorie.length; i++) {
      chips.add(
        Padding(
          padding: const EdgeInsets.all(2.0),
          child: ActionChip(
            label: Text(categorie.elementAt(i)),
            onPressed: () { //se si preme su una categoria ti porta alla pagina dove visualizzare i corsi di quella categoria passandogli la categoria cliccata come parametro
              Navigator.push(context, MaterialPageRoute(builder: (context) {
                return Categoria(categorie.elementAt(i));
              }));
              },
          ),
        ),
      );
    }
  }
  );
  return chips;
}

// ritorna un hashmap in cui la chiave è la categoria a cui appartengono i corsi e il valore è la lista
// dei corsi appartenenti a quella categoria
Future<HashMap<String,List<Widget>>> corsiByCat() async {
  HashMap<String,List<Widget>> corsiByCat = HashMap();
  List<Corso> corsi = await DatabaseUtils.getListaCorsi();
  Set<String> categorie = await DatabaseUtils.getAllCategories();
  for(String categoria in categorie){
    List<Widget> tmp = [];
    for(Corso corso in corsi){
      if(corso.categoria == categoria){
        tmp.add(CardCorso(corso));
      }
    }
    corsiByCat.putIfAbsent(categoria, () => tmp); // () => tmp sarebbe una funzione vuota che ritorna tmp, non metto direttamente tmp perchè putIfAbsent richiede una funzione come secondo parametro
  }
  return corsiByCat;
}