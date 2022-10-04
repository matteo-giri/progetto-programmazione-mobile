/*
 Questa classe serve per definire tutti i meccanismi di autenticazione,
 contiene quindi metodi per il login, per la registrazione, per il logout,
 ecc...

 */



import 'package:firebase_database/firebase_database.dart';
import 'package:flutter/material.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:progetto_camilloni_tiseni_giri/nav.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:progetto_camilloni_tiseni_giri/signInPage.dart';

import 'models/Utente.dart';

class AuthenticationService {
  final FirebaseAuth _firebaseAuth; //istanza della classe che gestisce l'autenticazione
  final databaseRef = FirebaseDatabase.instance.reference(); //istanza del database
  AuthenticationService(this._firebaseAuth);

  Stream<User?> get authStateChanges => _firebaseAuth.authStateChanges(); //Lo Stream fornisce un modo per ricevere una sequenza di eventi, in questo caso quelli dovuti ai cambiamenti di stato di firebase

  //funzione per effettuare il logout
  Future<void> signOut(BuildContext context) async { //async rende la funzione asincrona
    await _firebaseAuth.signOut(); //await mette in attesa la funzione che il metodo signOut si completi
    Navigator.pushAndRemoveUntil( //pushAndRemoveUntil pusha la rotta corrente nel Navigator e cancella tutte le precedenti fino a che non succede qualcosa
      context,
      MaterialPageRoute(
          builder: (context) => SignInPage()),
          (Route<dynamic> route) => false, //mettendo il predicato a false le rotte vengono eliminate per sempre dal Navigator
    );
  }

  //funzione per effetuare il login
  Future<String?> signIn(String email, String password, BuildContext context) async{
    try {
      await _firebaseAuth.signInWithEmailAndPassword(email: email, password: password);
      Navigator.pushAndRemoveUntil(
        context,
        MaterialPageRoute(
            builder: (context) => Nav()),
            (Route<dynamic> route) => false,
      );
      Fluttertoast.showToast( //Fluttertoast è una dipendenza esterna che permette di mettere i toast simili ad android su Flutter
        msg: "Login avvenuto correttamente",
        toastLength: Toast.LENGTH_SHORT,
        gravity: ToastGravity.CENTER,
        timeInSecForIosWeb: 1,
      );
      return "Sign in";
    } on FirebaseAuthException catch (e){ //gestione delle varie eccezioni

      if (e.code == 'user-not-found') {
        Fluttertoast.showToast(
            msg: "Non è stato trovato nessun utente con quell'email",
            toastLength: Toast.LENGTH_SHORT,
            gravity: ToastGravity.CENTER,
            timeInSecForIosWeb: 1,
        );
      } else if (e.code == 'wrong-password') {
        Fluttertoast.showToast(
          msg: "La password è errata",
          toastLength: Toast.LENGTH_SHORT,
          gravity: ToastGravity.CENTER,
          timeInSecForIosWeb: 1,
        );
      }
        else if (e.code =='invalid-email'){
        Fluttertoast.showToast(
          msg: "L'email ha un formato non valido",
          toastLength: Toast.LENGTH_SHORT,
          gravity: ToastGravity.CENTER,
          timeInSecForIosWeb: 1,
        );
      }
        else {
        Fluttertoast.showToast(
          msg: "Tutti i campi sono richiesti",
          toastLength: Toast.LENGTH_SHORT,
          gravity: ToastGravity.CENTER,
          timeInSecForIosWeb: 1,
        );
      }

      return e.message;
    }
  }

  //funzione per registrare un nuovo utente
  Future<String?> signUp(String email, String password, String nome, String cognome, BuildContext context) async{
    try {
      if(nome == "" || cognome == ""){ //controllo sui campi nome e cognome
        Fluttertoast.showToast(
          msg: "Nome e cognome sono richiesti",
          toastLength: Toast.LENGTH_SHORT,
          gravity: ToastGravity.CENTER,
          timeInSecForIosWeb: 1,
        );
        return "not registered";
      }
      await _firebaseAuth.createUserWithEmailAndPassword(email: email, password: password);
      final User? firebaseUser = _firebaseAuth.currentUser; //prende l'utente corrente
      if(firebaseUser != null) {
        await databaseRef.child("Users").child(firebaseUser.uid).set({ //inserisce nella rispettiva tabella del realtime database i dati dell'utente
          'firstName': nome,
          'lastName': cognome,
          'wishlist': [],
          'categoriePref': [],
          'iscrizioni': [],
        });
      }
      Navigator.pushAndRemoveUntil(
        context,
        MaterialPageRoute(
            builder: (context) => Nav()),
            (Route<dynamic> route) => false,
      );
      Fluttertoast.showToast(
        msg: "Registrazione avvenuta con successo",
        toastLength: Toast.LENGTH_SHORT,
        gravity: ToastGravity.CENTER,
        timeInSecForIosWeb: 1,
      );
      return "Signed up";
    } on FirebaseAuthException catch (e) { //gestione delle eccezioni
      if (e.code == 'invalid-email') {
        Fluttertoast.showToast(
          msg: "L'email ha un formato non valido",
          toastLength: Toast.LENGTH_SHORT,
          gravity: ToastGravity.CENTER,
          timeInSecForIosWeb: 1,
        );
      }
      else if (e.code == 'weak-password') {
        Fluttertoast.showToast(
        msg: "Scegli una password più sicura",
        toastLength: Toast.LENGTH_SHORT,
        gravity: ToastGravity.CENTER,
        timeInSecForIosWeb: 1,
        );
      }
      else if (e.code == 'email-already-in-use') {
        Fluttertoast.showToast(
          msg: "L'email è già stata utilizzata",
          toastLength: Toast.LENGTH_SHORT,
          gravity: ToastGravity.CENTER,
          timeInSecForIosWeb: 1,
        );
      }
      else {
        Fluttertoast.showToast(
          msg: "Tutti i campi sono richiesti",
          toastLength: Toast.LENGTH_SHORT,
          gravity: ToastGravity.CENTER,
          timeInSecForIosWeb: 1,
        );
        return e.message;
      }
    }
  }
}