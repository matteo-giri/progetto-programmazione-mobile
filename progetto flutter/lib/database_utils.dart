/*
  In questa classe ci sono tutti i metodi per recuperare dati dal database e utilizzarli nei vari modelli.
  Tutte i parametri e metodi della classe sono statici in modo da poter essere utilizzati nelle altre parti del programma
  senza dover instanziare la classe.
*/

import 'dart:collection';
import 'dart:convert';
import 'package:firebase_database/firebase_database.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:progetto_camilloni_tiseni_giri/models/Corso.dart';
import 'package:progetto_camilloni_tiseni_giri/models/Documento.dart';
import 'package:progetto_camilloni_tiseni_giri/models/Lezione.dart';
import 'package:progetto_camilloni_tiseni_giri/models/Utente.dart';
import 'package:fluttertoast/fluttertoast.dart';

class DatabaseUtils {
  static final _database = FirebaseDatabase.instance.reference(); //istanza del database
  static final FirebaseAuth _firebaseAuth = FirebaseAuth.instance; //istanza dell'oggetto che gestisce l'autenticazione

  //prende l'utente loggato
  static Future<Utente> getUtenteLoggato() async{
    var firebaseUser = _firebaseAuth.currentUser;
    Utente utenteLoggato = Utente(firstName: "",lastName: "", wishlist: [], iscrizioni: [], categoriePref: []);
    if (firebaseUser != null) {
        await _database.child("Users").child(firebaseUser.uid).once().then((DataSnapshot snapshot) { //once ascolta per un singolo evento e poi smette di ascoltare, then è la funzione di callback da attivare quando si è risolto il Future
          var values = snapshot.value; //lo snapshot contiene i dati presi dal database, in questo caso quelli dell'utente loggato
          List<String> wishlist = [];
          List<String> categoriePref = [];
          List<String> iscrizioni = [];
          if(values['wishlist'] != null) {
            for (var wish in values['wishlist']) {
              wishlist.add(wish); //riempe la lista delle wishlist
            }
          }
          if(values['categoriePref'] != null) {
            for (var cat in values['categoriePref']) {
              categoriePref.add(cat); //riempe la lista delle categorie preferite
            }
          }
          if(values['iscrizioni'] != null) {
            for (var iscr in values['iscrizioni']) {
              iscrizioni.add(iscr); //riempe la lista delle iscrizioni
            }
          }
        utenteLoggato = Utente(firstName: values['firstName'],lastName: values['lastName'],wishlist: wishlist, iscrizioni: iscrizioni, categoriePref: categoriePref);
      });
    }
    return utenteLoggato;
  }

  //prende la lista dei corsi
  static Future<List<Corso>> getListaCorsi() async {
    List<Corso> listaCorsi = [];
    await _database.child('Corsi').once().then((DataSnapshot snapshot) {
      for (var values in snapshot.value){
        List<Lezione> listaLezioni = [];
        List<Documento> listaDispense = [];
        List<num> listaRecensioni = [];
        for(var lezione in values["lezioni"]){
          listaLezioni.add(Lezione(descrizione: lezione["descrizione"], id: lezione["id"], titolo: lezione["titolo"], url: lezione["url"]));
        }
        
        if(values["dispense"] != null) {
          for (var doc in values["dispense"]) {
            listaDispense.add(Documento(id: doc["id"], titolo: doc["titolo"], url: doc['url']));
          }
        }
        
        if(values["recensioni"]!= null) {
          Map<String,num> mapRecensioni = Map.castFrom(values["recensioni"]);
          mapRecensioni.forEach((key, value) {
            listaRecensioni.add(value);
          });
        }
        //costruisco la somma degli elemnti della lista di recensioni, serve per ottenere in seguito la media delle recensioni
        num sum = 0;
        for(num recensione in listaRecensioni) {
          sum+=recensione;
        }
        num avg = 0;
        //faccio la media delle recensioni
        if(listaRecensioni.length != 0) {
          avg = sum/listaRecensioni.length;
        }

        var corso = Corso(id: values["id"].toString(), categoria: values["categoria"], descrizione: values["descrizione"], dispense: listaDispense, immagine: values["immagine"], lezioni: listaLezioni, titolo: values["titolo"],recensioni: listaRecensioni,avg:avg, docente: values["docente"] != null? values["docente"]:"Sconosciuto", prezzo: values["prezzo"] != null? values["prezzo"]: "Corso gratuito");
        listaCorsi.add(corso);
      }
    });
    return listaCorsi;
  }

