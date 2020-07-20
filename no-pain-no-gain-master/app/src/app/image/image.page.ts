import { Component, OnInit } from '@angular/core';
import { PhotoViewer } from '@ionic-native/photo-viewer/ngx';
import { Network } from '@ionic-native/network/ngx';
import { Dialogs } from '@ionic-native/dialogs/ngx';
import { Images } from 'src/app/interfaces/images';
import { Subscription } from 'rxjs';
import { ImagesService } from '../services/images.service';
import { EventHandlerVars } from '@angular/compiler/src/compiler_util/expression_converter';

@Component({
  selector: 'app-image',
  templateUrl: './image.page.html',
  styleUrls: ['./image.page.scss'],
})
export class ImagePage implements OnInit {
  public images = new Array<Images>();
  private imagesSubscription: Subscription;

  //Scroll Infinite
  imagesPage: any = [];
  private readonly offset: number = 4; //número de cards por carregamento
  private index: number = 0;

  constructor(private photoViewer: PhotoViewer, private network: Network, private dialogs: Dialogs, private imagesService: ImagesService) {

    this.imagesSubscription = this.imagesService.getImages().subscribe(data => {
      this.images = data;
      //Scroll Infinite
      this.imagesPage = this.images.slice(this.index, this.offset + this.index);
      this.index += this.offset;
    });

    this.network.onDisconnect().subscribe(() => {
      this.dialogs.alert('Você não possui conexão a internet!');
    });
  }

  //Scroll Infinite
  loadData(event) {
    setTimeout(() => {
      let news = this.images.slice(this.index, this.offset + this.index);
      this.index += this.offset;
      for (let i = 0; i < news.length; i++) {
        this.imagesPage.push(news[i]);
      }
      event.target.complete();

      if(this.imagesPage.length === this.images.length){
        event.target.disabled = true;
      }
    }, 2000); //tempo de espera para abrir mais cards
  }

  ngOnInit() {
  }

  ngOnDestroy() {
    this.imagesSubscription.unsubscribe();
  }

  photoView(photoUrl, title) {
    this.photoViewer.show(photoUrl, title);
  }

  vrView(vrUrl) {
    window['VrView'].showPhoto(vrUrl,
      {
        inputType: "TYPE_MONO",
        startDisplayMode: "DISPLAY_MODE_FULLSCREEN"
      }
    );
  }
}