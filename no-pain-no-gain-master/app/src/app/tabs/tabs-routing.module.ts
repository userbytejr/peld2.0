import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { TabsPage } from './tabs.page';

const routes: Routes = [
  {
    path: 'tabs',
    component: TabsPage,
    children: [
      {
        path: 'info',
        children: [
          {
            path: '',
            loadChildren: () =>
              import('../info/info.module').then(m => m.InfoPageModule)
          }
        ]
      },
      {
        path: 'image',
        children: [
          {
            path: '',
            loadChildren: () =>
              import('../image/image.module').then(m => m.ImagePageModule)
          }
        ]
      },
      {
        path: 'video',
        children: [
          {
            path: '',
            loadChildren: () =>
              import('../video/video.module').then(m => m.VideoPageModule)
          }
        ]
      },
      {
        path: 'map',
        children: [
          {
            path: '',
            loadChildren: () =>
              import('../map/map.module').then(m => m.MapPageModule)
          }
        ]
      },
      {
        path: '',
        redirectTo: '/tabs/info',
        pathMatch: 'full'
      }
    ]
  },
  {
    path: '',
    redirectTo: '/tabs/info',
    pathMatch: 'full'
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class TabsPageRoutingModule {}
