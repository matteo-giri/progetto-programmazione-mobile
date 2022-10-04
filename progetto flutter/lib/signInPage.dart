/*Pagina di login*/

import 'package:flutter/material.dart';
import 'package:progetto_camilloni_tiseni_giri/signUpPage.dart';

import 'authentication_service.dart';
import 'package:provider/provider.dart';
import 'package:progetto_camilloni_tiseni_giri/nav.dart';

class SignInPage extends StatelessWidget {
  final TextEditingController emailController = TextEditingController();
  final TextEditingController passwordController = TextEditingController();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          automaticallyImplyLeading: false,
          title: Text('ProgettoFlutter'),
        ),
      body: Container(
        margin: const EdgeInsets.only(left: 50.0, right: 50.0),
        child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          TextFormField(
            controller: emailController,
            decoration: InputDecoration(
              labelText: 'Email',
              border: OutlineInputBorder(),
            ),
          ),
          SizedBox(height: 50),
          TextFormField(
            controller: passwordController,
            decoration: InputDecoration(
              labelText: 'Password',
              border: OutlineInputBorder(),
            ),
          ),
          SizedBox(height: 50),

          Row(
            mainAxisAlignment: MainAxisAlignment.center,
            children:[
              ElevatedButton(
                onPressed: () {
                  context.read<AuthenticationService>().signIn( //richiama la classe AuthenticationService per utilizzarne il metodo signIn
                      emailController.text.trim(),
                      passwordController.text.trim(),
                      context
                  );
                },
                child: Text("Sign in"),
              ),
              SizedBox(width: 50),
              ElevatedButton(
                onPressed: () {
                  Navigator.pushReplacement(
                    context,
                    MaterialPageRoute(
                        builder: (context) => SignUpPage()),
                  );
                },
                child: Text("Registrati"),
              )
            ],
          )

        ],
      ),
      )
    );

  }
}