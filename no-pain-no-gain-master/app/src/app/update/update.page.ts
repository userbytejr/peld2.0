import { Component, OnInit } from '@angular/core';
import { ImagesService } from '../services/images.service';
import { Images } from '../interfaces/images';

@Component({
  selector: 'app-update',
  templateUrl: './update.page.html',
  styleUrls: ['./update.page.scss'],
})
export class UpdatePage implements OnInit {
  public images: Images = {};
  constructor(private imagesService: ImagesService) { }

  ngOnInit() {
  }

  saveImages(){
    this.imagesService.addImages(this.images);
  }
}
