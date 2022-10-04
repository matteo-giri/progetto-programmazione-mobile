/*
  Classe che implementa lo screen per i video, utilizza il package youtube_player_iframe
 */

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
//import 'package:youtube_player_flutter/youtube_player_flutter.dart';
import 'package:youtube_player_iframe/youtube_player_iframe.dart';
class VideoScreen extends StatefulWidget{

  final String id;

  VideoScreen(this.id);

  @override
  _VideoScreenState createState() => _VideoScreenState();
}
//classe per creare lo youtube player
class _VideoScreenState extends State<VideoScreen>{

  YoutubePlayerController _controller = YoutubePlayerController(initialVideoId: "");

  @override
  Widget build(BuildContext context){
    YoutubePlayerController _controller = YoutubePlayerController(
      initialVideoId: widget.id,
      params: YoutubePlayerParams(
        showControls: true,
        showFullscreenButton: false,
        autoPlay:false,
      ),
    );
    return Container(
        height:200,
        child:YoutubePlayerIFrame(
          controller: _controller,
          aspectRatio: 16/9,
        )
    );

  }
}