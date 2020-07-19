//
//  FotoVrVC.h
//  GoogleVrViewTest
//
//  Created by Leonardo Tonghini on 12/05/17.
//  Copyright © 2017 Leonardo Tonghini. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "VrControllerViewController.h"
#import "GVRPanoramaView.h"

@interface PanoramaVrVC : VrControllerViewController {
    GVRPanoramaView *_panoView;
}

@property NSURL *panoramaURL;
@property GVRPanoramaImageType panoramaType;

@end
