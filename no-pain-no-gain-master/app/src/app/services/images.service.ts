import { Injectable } from '@angular/core';
import { Images } from '../interfaces/images'
import { AngularFirestoreCollection, AngularFirestore } from '@angular/fire/firestore';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class ImagesService {
  private imagesCollection: AngularFirestoreCollection<Images>;
  images = []
  constructor(private afs: AngularFirestore) {
    this.imagesCollection = this.afs.collection<Images>('Images');
  }

  getImages() {
    return this.imagesCollection.snapshotChanges().pipe(
      map(actions => {
        return actions.map(a => {
          const data = a.payload.doc.data();
          const id = a.payload.doc.id;

          return { id, ...data };
        });
      })
    )
  }

  // async getImagesMap(){
  //   return await this.imagesCollection.snapshotChanges().pipe(
  //     map(actions => {
  //       return actions.map(a => {
  //         const data = a.payload.doc.data();
  //         const id = a.payload.doc.id;

  //         return { id, ...data };
  //       });
  //     })
  //   )

  // }
  async getImagesMap(): Promise<void> {
    const images = await this.imagesCollection.snapshotChanges().pipe(
      map(actions => {
        return actions.map(a => {
          const data = a.payload.doc.data();
          const id = a.payload.doc.id;

          return { id, ...data };
        });
      })
    )
 
  }
  addImages(images: Images){
    return this.imagesCollection.add(images);
  }
}