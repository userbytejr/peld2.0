import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { PeldPage } from './peld.page';

const routes: Routes = [
  {
    path: '',
    component: PeldPage
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class PeldPageRoutingModule {}
