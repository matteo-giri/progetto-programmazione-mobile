import 'package:flutter/material.dart';
import 'package:progetto_camilloni_tiseni_giri/Video_screen.dart';

import 'models/Lezione.dart';

class LezioneLista extends StatefulWidget {
  final Lezione lezione;

  const LezioneLista(this.lezione);

  _LezioneLista createState() => _LezioneLista();
}
class _LezioneLista extends State<LezioneLista>{

  bool isAperto = false;

  @override
  Widget build(BuildContext context) {
    return Card(
        child:Column(
            children: [
                ListTile(
                  trailing:isAperto ?Icon(Icons.keyboard_arrow_down_sharp) :Icon(Icons.keyboard_arrow_right_sharp),
                  title: Text(widget.lezione.titolo, style: TextStyle(fontSize: 14)),
                  tileColor: Colors.white54,
                  onTap: (){
                    setState(() {
                      isAperto = !isAperto;
                    });
                  },
                ),
                Visibility(
                  visible: isAperto,
                  child:Column(
                    crossAxisAlignment: CrossAxisAlignment.stretch,
                    children: [
                      VideoScreen(widget.lezione.url),
                      Padding(
                        padding: EdgeInsets.only(top:10, left:10, bottom:10),
                        child:Align(
                            alignment: Alignment.centerLeft,
                            child: Text(widget.lezione.descrizione, style: TextStyle(fontSize: 12))
                        )
                      )
                    ],
                  )
                ),
              ],
        ),
    );
  }

}