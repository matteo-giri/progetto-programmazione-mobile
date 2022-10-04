import 'package:firebase_auth/firebase_auth.dart';
import 'package:firebase_core/firebase_core.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:progetto_camilloni_tiseni_giri/nav.dart';
import 'package:flutter/material.dart';
import 'package:progetto_camilloni_tiseni_giri/signInPage.dart';
import 'package:provider/provider.dart';
import 'dart:developer';
import 'authentication_service.dart';

Future<void> main() async{ //il main è una funziona asincrona, lo è perchè almeno si può utilizzare il metodo await sull'intializeApp di firebase
  WidgetsFlutterBinding.ensureInitialized(); //questa funzione si assicura che sia stata creata un'istanza di WidgetsFlutterBinding, se non è stata istanziata la istanzia
  await Firebase.initializeApp(); //crea e inizializza un'istanza dell'app Firebase
  SystemChrome.setPreferredOrientations([DeviceOrientation.portraitUp]) //blocca l'orientamento del dispositivo
      .then((_) {
    runApp(MyApp());
  });
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MultiProvider( //serve per utilizzare più di un provider
      providers: [ //i providers sono i providers di autenticazione
        Provider<AuthenticationService>(
            create: (_) => AuthenticationService(FirebaseAuth.instance),
        ),
        StreamProvider( // è un providere che ascolta uno Stream e espone il suo contenuto ai figli e discendenti
        create: (context) => context.read<AuthenticationService>().authStateChanges, initialData: null,
        ),
      ],
      child: MaterialApp(
        title: 'Progetto Flutter',
        debugShowCheckedModeBanner: false, //toglie il debug banner
        home: AuthenticationWrapper(),
      ),
    );

  }
}


/*questa classe è la prima che viene chiamata quando si fa partire l'app e
  quello che fa è controllare se c'è un utente loggato, se non c'è porta
  l'utente alla pagina di login, altrimenti lo porta alla home dell'app
 */
class AuthenticationWrapper extends StatelessWidget {
  const AuthenticationWrapper({
    Key? key,
  }) : super(key: key);

  @override
  Widget build(BuildContext context){
    final User? firebaseUser = FirebaseAuth.instance.currentUser;
    if(firebaseUser != null){
      return Nav();
    }
    return SignInPage();
  }
}