  //funzione che aggiorna gli attributi dell'utente loggato, viene attivata quando clicco sul pulsante "salva" nell'area utente
  static void updateUser(String firstName, String lastName, Set<String> categorie) {
    final User? firebaseUser = _firebaseAuth.currentUser;
    Map<String, dynamic> changeMap = {}; //mappa che conterrà i dati che servono per l'update dell'utente sul DB
    List<String> cat = [];
    for(var c in categorie){
      cat.add(c);
    }

    changeMap['firstName'] = firstName;
    changeMap['lastName'] = lastName;
    changeMap['categoriePref'] = cat;
    _database.child('Users').child(firebaseUser!.uid).update(changeMap); //update aggiorna l'utente con i valori contenuti nella mappa
    Fluttertoast.showToast(
      msg: "Modifiche salvate",
      toastLength: Toast.LENGTH_SHORT,
      gravity: ToastGravity.CENTER,
      timeInSecForIosWeb: 1,
    );
  }

  //funzione che aggiunge il corso alla wishlist se non c'è già, se c'è gia lo elimina. Viene attivata quando clicco sul cuoricino nelle info del corso
  static void updateWishlist(String idCorso){
    final User? firebaseUser = _firebaseAuth.currentUser;
    Map<String, dynamic> changeMap = {}; //mappa in cui inserire le wishlist
    getUtenteLoggato().then((utente) { //devo usare il then perchè getUtenteLoggato ritorna un Future
      for(var i=0; i<utente.wishlist.length; i++){
        changeMap[i.toString()] = utente.wishlist[i]; //ogni wish precedente all'update viene inserita nella mappa
      }
      var found = false;
      var keyDaRimuovere;
      changeMap.forEach((key, value) { //controlla se c'è una wish che ha lo stesso valore dell'id del corso passato come parametro, se lo trova significa che l'utente aveva già la wish nella wishlist e quindi deve essere rimossa
        if(value==idCorso){
          keyDaRimuovere = key;
          found = true;
        }
      });
      if(found) {
        changeMap.remove(keyDaRimuovere);
        Map<String, dynamic> tmpMap = Map();
        int it = 0;
        changeMap.forEach((key, value) { //sposto la mappa in una mappa temporanea per riordinare i valori delle chiavi, dato che una è stata eliminata
          tmpMap[it.toString()] = value;
          it++;
        });
        changeMap = tmpMap;
      }
      else{ //se l'id non è stato trovato significa che la wish è da aggiungere, quindi viene messa come ultimo elemento della mappa che poi verrà settata come wishlist nella tabella dell'utente
        int id = 0;
        changeMap.forEach((key, value) { id++; });
        changeMap[id.toString()] = idCorso;
      }
      _database.child('Users').child(firebaseUser!.uid).child('wishlist').set(changeMap); //set a differenza di update sovrascrive i dati nel db
    });
  }

  //funzione che aggiunge il corso alle iscrizioni se non c'è già, se c'è gia lo elimina (è come la funzione updateWishlist)
  static void updateIscrizioni(String idCorso){
    final User? firebaseUser = _firebaseAuth.currentUser;
    Map<String, dynamic> changeMap = {};
    getUtenteLoggato().then((utente) {
      for(var i=0; i<utente.iscrizioni.length; i++){
        changeMap[i.toString()] = utente.iscrizioni[i];
      }
      var found = false;
      var keyDaRimuovere;
      changeMap.forEach((key, value) {
        if(value==idCorso){
          keyDaRimuovere = key;
          found = true;
        }
      });
      if(found) {
        changeMap.remove(keyDaRimuovere);
        Map<String, dynamic> tmpMap = Map();
        int it = 0;
        changeMap.forEach((key, value) {
          tmpMap[it.toString()] = value;
          it++;
        });
        changeMap = tmpMap;
      }
      else{
        int id = 0;
        changeMap.forEach((key, value) { id++; });
        changeMap[id.toString()] = idCorso;
      }
      _database.child('Users').child(firebaseUser!.uid).child('iscrizioni').set(changeMap);
    });
  }

  //funzione che prende tutte le diverse categorie esistenti nel database
  static Future<Set<String>> getAllCategories() async {
    Set<String> categorie = Set(); //creo un set in modo tale da essere sicuro che non vengano inserite categorie duplicate
    await getListaCorsi().then((corsi){
      for (var corso in corsi){
        categorie.add(corso.categoria);
      }
    });
    return categorie;
  }

  //funzione che ritorna il corso di cui passo l'id
  static Future<Corso> getCorso(String id) async {
    Corso corso = Corso(id: "", categoria: "", descrizione: "", dispense: [], immagine: "", lezioni: [], titolo: "", recensioni: [],avg:0.0, docente: "", prezzo:"");
    await getListaCorsi().then((corsi){
      for (var c in corsi){
        if (c.id == id){
          corso = c;
          break;
        }
      }
    });
    return corso;
  }

  // funzione che estrae dai corsi i corsi a cui è iscritto un certo utente
  static Future<List<Corso>> getIscrizioni() async{
    List<Corso> iscrizioni = [];
    Utente currUser = await getUtenteLoggato();
    await getListaCorsi().then((corsi){
      for(Corso corso in corsi){
        if(currUser.iscrizioni.contains(corso.id)){
          iscrizioni.add(corso);
        }
      }
    });
    return iscrizioni;
  }
}

