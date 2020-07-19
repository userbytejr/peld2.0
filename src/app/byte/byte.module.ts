import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { IonicModule } from '@ionic/angular';

import { BytePageRoutingModule } from './byte-routing.module';

import { BytePage } from './byte.page';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    BytePageRoutingModule
  ],
  declarations: [BytePage]
})
export class BytePageModule {}
