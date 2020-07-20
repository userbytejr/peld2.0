import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { IonicModule } from '@ionic/angular';

import { PeldPageRoutingModule } from './peld-routing.module';

import { PeldPage } from './peld.page';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    PeldPageRoutingModule
  ],
  declarations: [PeldPage]
})
export class PeldPageModule {}
