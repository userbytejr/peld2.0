import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { BytePage } from './byte.page';

const routes: Routes = [
  {
    path: '',
    component: BytePage
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class BytePageRoutingModule {}
