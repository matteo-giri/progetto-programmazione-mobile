/*Pagina di registrazione*/

import 'package:flutter/material.dart';
import 'package:progetto_camilloni_tiseni_giri/signInPage.dart';

import 'authentication_service.dart';
import 'package:provider/provider.dart';
import 'package:progetto_camilloni_tiseni_giri/nav.dart';

class SignUpPage extends StatelessWidget {
  final TextEditingController emailController = TextEditingController();
  final TextEditingController passwordController = TextEditingController();
  final TextEditingController firstnameController = TextEditingController();
  final TextEditingController lastnameController = TextEditingController();
  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          automaticallyImplyLeading: false,
          title: Text('ProgettoFlutter'),
        ),
      body: Container(
        margin: const EdgeInsets.only(left: 50.0, right: 50.0, top:100.0),
        child: SingleChildScrollView(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              TextFormField(
                controller: firstnameController,
                decoration: InputDecoration(
                  labelText: 'Il tuo nome',
                  border: OutlineInputBorder(),
                ),
              ),
              SizedBox(height: 50),
              TextFormField(
                controller: lastnameController,
                decoration: InputDecoration(
                  labelText: 'Il tuo cognome',
                  border: OutlineInputBorder(),
                ),
              ),
              SizedBox(height: 50),
              TextFormField(
                controller: emailController,
                decoration: InputDecoration(
                  labelText: 'Scegli una email',
                  border: OutlineInputBorder(),
                ),
              ),
              SizedBox(height: 50),
              TextFormField(
                controller: passwordController,
                decoration: InputDecoration(
                  labelText: 'Scegli una password',
                  border: OutlineInputBorder(),
                ),
              ),
              SizedBox(height: 50),

              Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children:[
                  ElevatedButton(
                    onPressed: () {
                      context.read<AuthenticationService>().signUp( //richiama la classe AuthenticationService per utilizzarne la sua funzione di signup
                          emailController.text.trim(),
                          passwordController.text.trim(),
                          firstnameController.text.trim(),
                          lastnameController.text.trim(),
                          context
                      );
                    },
                    child: Text("Registrati"),
                  ),
                  SizedBox(width: 50),
                  ElevatedButton(
                    onPressed: () {
                      Navigator.pushReplacement( //pushReplacement serve per non permettere di tornare indietro, perchè alle pagine di login e registrazione ci si va cliccando i bottoni corrispondenti
                        context,
                        MaterialPageRoute(
                            builder: (context) => SignInPage()),
                      );
                    },
                    child: Text("vai al login"),
                  )
                ],
              )

            ],
          ),
        ),
      )
    );

  }
}