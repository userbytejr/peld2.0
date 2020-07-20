//
//  VideoVrVC.h
//  GoogleVrViewTest
//
//  Created by Leonardo Tonghini on 12/05/17.
//  Copyright © 2017 Leonardo Tonghini. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "VrControllerViewController.h"
#import "GVRVideoView.h"

@interface VideoVrVC : VrControllerViewController <GVRVideoViewDelegate> {
    GVRVideoView *_videoView;
    bool _isPaused;
}

@property NSURL *videoURL;
@property GVRVideoType videoType;

@end
