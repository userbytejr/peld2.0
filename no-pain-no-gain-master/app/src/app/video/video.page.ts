import { Component, OnInit } from '@angular/core';
import { YoutubeVideoPlayer } from '@ionic-native/youtube-video-player/ngx';
//import { HTTP } from '@ionic-native/http/ngx';
import { HttpClient } from '@angular/common/http';
import { Network } from '@ionic-native/network/ngx';
import { Dialogs } from '@ionic-native/dialogs/ngx';

@Component({
  selector: 'app-video',
  templateUrl: './video.page.html',
  styleUrls: ['./video.page.scss'],
})

export class VideoPage implements OnInit {
  videoIds = [
    '4r7698cnP68',
    'OPzWdNJHkPQ'
  ];
  videos = [];
  
  constructor(private youtube: YoutubeVideoPlayer, private http: HttpClient, private network: Network, private dialogs: Dialogs) { 
    
    this.network.onDisconnect().subscribe(() => {
      this.dialogs.alert('Você não possui conexão a internet!');
    });
  }

  ngOnInit() {
    this.videoIds
    .forEach (id => 
      this.http.get('https://www.googleapis.com/youtube/v3/videos?part=id,snippet&id=' + id + '&key=AIzaSyCztbw3brynJiPfLF0iUw_GE1AgWQOcC1A')
      .subscribe(response => 
        this.videos.push(response)//response.items[0].snippet)
      )
    );
  }

  watch(idVideo){
    this.youtube.openVideo(idVideo);
  }
}